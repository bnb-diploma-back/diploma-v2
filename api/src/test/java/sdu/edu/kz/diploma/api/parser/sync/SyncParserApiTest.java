package sdu.edu.kz.diploma.api.parser.sync;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.parser.mongo.*;
import sdu.edu.kz.diploma.library.model.enums.Semester;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;
import sdu.edu.kz.diploma.library.test.BaseTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Transactional
class SyncParserApiTest extends BaseTest {

    @Autowired
    private SyncParserApi syncParserApi;

    @Autowired
    private SyllabusRepository syllabusRepository;

    @MockitoBean
    private MongoCourseRepository mongoCourseRepository;

    @MockitoBean
    private MongoCurriculumCourseRepository mongoCurriculumCourseRepository;

    @MockitoBean
    private MongoSyllabusRepository mongoSyllabusRepository;

    @BeforeEach
    void setUp() {
        remover.all();
        when(mongoCourseRepository.findAll()).thenReturn(List.of());
        when(mongoCurriculumCourseRepository.findAll()).thenReturn(List.of());
        when(mongoSyllabusRepository.findAll()).thenReturn(List.of());
    }

    @Test
    void sync_createsNewSyllabusFromCurriculumCourse() {
        final var course = new MongoCurriculumCourse();
        course.setCourseCode("CSS 105");
        course.setCourseName("Fundamentals of Programming");
        course.setCredit(4);
        course.setTerm(1);
        course.setRequisites("MAT 101");
        when(mongoCurriculumCourseRepository.findAll()).thenReturn(List.of(course));

        final var result = syncParserApi.sync();

        assertThat(result.created()).isEqualTo(1);
        final var saved = syllabusRepository.findByCourseCodeAndSemester("CSS 105", Semester.FALL);
        assertThat(saved).isPresent();
        assertThat(saved.get().getTitle()).isEqualTo("Fundamentals of Programming");
        assertThat(saved.get().getCredits()).isEqualTo(4);
        assertThat(saved.get().getPrerequisites()).isEqualTo("MAT 101");
    }

    @Test
    void sync_createsNewSyllabusFromElectiveCourse() {
        final var course = new MongoCourse();
        course.setCourseCode("CSS 301");
        course.setCourseName("Web Development");
        course.setCredit(3);
        course.setTerm(5);
        when(mongoCourseRepository.findAll()).thenReturn(List.of(course));

        final var result = syncParserApi.sync();

        assertThat(result.created()).isEqualTo(1);
        final var saved = syllabusRepository.findByCourseCodeAndSemester("CSS 301", Semester.FALL);
        assertThat(saved).isPresent();
        assertThat(saved.get().getTitle()).isEqualTo("Web Development");
    }

    @Test
    void sync_updatesExistingSyllabus() {
        final var existing = creator.syllabus(
                new sdu.edu.kz.diploma.library.test.builder.SyllabusTest()
                        .courseCode("CSS 105")
                        .semester(Semester.FALL)
        );

        final var course = new MongoCurriculumCourse();
        course.setCourseCode("CSS 105");
        course.setCourseName("Updated Name");
        course.setCredit(5);
        course.setTerm(1);
        when(mongoCurriculumCourseRepository.findAll()).thenReturn(List.of(course));

        final var result = syncParserApi.sync();

        assertThat(result.updated()).isEqualTo(1);
        assertThat(result.created()).isEqualTo(0);
        final var updated = syllabusRepository.findByCourseCodeAndSemester("CSS 105", Semester.FALL);
        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("Updated Name");
        assertThat(updated.get().getCredits()).isEqualTo(5);
    }

    @Test
    void sync_enrichesSyllabusWithDescription() {
        creator.syllabus(
                new sdu.edu.kz.diploma.library.test.builder.SyllabusTest()
                        .courseCode("CSS 105")
                        .semester(Semester.FALL)
        );

        final var mongoSyllabus = new MongoSyllabus();
        mongoSyllabus.setCourseCode("CSS 105");
        mongoSyllabus.setTerm(1);

        final var description = new MongoSyllabus.Description();
        description.setCourseDescription("Learn Java basics");

        final var instructor = new MongoSyllabus.Instructor();
        instructor.setName("John Doe");
        instructor.setEmail("john@sdu.edu.kz");
        description.setInstructors(List.of(instructor));

        mongoSyllabus.setDescription(description);
        when(mongoSyllabusRepository.findAll()).thenReturn(List.of(mongoSyllabus));

        final var result = syncParserApi.sync();

        assertThat(result.enriched()).isEqualTo(1);
        final var enriched = syllabusRepository.findByCourseCodeAndSemester("CSS 105", Semester.FALL);
        assertThat(enriched).isPresent();
        assertThat(enriched.get().getDescription()).isEqualTo("Learn Java basics");
        assertThat(enriched.get().getInstructor()).isEqualTo("John Doe");
    }

    @Test
    void sync_enrichesSyllabusWithWeeklyPlans() {
        creator.syllabus(
                new sdu.edu.kz.diploma.library.test.builder.SyllabusTest()
                        .courseCode("CSS 105")
                        .semester(Semester.FALL)
        );

        final var mongoSyllabus = new MongoSyllabus();
        mongoSyllabus.setCourseCode("CSS 105");
        mongoSyllabus.setTerm(1);

        final var contents = new MongoSyllabus.Contents();
        contents.setWeeklyCoursePlan(List.of(
                Map.of("Week №", "1", "Topics", "Introduction", "Activity", "Quiz 1"),
                Map.of("Week №", "2", "Topics", "Variables", "Activity", "Quiz 2"),
                Map.of("Week №", "3", "Topics", "Control Flow", "Activity", "Quiz 3")
        ));
        mongoSyllabus.setContents(contents);
        when(mongoSyllabusRepository.findAll()).thenReturn(List.of(mongoSyllabus));

        final var result = syncParserApi.sync();

        assertThat(result.enriched()).isEqualTo(1);
        final var enriched = syllabusRepository.findByCourseCodeAndSemester("CSS 105", Semester.FALL);
        assertThat(enriched).isPresent();

        final var syllabus = enriched.get();
        assertThat(syllabus.getWeeklyPlans()).hasSize(3);
        assertThat(syllabus.getWeeklyPlans().get(0).getWeekNumber()).isEqualTo(1);
        assertThat(syllabus.getWeeklyPlans().get(0).getTopic()).isEqualTo("Introduction");
        assertThat(syllabus.getWeeklyPlans().get(0).getAssignments()).isEqualTo("Quiz 1");
        assertThat(syllabus.getWeeklyPlans().get(2).getWeekNumber()).isEqualTo(3);
        assertThat(syllabus.getWeeklyPlans().get(2).getTopic()).isEqualTo("Control Flow");
    }

    @Test
    void sync_skipsEnrichmentWhenNoMatchingSyllabus() {
        final var mongoSyllabus = new MongoSyllabus();
        mongoSyllabus.setCourseCode("NONEXISTENT");
        mongoSyllabus.setTerm(1);
        when(mongoSyllabusRepository.findAll()).thenReturn(List.of(mongoSyllabus));

        final var result = syncParserApi.sync();

        assertThat(result.enriched()).isEqualTo(0);
    }

    @Test
    void sync_mapsOddTermToFallEvenTermToSpring() {
        final var fallCourse = new MongoCurriculumCourse();
        fallCourse.setCourseCode("CSS 101");
        fallCourse.setCourseName("Fall Course");
        fallCourse.setCredit(3);
        fallCourse.setTerm(3);

        final var springCourse = new MongoCurriculumCourse();
        springCourse.setCourseCode("CSS 102");
        springCourse.setCourseName("Spring Course");
        springCourse.setCredit(3);
        springCourse.setTerm(4);

        when(mongoCurriculumCourseRepository.findAll()).thenReturn(List.of(fallCourse, springCourse));

        syncParserApi.sync();

        assertThat(syllabusRepository.findByCourseCodeAndSemester("CSS 101", Semester.FALL)).isPresent();
        assertThat(syllabusRepository.findByCourseCodeAndSemester("CSS 102", Semester.SPRING)).isPresent();
    }

    @Test
    void sync_returnsZerosWhenMongoIsEmpty() {
        final var result = syncParserApi.sync();

        assertThat(result.created()).isEqualTo(0);
        assertThat(result.updated()).isEqualTo(0);
        assertThat(result.enriched()).isEqualTo(0);
    }

    @Test
    void sync_fullFlow_createsThenEnriches() {
        final var course = new MongoCurriculumCourse();
        course.setCourseCode("CSS 200");
        course.setCourseName("Data Structures");
        course.setCredit(4);
        course.setTerm(3);
        when(mongoCurriculumCourseRepository.findAll()).thenReturn(List.of(course));

        final var mongoSyllabus = new MongoSyllabus();
        mongoSyllabus.setCourseCode("CSS 200");
        mongoSyllabus.setTerm(3);
        final var description = new MongoSyllabus.Description();
        description.setCourseDescription("Learn data structures");
        mongoSyllabus.setDescription(description);
        final var contents = new MongoSyllabus.Contents();
        contents.setWeeklyCoursePlan(List.of(
                Map.of("Week №", "1", "Topics", "Arrays", "Activity", "Lab 1")
        ));
        mongoSyllabus.setContents(contents);
        when(mongoSyllabusRepository.findAll()).thenReturn(List.of(mongoSyllabus));

        final var result = syncParserApi.sync();

        assertThat(result.created()).isEqualTo(1);
        assertThat(result.enriched()).isEqualTo(1);

        final var saved = syllabusRepository.findByCourseCodeAndSemester("CSS 200", Semester.FALL);
        assertThat(saved).isPresent();
        assertThat(saved.get().getTitle()).isEqualTo("Data Structures");
        assertThat(saved.get().getDescription()).isEqualTo("Learn data structures");
        assertThat(saved.get().getWeeklyPlans()).hasSize(1);
        assertThat(saved.get().getWeeklyPlans().getFirst().getTopic()).isEqualTo("Arrays");
    }
}