package com.komatsu.pt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Connection {
    private String locStart;
    private String locEnd;
    private int time;
}
