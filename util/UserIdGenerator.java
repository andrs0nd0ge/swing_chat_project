package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class UserIdGenerator {
    final List<String> adjectives = new ArrayList<>();
    final List<String> nouns = new ArrayList<>();
    final int minInt;
    final int maxInt;
    final Random random;

    public UserIdGenerator(Path adjectivesPath, Path nounsPath, int minInt, int maxInt)
            throws IOException {
        this.minInt = minInt;
        this.maxInt = maxInt;
        try (Stream<String> lines = Files.lines(adjectivesPath)) {
            lines.forEach(line -> adjectives.add(capitalize(line)));
        }
        try (Stream<String> lines = Files.lines(nounsPath)) {
            lines.forEach(line -> nouns.add(capitalize(line)));
        }
        this.random = new Random();
    }

    private static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    public String generate() {
        StringBuilder sb = new StringBuilder()
                .append(adjectives.get(random.nextInt(adjectives.size())))
                .append(nouns.get(random.nextInt(nouns.size())));
        int i = random.nextInt(maxInt);
        if (i >= minInt) {
            sb.append(i - minInt);
        }
        return sb.toString();
    }
}