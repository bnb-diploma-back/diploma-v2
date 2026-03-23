package sdu.edu.kz.diploma.api.dictionary.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryApi;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteDictionaryApiTest extends BaseTest {

    @Autowired
    private DeleteDictionaryApi deleteDictionaryApi;

    @Autowired
    private GetDictionaryApi getDictionaryApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void deleteDepartment_removesIt() {
        final var department = creator.department();

        deleteDictionaryApi.deleteDepartment(department.getId());

        assertThatThrownBy(() -> getDictionaryApi.findDepartmentById(department.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteDepartment_cascadesDeleteMajors() {
        final var department = creator.department();
        final var major = creator.major(department);

        deleteDictionaryApi.deleteDepartment(department.getId());

        assertThatThrownBy(() -> getDictionaryApi.findMajorById(major.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteDepartment_throwsException_whenNotFound() {
        assertThatThrownBy(() -> deleteDictionaryApi.deleteDepartment(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteMajor_removesIt() {
        final var department = creator.department();
        final var major = creator.major(department);

        deleteDictionaryApi.deleteMajor(major.getId());

        assertThatThrownBy(() -> getDictionaryApi.findMajorById(major.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void deleteMajor_doesNotAffectDepartment() {
        final var department = creator.department();
        creator.major(department, "SE");
        final var major2 = creator.major(department, "DS");

        deleteDictionaryApi.deleteMajor(major2.getId());

        final var dept = getDictionaryApi.findDepartmentById(department.getId());
        assertThat(dept.getMajors()).hasSize(1);
    }

    @Test
    void deleteMajor_throwsException_whenNotFound() {
        assertThatThrownBy(() -> deleteDictionaryApi.deleteMajor(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}