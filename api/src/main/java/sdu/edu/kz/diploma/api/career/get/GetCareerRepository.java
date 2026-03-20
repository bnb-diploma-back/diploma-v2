package sdu.edu.kz.diploma.api.career.get;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentCareers;

import java.util.Optional;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class GetCareerRepository {

    private final DSLContext dsl;

    public Optional<GetCareerResponse> findByStudentId(Long studentId) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return Optional.empty();
        }

        final var careers = dsl.selectFrom(STUDENT_CAREERS)
                .where(STUDENT_CAREERS.STUDENT_ID.eq(studentId))
                .fetchInto(StudentCareers.class);

        final var careerCards = careers.stream()
                .map(c -> GetCareerResponse.CareerCardResponse.builder()
                        .id(c.id())
                        .profession(c.profession())
                        .description(c.description())
                        .requiredSkills(c.requiredSkills())
                        .build())
                .toList();

        return Optional.of(GetCareerResponse.builder()
                .studentId(student.id())
                .studentName(student.firstName() + " " + student.lastName())
                .careerCards(careerCards)
                .build());
    }
}