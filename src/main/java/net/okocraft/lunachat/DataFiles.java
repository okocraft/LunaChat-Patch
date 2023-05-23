package net.okocraft.lunachat;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class DataFiles {

    private static final Pattern SPLITTER = Pattern.compile(": ", Pattern.LITERAL);

    public static BiMap<String, String> loadUUIDNameCache(Path filepath) throws IOException {
        var result = HashBiMap.<String, String>create();
        if (Files.isRegularFile(filepath)) {
            try (var lines = Files.lines(filepath)) {
                lines.forEach(line -> {
                    var elements = SPLITTER.split(line);

                    if (elements.length != 2) {
                        return;
                    }

                    result.forcePut(elements[0], elements[1]);
                });
            }
        }
        return result;
    }

    public static synchronized void saveStringMap(Path filepath, Map<String, String> data) throws IOException {
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

    public static synchronized void saveHideList(Path filepath, Map<String, List<String>> data) throws IOException {
        Files.createDirectories(filepath.getParent());

        try (var writer = Files.newBufferedWriter(filepath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (var entry : data.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    return;
                }

                writer.write(entry.getKey());
                writer.write(": ");
                writer.newLine();

                for (var who : entry.getValue()) {
                    writer.write("- ");
                    writer.write(who);
                    writer.newLine();
                }
            }
        }
    }
}
