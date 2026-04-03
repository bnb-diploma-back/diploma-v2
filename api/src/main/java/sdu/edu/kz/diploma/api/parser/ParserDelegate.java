package sdu.edu.kz.diploma.api.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.parser.sync.SyncParserApi;
import sdu.edu.kz.diploma.api.parser.sync.SyncResult;

@Component
@RequiredArgsConstructor
public class ParserDelegate {

    private final SyncParserApi syncParserApi;

    public SyncResult sync() {
        return syncParserApi.sync();
    }
}