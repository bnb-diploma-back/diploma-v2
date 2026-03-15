package sdu.edu.kz.diploma.api.syllabus.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSyllabusApi {

    private final CreateSyllabusRepository repository;

    @Transactional
    public Long create(CreateSyllabusRequest request) {
        return repository.insertSyllabus(request);
    }
}