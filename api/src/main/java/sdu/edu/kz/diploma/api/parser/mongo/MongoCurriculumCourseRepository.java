package sdu.edu.kz.diploma.api.parser.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCurriculumCourseRepository extends MongoRepository<MongoCurriculumCourse, String> {
}