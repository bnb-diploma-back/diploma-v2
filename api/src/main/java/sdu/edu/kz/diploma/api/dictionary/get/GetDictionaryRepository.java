package sdu.edu.kz.diploma.api.dictionary.get;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class GetDictionaryRepository {

    private final DSLContext dsl;

    public List<GetDictionaryResponse.DepartmentResponse> findAllDepartments() {
        final var departments = dsl.selectFrom(DEPARTMENTS)
                .orderBy(DEPARTMENTS.NAME)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Departments.class);

        return departments.stream()
                .map(d -> GetDictionaryResponse.DepartmentResponse.builder()
                        .id(d.id())
                        .code(d.code())
                        .name(d.name())
                        .description(d.description())
                        .majors(fetchMajors(d.id()))
                        .createdAt(d.createdAt())
                        .updatedAt(d.updatedAt())
                        .build())
                .toList();
    }

    public Optional<GetDictionaryResponse.DepartmentResponse> findDepartmentById(Long id) {
        final var department = dsl.selectFrom(DEPARTMENTS)
                .where(DEPARTMENTS.ID.eq(id))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Departments.class);

        if (department == null) {
            return Optional.empty();
        }

        return Optional.of(GetDictionaryResponse.DepartmentResponse.builder()
                .id(department.id())
                .code(department.code())
                .name(department.name())
                .description(department.description())
                .majors(fetchMajors(department.id()))
                .createdAt(department.createdAt())
                .updatedAt(department.updatedAt())
                .build());
    }

    public List<GetDictionaryResponse.MajorResponse> findAllMajors() {
        final var majors = dsl.selectFrom(MAJORS)
                .orderBy(MAJORS.NAME)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);

        return majors.stream()
                .map(this::toMajorResponse)
                .toList();
    }

    public Optional<GetDictionaryResponse.MajorResponse> findMajorById(Long id) {
        final var major = dsl.selectFrom(MAJORS)
                .where(MAJORS.ID.eq(id))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);

        if (major == null) {
            return Optional.empty();
        }

        return Optional.of(toMajorResponse(major));
    }

    public List<GetDictionaryResponse.MajorResponse> findMajorsByDepartmentId(Long departmentId) {
        final var majors = dsl.selectFrom(MAJORS)
                .where(MAJORS.DEPARTMENT_ID.eq(departmentId))
                .orderBy(MAJORS.NAME)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);

        return majors.stream()
                .map(this::toMajorResponse)
                .toList();
    }

    private List<GetDictionaryResponse.MajorResponse> fetchMajors(Long departmentId) {
        final var majors = dsl.selectFrom(MAJORS)
                .where(MAJORS.DEPARTMENT_ID.eq(departmentId))
                .orderBy(MAJORS.NAME)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);

        return majors.stream()
                .map(this::toMajorResponse)
                .toList();
    }

    private GetDictionaryResponse.MajorResponse toMajorResponse(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors m) {
        final var department = dsl.selectFrom(DEPARTMENTS)
                .where(DEPARTMENTS.ID.eq(m.departmentId()))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Departments.class);

        return GetDictionaryResponse.MajorResponse.builder()
                .id(m.id())
                .code(m.code())
                .name(m.name())
                .description(m.description())
                .departmentId(m.departmentId())
                .departmentName(department != null ? department.name() : null)
                .createdAt(m.createdAt())
                .updatedAt(m.updatedAt())
                .build();
    }
}