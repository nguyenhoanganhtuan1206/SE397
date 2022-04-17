package com.se397.ultil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrypPasswordUtils {

    public static String EncrypPasswordUtils(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(password);
    }
}
