package com.project.example.model.dto.userInterface;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.project.example.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private Long interfaceInfoId;

    /**
     *
     */
    private Integer totalNum;

    /**
     *
     */
    private Integer leftNum;

    /**
     *
     */
    private Integer status;



}