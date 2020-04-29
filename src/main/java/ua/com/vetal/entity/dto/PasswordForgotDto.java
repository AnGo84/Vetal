package ua.com.vetal.entity.dto;


import javax.validation.constraints.NotEmpty;

public class PasswordForgotDto {
    @NotEmpty
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
