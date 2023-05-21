package net.okocraft.lunachat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class UUIDCacheFile {

    private static final Pattern SPLITTER = Pattern.compile(": ", Pattern.LITERAL);

    public static Map<String, String> load(Path filepath) throws IOException {
        var result = new HashMap<String, String>();
        if (Files.isRegularFile(filepath)) {
            try (var lines = Files.lines(filepath)) {
                lines.forEach(line -> processLine(line, result));
            }
        }
        return result;
    }

    public static synchronized void save(Path filepath, Map<String, String> data) throws IOException {
        Files.createDirectories(filepath.getParent());

        try (var writer = Files.newBufferedWriter(filepath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (var entry : data.entrySet()) {
                writer.write(entry.getKey());
                writer.write(": ");
                writer.write(entry.getValue());
                writer.newLine();
            }
        }
    }

    private static void processLine(String line, Map<String, String> output) {
        var elements = SPLITTER.split(line);

        if (elements.length != 2) {
            return;
        }

        output.put(elements[0], elements[1]);
    }
}
