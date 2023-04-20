package com.project.example.model.dto.userInterface;



import lombok.Data;




@Data
public class AddUserInterfaceRequest {

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
