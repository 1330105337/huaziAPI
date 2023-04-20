package com.project.example.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateInterfaceRequest {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

