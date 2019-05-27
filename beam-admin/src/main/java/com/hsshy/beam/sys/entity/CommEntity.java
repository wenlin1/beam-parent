package com.hsshy.beam.sys.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommEntity {
    private long total;
    private long myTotal;
    private Long count;
    private String message;
    //未知率
    private double unknownRate;
    //拜访率
    private double viewRate;
    //意向率
    private double intentionRate;
    //签约率
    private double contractRate;

}
