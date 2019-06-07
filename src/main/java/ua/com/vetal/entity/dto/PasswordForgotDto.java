package ua.com.vetal.entity.dto;


import javax.validation.constraints.NotNull;

public class PasswordForgotDto {
    @NotNull
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
