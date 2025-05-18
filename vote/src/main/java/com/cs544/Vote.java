package com.cs544;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vote {
    private String voterId;
    private String vote;
}
