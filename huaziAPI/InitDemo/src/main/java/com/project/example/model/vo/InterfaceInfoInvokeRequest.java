package com.project.example.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceInfoInvokeRequest {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 请求参数
     */
    private String requestParams;

}
