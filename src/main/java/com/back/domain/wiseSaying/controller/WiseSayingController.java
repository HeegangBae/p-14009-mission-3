package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner scanner;
    private final WiseSayingService service;

    public WiseSayingController(Scanner scanner) {
        this.scanner = scanner;
        this.service = new WiseSayingService();
    }

    public void write() {
        System.out.print("명언: ");
        String content = scanner.nextLine().trim();
        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        WiseSaying wiseSaying = service.write(content, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", wiseSaying.getId());
    }

    public void list() {
        List<WiseSaying> list = service.findAll();
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying ws : list) {
            System.out.printf("%d / %s / %s\n", ws.getId(), ws.getAuthor(), ws.getContent());
        }
    }

    public void delete(String cmd) {
        int id = extractId(cmd);
        if (id == -1) return;

        boolean result = service.delete(id);
        if (result) {
            System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
        } else {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
        }
    }

    public void modify(String cmd) {
        int id = extractId(cmd);
        if (id == -1) return;

        WiseSaying ws = service.findById(id);
        if (ws == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존): %s\n", ws.getContent());
        System.out.print("명언: ");
        String content = scanner.nextLine().trim();

        System.out.printf("작가(기존): %s\n", ws.getAuthor());
        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        service.modify(ws, content, author);
    }

    public void build() {
        service.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    private int extractId(String cmd) {
        try {
            String[] bits = cmd.split("=", 2);
            return Integer.parseInt(bits[1]);
        } catch (Exception e) {
            System.out.println("id가 입력되지 않았습니다.");
            return -1;
        }
    }
}