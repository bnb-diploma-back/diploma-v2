package sdu.edu.kz.diploma.api.dictionary.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryApi;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateDictionaryApiTest extends BaseTest {

    @Autowired
    private UpdateDictionaryApi updateDictionaryApi;

    @Autowired
    private GetDictionaryApi getDictionaryApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void updateDepartment_changesFields() {
        final var department = creator.department("Old Name");

        final var request = UpdateDepartmentRequest.builder()
                .code("NEW_CODE")
                .name("New Name")
                .description("New description")
                .build();

        updateDictionaryApi.updateDepartment(department.getId(), request);

        final var result = getDictionaryApi.findDepartmentById(department.getId());
        assertThat(result.getCode()).isEqualTo("NEW_CODE");
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDescription()).isEqualTo("New description");
    }

    @Test
    void updateDepartment_throwsException_whenNotFound() {
        final var request = UpdateDepartmentRequest.builder()
                .code("X")
                .name("X")
                .build();

        assertThatThrownBy(() -> updateDictionaryApi.updateDepartment(999999L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void updateMajor_changesFields() {
        final var department = creator.department();
        final var major = creator.major(department, "Old Major");

        final var request = UpdateMajorRequest.builder()
                .code("NEW_CODE")
                .name("New Major")
                .description("Updated")
                .departmentId(department.getId())
                .build();

        updateDictionaryApi.updateMajor(major.getId(), request);

        final var result = getDictionaryApi.findMajorById(major.getId());
        assertThat(result.getCode()).isEqualTo("NEW_CODE");
        assertThat(result.getName()).isEqualTo("New Major");
        assertThat(result.getDescription()).isEqualTo("Updated");
    }

    @Test
    void updateMajor_reassignsDepartment() {
        final var dept1 = creator.department("Dept 1");
        final var dept2 = creator.department("Dept 2");
        final var major = creator.major(dept1, "SE");

        final var request = UpdateMajorRequest.builder()
                .code(major.getCode())
                .name(major.getName())
                .departmentId(dept2.getId())
                .build();

        updateDictionaryApi.updateMajor(major.getId(), request);

        final var result = getDictionaryApi.findMajorById(major.getId());
        assertThat(result.getDepartmentId()).isEqualTo(dept2.getId());
        assertThat(result.getDepartmentName()).isEqualTo("Dept 2");
    }

    @Test
    void updateMajor_throwsException_whenMajorNotFound() {
        final var department = creator.department();
        final var request = UpdateMajorRequest.builder()
                .code("X")
                .name("X")
                .departmentId(department.getId())
                .build();

        assertThatThrownBy(() -> updateDictionaryApi.updateMajor(999999L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void updateMajor_throwsException_whenDepartmentNotFound() {
        final var department = creator.department();
        final var major = creator.major(department);

        final var request = UpdateMajorRequest.builder()
                .code(major.getCode())
                .name(major.getName())
                .departmentId(999999L)
                .build();

        assertThatThrownBy(() -> updateDictionaryApi.updateMajor(major.getId(), request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}