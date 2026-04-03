package sdu.edu.kz.diploma.api.parser.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoSyllabusRepository extends MongoRepository<MongoSyllabus, String> {

    Optional<MongoSyllabus> findByCourseCodeAndTerm(String courseCode, Integer term);
}