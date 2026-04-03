package sdu.edu.kz.diploma.api.parser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdu.edu.kz.diploma.api.parser.sync.SyncResult;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/parser")
@RequiredArgsConstructor
@Tag(name = "Parser", description = "Sync parsed course data from MongoDB into PostgreSQL")
public class ParserController {

    private final ParserDelegate parserDelegate;
    private final MongoTemplate mongoTemplate;

    @PostMapping("/sync")
    @Operation(
            summary = "Sync MongoDB data to PostgreSQL",
            description = "Reads courses, curriculum courses, and syllabuses from MongoDB and upserts them into the syllabi and weekly_plans tables"
    )
    public ResponseEntity<SyncResult> sync() {
        return ResponseEntity.ok(parserDelegate.sync());
    }

    @GetMapping("/debug")
    @Operation(summary = "Debug MongoDB connection")
    public ResponseEntity<Map<String, Object>> debug() {
        final var db = mongoTemplate.getDb();
        final var collections = db.listCollectionNames().into(new java.util.ArrayList<>());
        final var coursesViaTemplate = mongoTemplate.findAll(
                sdu.edu.kz.diploma.api.parser.mongo.MongoCourse.class);
        final var coursesViaRepo = parserDelegate.countCourses();
        return ResponseEntity.ok(Map.of(
                "database", db.getName(),
                "collections", collections,
                "coursesViaTemplate", coursesViaTemplate.size(),
                "coursesViaRepo", coursesViaRepo
        ));
    }
}