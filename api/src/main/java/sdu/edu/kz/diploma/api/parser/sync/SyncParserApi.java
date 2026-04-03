package sdu.edu.kz.diploma.api.parser.sync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.parser.mongo.*;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.model.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.model.enums.Semester;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncParserApi {

    private final MongoCourseRepository mongoCourseRepository;
    private final MongoCurriculumCourseRepository mongoCurriculumCourseRepository;
    private final MongoSyllabusRepository mongoSyllabusRepository;
    private final SyllabusRepository syllabusRepository;

    @Transactional
    public SyncResult sync() {
        var created = 0;
        var updated = 0;

        final var curriculumCourses = mongoCurriculumCourseRepository.findAll();
        log.info("Fetched {} curriculum courses from MongoDB", curriculumCourses.size());
        for (final var course : curriculumCourses) {
            final var result = syncCourse(course.getCourseCode(), course.getCourseName(),
                    course.getCredit(), course.getTerm(), course.getRequisites());
            if (result) created++; else updated++;
        }

        final var electiveCourses = mongoCourseRepository.findAll();
        log.info("Fetched {} elective courses from MongoDB", electiveCourses.size());
        for (final var course : electiveCourses) {
            final var result = syncCourse(course.getCourseCode(), course.getCourseName(),
                    course.getCredit(), course.getTerm(), null);
            if (result) created++; else updated++;
        }

        final var syllabuses = mongoSyllabusRepository.findAll();
        log.info("Fetched {} syllabuses from MongoDB", syllabuses.size());
        var enriched = 0;
        for (final var mongoSyllabus : syllabuses) {
            if (enrichSyllabus(mongoSyllabus)) enriched++;
        }

        log.info("Sync complete: created={}, updated={}, enriched={}", created, updated, enriched);
        return new SyncResult(created, updated, enriched);
    }

    private boolean syncCourse(String courseCode, String courseName, Integer credit,
                               Integer term, String requisites) {
        final var semester = toSemester(term);
        final var existing = syllabusRepository.findByCourseCodeAndSemester(courseCode, semester);

        if (existing.isPresent()) {
            final var syllabus = existing.get();
            syllabus.setTitle(courseName);
            syllabus.setCredits(credit);
            syllabus.setPrerequisites(requisites);
            syllabusRepository.save(syllabus);
            return false;
        }

        final var syllabus = Syllabus.builder()
                .courseCode(courseCode)
                .title(courseName)
                .credits(credit)
                .semester(semester)
                .prerequisites(requisites)
                .build();
        syllabusRepository.save(syllabus);
        return true;
    }

    private boolean enrichSyllabus(MongoSyllabus mongoSyllabus) {
        final var semester = toSemester(mongoSyllabus.getTerm());
        final var existing = syllabusRepository.findByCourseCodeAndSemester(
                mongoSyllabus.getCourseCode(), semester);

        if (existing.isEmpty()) {
            log.warn("No syllabus found for courseCode={}, term={} — skipping enrichment",
                    mongoSyllabus.getCourseCode(), mongoSyllabus.getTerm());
            return false;
        }

        final var syllabus = existing.get();

        if (mongoSyllabus.getDescription() != null) {
            syllabus.setDescription(mongoSyllabus.getDescription().getCourseDescription());

            final var instructors = mongoSyllabus.getDescription().getInstructors();
            if (instructors != null && !instructors.isEmpty()) {
                syllabus.setInstructor(instructors.stream()
                        .map(MongoSyllabus.Instructor::getName)
                        .collect(Collectors.joining(", ")));
            }
        }

        if (mongoSyllabus.getBasic() != null) {
            final var basic = mongoSyllabus.getBasic();
            if (basic.containsKey("language")) {
                syllabus.setObjectives(basic.get("language"));
            }
        }

        if (mongoSyllabus.getContents() != null) {
            enrichFromContents(syllabus, mongoSyllabus.getContents());
        }

        syllabusRepository.save(syllabus);
        return true;
    }

    private void enrichFromContents(Syllabus syllabus, MongoSyllabus.Contents contents) {
        if (contents.getCourseLearningOutcomes() != null) {
            final var outcomes = contents.getCourseLearningOutcomes().stream()
                    .map(row -> row.values().stream().collect(Collectors.joining(" — ")))
                    .collect(Collectors.joining("\n"));
            syllabus.setLearningOutcomes(outcomes);
        }

        if (contents.getAcademicSkills() != null) {
            final var skills = contents.getAcademicSkills().stream()
                    .map(row -> row.values().stream().collect(Collectors.joining(" — ")))
                    .collect(Collectors.joining("\n"));
            syllabus.setAssessmentCriteria(skills);
        }

        if (contents.getWeeklyCoursePlan() != null) {
            syncWeeklyPlans(syllabus, contents.getWeeklyCoursePlan());
        }
    }

    private void syncWeeklyPlans(Syllabus syllabus, List<Map<String, String>> weeklyPlanRows) {
        syllabus.getWeeklyPlans().clear();

        var weekNumber = 1;
        for (final var row : weeklyPlanRows) {
            final var topic = row.getOrDefault("Topic", row.getOrDefault("theme", ""));
            if (topic.isBlank()) continue;

            final var plan = WeeklyPlan.builder()
                    .weekNumber(weekNumber++)
                    .topic(topic)
                    .lectureContent(row.getOrDefault("Lecture", row.getOrDefault("lecture", "")))
                    .practiceContent(row.getOrDefault("Practice", row.getOrDefault("practice", "")))
                    .assignments(row.getOrDefault("Assignment", row.getOrDefault("SRO", "")))
                    .build();
            syllabus.addWeeklyPlan(plan);
        }
    }

    private Semester toSemester(Integer term) {
        if (term == null) return Semester.FALL;
        return term % 2 == 1 ? Semester.FALL : Semester.SPRING;
    }
}