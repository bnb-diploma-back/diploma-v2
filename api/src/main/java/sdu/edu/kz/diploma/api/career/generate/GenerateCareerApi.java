package sdu.edu.kz.diploma.api.career.generate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.model.repository.StudentCareerRepository;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Service
@RequiredArgsConstructor
public class GenerateCareerApi {

    private final DSLContext dsl;
    private final CareerAiService careerAiService;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public GenerateCareerResponse generate(Long studentId) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }

        final var studentSyllabi = dsl.selectFrom(STUDENT_SYLLABI)
                .where(STUDENT_SYLLABI.STUDENT_ID.eq(studentId))
                .fetchInto(StudentSyllabi.class);

        final var courses = studentSyllabi.stream()
                .map(ss -> {
                    final var syllabus = dsl.selectFrom(SYLLABI)
                            .where(SYLLABI.ID.eq(ss.syllabusId()))
                            .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

                    return CareerAiInput.CourseInfo.builder()
                            .courseCode(syllabus.courseCode())
                            .courseTitle(syllabus.title())
                            .department(syllabus.department())
                            .credits(syllabus.credits())
                            .expectedGrade(ss.expectedGrade())
                            .description(syllabus.description())
                            .learningOutcomes(syllabus.learningOutcomes())
                            .build();
                })
                .toList();

        final var aiInput = CareerAiInput.builder()
                .studentName(student.firstName() + " " + student.lastName())
                .department(student.department())
                .major(student.major())
                .enrollmentYear(student.enrollmentYear())
                .currentSemester(student.currentSemester())
                .courses(courses)
                .build();

        final var aiResponseJson = careerAiService.generateCareerCards(aiInput);
        final var response = parseResponse(aiResponseJson);

        saveCareerCards(studentId, response.getCareerCards());

        response.setStudentId(studentId);
        response.setStudentName(student.firstName() + " " + student.lastName());
        response.setMajor(student.major());
        response.setGeneratedAt(LocalDateTime.now());
        return response;
    }

    private void saveCareerCards(Long studentId, List<GenerateCareerResponse.CareerCardResponse> cards) {
        final var studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        studentEntity.getStudentCareers().clear();
        studentRepository.save(studentEntity);

        cards.forEach(card -> {
            final var career = StudentCareer.builder()
                    .profession(card.getProfession())
                    .description(card.getDescription())
                    .requiredSkills(card.getRequiredSkills())
                    .build();
            studentEntity.addStudentCareer(career);
        });

        studentRepository.save(studentEntity);
    }

    private GenerateCareerResponse parseResponse(String aiResponseJson) {
        try {
            return objectMapper.readValue(aiResponseJson, GenerateCareerResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI career response", e);
        }
    }
}