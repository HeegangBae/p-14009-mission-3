package com.back.domain.wiseSaying.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WiseSaying {
    private int id;
    private String author;
    private String content;
}
