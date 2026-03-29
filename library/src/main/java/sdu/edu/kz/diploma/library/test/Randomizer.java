package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.library.model.enums.Semester;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Randomizer {

    public String str(int length) {
        final var uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, Math.min(length, uuid.length()));
    }

    public String name() {
        return "Name_" + str(8);
    }

    public String text() {
        return "Text_" + str(16);
    }

    public String code() {
        return "CODE_" + str(6).toUpperCase();
    }

    public String email() {
        return str(8) + "@test.com";
    }

    public int intBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public long id() {
        return ThreadLocalRandom.current().nextLong(1, 100_000);
    }

    public LocalDate date() {
        final var daysOffset = ThreadLocalRandom.current().nextInt(-365, 365);
        return LocalDate.now().plusDays(daysOffset);
    }

    public LocalDate futureDate() {
        final var daysOffset = ThreadLocalRandom.current().nextInt(1, 365);
        return LocalDate.now().plusDays(daysOffset);
    }

    public Semester semester() {
        final var values = Semester.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    @SafeVarargs
    public final <T> T oneOf(T... values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public boolean bool() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}