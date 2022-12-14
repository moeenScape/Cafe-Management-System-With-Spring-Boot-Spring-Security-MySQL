package com.moeen.Newcafe.wrapper;

import lombok.Data;

@Data
public class UserWrapper {
    private Integer id;
    private String name;
    private String contractNumber;
    private String email;
    private String password;
    private String status;
    public UserWrapper()
    {

    }

    public UserWrapper(Integer id, String name, String contractNumber, String email, String password, String status) {
        this.id = id;
        this.name = name;
        this.contractNumber = contractNumber;
        this.email = email;
        this.password = password;
        this.status = status;
    }
}
