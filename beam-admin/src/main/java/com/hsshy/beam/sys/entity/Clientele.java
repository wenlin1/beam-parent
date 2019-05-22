package com.hsshy.beam.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.common.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户管理表
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-07 18:03:20
 */
@NoArgsConstructor
@Data
@TableName("sys_clientele")
public class Clientele extends RestEntity<Long> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    // 日志名称
    @TableField(value = "customer_store_name")
    private String customerStoreName;
    // 用户ID
    @TableField(value = "customer_contact_name")
    private String customerContactName;
    // 是否成功
    @TableField(value = "customer_phone")
    private long customerPhone;
    // 消息
    @TableField(value = "create_person_id")
    private String createPersonId;
    // ip
    @TableField(value = "create_person_name")
    private String createPersonName;
    // 消息
    @TableField(value = "update_person_id")
    private String updatePersonId;
    // ip
    @TableField(value = "update_person_name")
    private String updatePersonName;
    // 消息
    @TableField(value = "create_time")
    private Date createTime;
    // ip
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "sales_account")
    private String salesAccount;

    @TableField(value = "sales_name")
    private String salesName;
    // 消息
    @TableField(value = "customer_type")
    private String customerType;
    // ip
    @TableField(value = "is_delete")
    private String isDelete;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
