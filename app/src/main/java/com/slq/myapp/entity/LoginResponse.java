package com.slq.myapp.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LoginResponse {
    private String msg;
    private int code;
    private int expire;
    private String token;
}
