package sdu.edu.kz.diploma.api.parser.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "electivegroups")
@Getter
@Setter
public class MongoElectiveGroup {

    @Id
    private String id;

    @Field("course_name")
    private String courseName;
    private List<String> codes;
}