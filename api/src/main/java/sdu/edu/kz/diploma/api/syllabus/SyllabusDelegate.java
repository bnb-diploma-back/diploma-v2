package sdu.edu.kz.diploma.api.syllabus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.syllabus.create.CreateSyllabusApi;
import sdu.edu.kz.diploma.api.syllabus.create.CreateSyllabusRequest;
import sdu.edu.kz.diploma.api.syllabus.delete.DeleteSyllabusApi;
import sdu.edu.kz.diploma.api.syllabus.get.GetSyllabusApi;
import sdu.edu.kz.diploma.api.syllabus.get.GetSyllabusResponse;
import sdu.edu.kz.diploma.api.syllabus.update.UpdateSyllabusApi;
import sdu.edu.kz.diploma.api.syllabus.update.UpdateSyllabusRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SyllabusDelegate {

    private final GetSyllabusApi getSyllabusApi;
    private final CreateSyllabusApi createSyllabusApi;
    private final UpdateSyllabusApi updateSyllabusApi;
    private final DeleteSyllabusApi deleteSyllabusApi;

    public List<GetSyllabusResponse> findAll() {
        return getSyllabusApi.findAll();
    }

    public GetSyllabusResponse findById(Long id) {
        return getSyllabusApi.findById(id);
    }

    public GetSyllabusResponse findByCourseCode(String courseCode) {
        return getSyllabusApi.findByCourseCode(courseCode);
    }

    public Long create(CreateSyllabusRequest request) {
        return createSyllabusApi.create(request);
    }

    public void update(Long id, UpdateSyllabusRequest request) {
        updateSyllabusApi.update(id, request);
    }

    public void delete(Long id) {
        deleteSyllabusApi.delete(id);
    }
}