package sdu.edu.kz.diploma.api.syllabus.delete;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static sdu.edu.kz.diploma.library.jooq.Tables.SYLLABI;
import static sdu.edu.kz.diploma.library.jooq.Tables.WEEKLY_PLANS;

@Repository
@RequiredArgsConstructor
public class DeleteSyllabusRepository {

    private final DSLContext dsl;

    public boolean existsById(Long id) {
        return dsl.fetchExists(
                dsl.selectOne().from(SYLLABI).where(SYLLABI.ID.eq(id))
        );
    }

    public void deleteById(Long id) {
        dsl.deleteFrom(WEEKLY_PLANS)
                .where(WEEKLY_PLANS.SYLLABUS_ID.eq(id))
                .execute();

        dsl.deleteFrom(SYLLABI)
                .where(SYLLABI.ID.eq(id))
                .execute();
    }
}