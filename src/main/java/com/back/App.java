package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    private final Scanner scanner = new Scanner(System.in);
    private final WiseSayingController wiseSayingController = new WiseSayingController(scanner);
    private final SystemController systemController = new SystemController();

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                break;
            } else if (cmd.equals("등록")) {
                wiseSayingController.write();
            } else if (cmd.equals("목록")) {
                wiseSayingController.list();
            } else if (cmd.startsWith("삭제")) {
                wiseSayingController.delete(cmd);
            } else if (cmd.startsWith("수정")) {
                wiseSayingController.modify(cmd);
            } else if (cmd.equals("빌드")) {
                wiseSayingController.build();
            } else {
                systemController.unknownCommand();
            }
        }
        scanner.close();
    }
}