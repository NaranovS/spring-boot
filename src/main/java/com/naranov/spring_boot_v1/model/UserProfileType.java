package com.naranov.spring_boot_v1.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable{
    USER("user"),
    DBA("dba"),
    ADMIN("admin");

    String userProfileType;

    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType(){
        return userProfileType;
    }

}