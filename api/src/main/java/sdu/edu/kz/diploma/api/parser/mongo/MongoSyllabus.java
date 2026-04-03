package sdu.edu.kz.diploma.api.parser.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "syllabuses")
@Getter
@Setter
public class MongoSyllabus {

    @Id
    private String id;
    private String courseCode;
    private Integer term;
    private Map<String, String> basic;
    private Description description;
    private Contents contents;

    @Getter
    @Setter
    public static class Description {
        private String courseDescription;
        private List<Instructor> instructors;
    }

    @Getter
    @Setter
    public static class Instructor {
        private String name;
        private String email;
    }

    @Getter
    @Setter
    public static class Contents {
        private List<Map<String, String>> AcademicSkills;
        private List<Map<String, String>> SubjectSpecificSkills;
        private List<Map<String, String>> WeeklyCoursePlan;
        private List<Map<String, String>> CourseLearningOutcomes;
    }
}