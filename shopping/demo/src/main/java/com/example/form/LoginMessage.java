package com.example.form;

import lombok.Data;

@Data
public class LoginMessage {
    private String username;
    private String password;
    private String key;
}
