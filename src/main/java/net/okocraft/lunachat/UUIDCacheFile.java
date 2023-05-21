package net.okocraft.lunachat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.regex.Pattern;

public final class UUIDCacheFile {

    private static final Pattern SPLITTER = Pattern.compile(": ", Pattern.LITERAL);

    public static BiMap<String, String> load(Path filepath) throws IOException {
        var result = HashBiMap.<String, String>create();
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

    private static void processLine(String line, BiMap<String, String> output) {
        var elements = SPLITTER.split(line);

        if (elements.length != 2) {
            return;
        }

        output.forcePut(elements[0], elements[1]);
    }
}
