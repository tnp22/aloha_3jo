package com.aloha.movie_project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Option {
    public Option() {
        this.orderCode = 0;
    }
    private int orderCode;
}
