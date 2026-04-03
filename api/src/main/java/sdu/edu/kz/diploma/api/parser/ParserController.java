package sdu.edu.kz.diploma.api.parser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdu.edu.kz.diploma.api.parser.sync.SyncResult;

@RestController
@RequestMapping("/api/v1/parser")
@RequiredArgsConstructor
@Tag(name = "Parser", description = "Sync parsed course data from MongoDB into PostgreSQL")
public class ParserController {

    private final ParserDelegate parserDelegate;

    @PostMapping("/sync")
    @Operation(
            summary = "Sync MongoDB data to PostgreSQL",
            description = "Reads courses, curriculum courses, and syllabuses from MongoDB and upserts them into the syllabi and weekly_plans tables"
    )
    public ResponseEntity<SyncResult> sync() {
        return ResponseEntity.ok(parserDelegate.sync());
    }
}