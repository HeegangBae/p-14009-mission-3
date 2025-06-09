package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class WiseSayingRepository {
    private final Path dbPath = Path.of("db/wiseSaying");
    private final Path lastIdFilePath = dbPath.resolve("lastId.txt");
    private final Path dataJsonPath = dbPath.resolve("data.json");

    public WiseSaying save(String content, String author) {
        int lastId = getLastId() + 1;
        WiseSaying ws = new WiseSaying(lastId, author, content);
        save(ws);
        saveLastId(lastId);
        return ws;
    }

    public void save(WiseSaying ws) {
        try {
            Files.createDirectories(dbPath);
            Path filePath = dbPath.resolve(ws.getId() + ".json");
            try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
                bw.write("{\n");
                bw.write("  \"id\": " + ws.getId() + ",\n");
                bw.write("  \"author\": \"" + ws.getAuthor() + "\",\n");
                bw.write("  \"content\": \"" + ws.getContent() + "\"\n");
                bw.write("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WiseSaying> findAll() {
        if (!Files.exists(dbPath)) return new ArrayList<>();
        try {
            return Files.list(dbPath)
                    .filter(p -> p.toString().endsWith(".json") && !p.endsWith("data.json"))
                    .map(this::loadFromJsonFile)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(WiseSaying::getId).reversed())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private WiseSaying loadFromJsonFile(Path path) {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            int id = -1;
            String author = "";
            String content = "";

            for (String line; (line = br.readLine()) != null; ) {
                line = line.trim();
                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(line.split(":")[1].trim().replace(",", ""));
                } else if (line.startsWith("\"author\"")) {
                    author = line.split(":")[1].trim().replaceAll("^\"|\",$", "");
                } else if (line.startsWith("\"content\"")) {
                    content = line.split(":")[1].trim().replaceAll("^\"|\"$", "");
                }
            }

            return new WiseSaying(id, author, content);
        } catch (IOException e) {
            return null;
        }
    }

    public WiseSaying findById(int id) {
        Path path = dbPath.resolve(id + ".json");
        if (!Files.exists(path)) return null;
        return loadFromJsonFile(path);
    }

    public boolean delete(int id) {
        Path path = dbPath.resolve(id + ".json");
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    public void buildDataJson() {
        List<WiseSaying> all = findAll();
        try (BufferedWriter bw = Files.newBufferedWriter(dataJsonPath)) {
            bw.write("[\n");
            for (int i = 0; i < all.size(); i++) {
                WiseSaying ws = all.get(i);
                bw.write("  {\n");
                bw.write("    \"id\": " + ws.getId() + ",\n");
                bw.write("    \"author\": \"" + ws.getAuthor() + "\",\n");
                bw.write("    \"content\": \"" + ws.getContent() + "\"\n");
                bw.write("  }" + (i < all.size() - 1 ? ",\n" : "\n"));
            }
            bw.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getLastId() {
        try {
            if (!Files.exists(lastIdFilePath)) return 0;
            return Integer.parseInt(Files.readString(lastIdFilePath));
        } catch (IOException e) {
            return 0;
        }
    }

    private void saveLastId(int id) {
        try {
            Files.createDirectories(dbPath);
            Files.writeString(lastIdFilePath, String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
