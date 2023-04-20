package com.project.example.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeleteInterfaceRequest {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
}
