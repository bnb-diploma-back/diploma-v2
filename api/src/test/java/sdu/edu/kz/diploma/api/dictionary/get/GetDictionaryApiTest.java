package sdu.edu.kz.diploma.api.dictionary.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetDictionaryApiTest extends BaseTest {

    @Autowired
    private GetDictionaryApi getDictionaryApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findAllDepartments_returnsEmptyList_whenNone() {
        final var result = getDictionaryApi.findAllDepartments();

        assertThat(result).isEmpty();
    }

    @Test
    void findAllDepartments_returnsAll() {
        creator.department("Computer Science");
        creator.department("Mathematics");

        final var result = getDictionaryApi.findAllDepartments();

        assertThat(result).hasSize(2);
    }

    @Test
    void findDepartmentById_returnsDepartmentWithMajors() {
        final var department = creator.department("Computer Science");
        creator.major(department, "Software Engineering");
        creator.major(department, "Data Science");

        final var result = getDictionaryApi.findDepartmentById(department.getId());

        assertThat(result.getName()).isEqualTo("Computer Science");
        assertThat(result.getMajors()).hasSize(2);
    }

    @Test
    void findDepartmentById_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getDictionaryApi.findDepartmentById(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findAllMajors_returnsAll() {
        final var dept = creator.department();
        creator.major(dept, "SE");
        creator.major(dept, "DS");

        final var result = getDictionaryApi.findAllMajors();

        assertThat(result).hasSize(2);
    }

    @Test
    void findMajorById_returnsMajorWithDepartmentName() {
        final var department = creator.department("Computer Science");
        final var major = creator.major(department, "Software Engineering");

        final var result = getDictionaryApi.findMajorById(major.getId());

        assertThat(result.getName()).isEqualTo("Software Engineering");
        assertThat(result.getDepartmentId()).isEqualTo(department.getId());
        assertThat(result.getDepartmentName()).isEqualTo("Computer Science");
    }

    @Test
    void findMajorById_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getDictionaryApi.findMajorById(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findMajorsByDepartmentId_returnsOnlyMatchingMajors() {
        final var dept1 = creator.department();
        final var dept2 = creator.department();
        creator.major(dept1, "SE");
        creator.major(dept1, "DS");
        creator.major(dept2, "Pure Math");

        final var result = getDictionaryApi.findMajorsByDepartmentId(dept1.getId());

        assertThat(result).hasSize(2);
    }
}