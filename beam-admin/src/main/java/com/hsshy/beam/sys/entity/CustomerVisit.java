package com.hsshy.beam.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.common.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
@TableName("customer_visit")
public class CustomerVisit  extends RestEntity<Long> {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    private Long userId;
    private Long customerId;
    private String visitSchedule;
    private int visitType;
    private String visitInfo;
    private String resultInfo;
    private int amount;
    private Date creatTime;

    @TableField(exist = false)
    private String  userName;
    @TableField(exist = false)
    private String customerStoreName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
