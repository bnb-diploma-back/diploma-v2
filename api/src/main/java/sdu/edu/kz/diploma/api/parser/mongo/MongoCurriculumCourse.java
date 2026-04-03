package sdu.edu.kz.diploma.api.parser.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "curriculumcourses")
@Getter
@Setter
public class MongoCurriculumCourse {

    @Id
    private String id;
    private String courseCode;
    private String courseName;
    private Integer credit;
    private Integer ects;
    private Integer term;
    private String grade;
    private String requisites;
    private String status;
    private String syllabusLink;
}