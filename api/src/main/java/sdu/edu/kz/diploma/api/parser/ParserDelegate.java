package sdu.edu.kz.diploma.api.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.parser.mongo.MongoCourseRepository;
import sdu.edu.kz.diploma.api.parser.sync.SyncParserApi;
import sdu.edu.kz.diploma.api.parser.sync.SyncResult;

@Component
@RequiredArgsConstructor
public class ParserDelegate {

    private final SyncParserApi syncParserApi;
    private final MongoCourseRepository mongoCourseRepository;

    public SyncResult sync() {
        return syncParserApi.sync();
    }

    public long countCourses() {
        return mongoCourseRepository.count();
    }
}