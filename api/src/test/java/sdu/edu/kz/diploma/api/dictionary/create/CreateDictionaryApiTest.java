package sdu.edu.kz.diploma.api.dictionary.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryApi;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateDictionaryApiTest extends BaseTest {

    @Autowired
    private CreateDictionaryApi createDictionaryApi;

    @Autowired
    private GetDictionaryApi getDictionaryApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void createDepartment_returnsId() {
        final var request = CreateDepartmentRequest.builder()
                .code(randomizer.code())
                .name("Computer Science")
                .description("CS Department")
                .build();

        final var id = createDictionaryApi.createDepartment(request);

        assertThat(id).isNotNull();
    }

    @Test
    void createDepartment_persistsCorrectly() {
        final var request = CreateDepartmentRequest.builder()
                .code("CS")
                .name("Computer Science")
                .description("CS Department")
                .build();

        final var id = createDictionaryApi.createDepartment(request);
        final var result = getDictionaryApi.findDepartmentById(id);

        assertThat(result.getCode()).isEqualTo("CS");
        assertThat(result.getName()).isEqualTo("Computer Science");
        assertThat(result.getDescription()).isEqualTo("CS Department");
    }

    @Test
    void createMajor_returnsId() {
        final var department = creator.department();
        final var request = CreateMajorRequest.builder()
                .code(randomizer.code())
                .name("Software Engineering")
                .departmentId(department.getId())
                .build();

        final var id = createDictionaryApi.createMajor(request);

        assertThat(id).isNotNull();
    }

    @Test
    void createMajor_persistsWithDepartment() {
        final var department = creator.department("Computer Science");
        final var request = CreateMajorRequest.builder()
                .code("SE")
                .name("Software Engineering")
                .description("SE Major")
                .departmentId(department.getId())
                .build();

        final var id = createDictionaryApi.createMajor(request);
        final var result = getDictionaryApi.findMajorById(id);

        assertThat(result.getCode()).isEqualTo("SE");
        assertThat(result.getName()).isEqualTo("Software Engineering");
        assertThat(result.getDepartmentId()).isEqualTo(department.getId());
        assertThat(result.getDepartmentName()).isEqualTo("Computer Science");
    }

    @Test
    void createMajor_throwsException_whenDepartmentNotFound() {
        final var request = CreateMajorRequest.builder()
                .code(randomizer.code())
                .name("Invalid Major")
                .departmentId(999999L)
                .build();

        assertThatThrownBy(() -> createDictionaryApi.createMajor(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}