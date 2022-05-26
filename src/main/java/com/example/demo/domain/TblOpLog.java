package com.example.demo.domain;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * business operation record
 * </p>
 *
 * @author hujun
 * @since 2021-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@TableName("tbl_op_log")(继承Model，开启AR。可以不指定表名)
public class TblOpLog extends Model<TblOpLog> {

    //private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * AppKey
     */
    @TableField("AppName")
    private String appName;

    /**
     * operation type[1:add,2:update,3:delete]
     */
    @TableField("ActionType")
    private Integer actionType;

    /**
     * bussiness object id
     */
    @TableField("BusinessId")
    private Long businessId;

    /**
     * log type id
     */
    @TableField("LogModuleType")
    private Integer logModuleType;

    /**
     * bussiness module name
     */
    @TableField("LogModuleName")
    private String logModuleName;

    /**
     * entity class name
     */
    @TableField("EntityClass")
    private String entityClass;

    /**
     * summary
     */
    @TableField("Summary")
    private String summary;

    /**
     * operation context
     */
    @TableField("Context")
    private String context;

    /**
     * operator id
     */
    @TableField("OperatorId")
    private Long operatorId;

    /**
     * operator name
     */
    @TableField("OperatorName")
    private String operatorName;

    /**
     * operation time
     */
    @TableField("OperateTime")
    private Date operateTime;

    /**
     * operator client ip
     */
    @TableField("ClientIp")
    private String clientIp;

    /**
     * operator proxy info
     */
    @TableField("Ua")
    private String ua;

    /**
     * remark
     */
    @TableField("Remark")
    private String remark;

    /**
     * delete status[0-false,1-true]
     */
    @TableField("Deleted")
    private Integer deleted;

    /**
     * create time
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * last update time
     */
    @TableField("UpdateTime")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
