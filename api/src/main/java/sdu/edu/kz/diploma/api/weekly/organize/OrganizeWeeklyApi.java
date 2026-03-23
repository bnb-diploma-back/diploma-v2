package sdu.edu.kz.diploma.api.weekly.organize;

import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyApi;
import sdu.edu.kz.diploma.library.model.entity.WeeklyOrganizer;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;
import sdu.edu.kz.diploma.library.model.repository.WeeklyOrganizerRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrganizeWeeklyApi {

    private final GetWeeklyApi getWeeklyApi;
    private final WeeklyAiService weeklyAiService;
    private final WeeklyOrganizerRepository weeklyOrganizerRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrganizeWeeklyResponse organize(Long studentId, Integer weekNumber) {
        final var weeklyData = getWeeklyApi.findByStudentAndWeek(studentId, weekNumber);

        final var aiResponseJson = weeklyAiService.generateOrganizer(weeklyData);

        final var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        final var existing = weeklyOrganizerRepository.findByStudentIdAndWeekNumber(studentId, weekNumber);
        if (existing.isPresent()) {
            final var organizer = existing.get();
            organizer.setAiResponse(aiResponseJson);
            weeklyOrganizerRepository.save(organizer);
        } else {
            final var organizer = WeeklyOrganizer.builder()
                    .student(student)
                    .weekNumber(weekNumber)
                    .aiResponse(aiResponseJson)
                    .build();
            weeklyOrganizerRepository.save(organizer);
        }

        return parseResponse(studentId, weekNumber, aiResponseJson);
    }

    @Transactional(readOnly = true)
    public OrganizeWeeklyResponse getOrganized(Long studentId, Integer weekNumber) {
        final var organizer = weeklyOrganizerRepository.findByStudentIdAndWeekNumber(studentId, weekNumber)
                .orElseThrow(() -> new RuntimeException(
                        "No organizer found for student " + studentId + " week " + weekNumber +
                                ". Call POST to generate one first."));

        return parseResponse(studentId, weekNumber, organizer.getAiResponse());
    }

    private OrganizeWeeklyResponse parseResponse(Long studentId, Integer weekNumber, String aiResponseJson) {
        try {
            final var response = objectMapper.readValue(aiResponseJson, OrganizeWeeklyResponse.class);
            response.setStudentId(studentId);
            response.setWeekNumber(weekNumber);
            response.setGeneratedAt(LocalDateTime.now());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}