package com.se397.dto;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
    @NotEmpty(message = "User name cannot be empty")
    @Size(min = 3 , message = "Length user name must be largest than 3")
    private String fullName;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 3 , max = 30 , message = "Length password must be between 3 and 30")
    private String password;

    @NotEmpty(message = "Password cannot matches")
    private String confirmPassword;

    @Pattern(regexp = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "Email is invalid")
    @NotEmpty(message = "Email cannot be empty")
    @Length(min = 3 , max = 50 , message = "Length email must be between 3 and 50")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Phone Number is invalid")
    @Length(min = 3 , max = 50 , message = "Length phone number must be between 3 and 50")
    private String phoneNumber;

    private int province;

    public boolean checkConfirmPassword(String password , String confirmPassword) {
        if(!password.equals(confirmPassword))  {
            return false;
        }
        return true;
    }

    public UserDTO(){}

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
