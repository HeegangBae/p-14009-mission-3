package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository = new WiseSayingRepository();

    public WiseSaying write(String content, String author) {
        return repository.save(content, author);
    }

    public List<WiseSaying> findAll() {
        return repository.findAll();
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    public WiseSaying findById(int id) {
        return repository.findById(id);
    }

    public void modify(WiseSaying ws, String content, String author) {
        ws.setContent(content);
        ws.setAuthor(author);
        repository.save(ws);
    }

    public void build() {
        repository.buildDataJson();
    }
}