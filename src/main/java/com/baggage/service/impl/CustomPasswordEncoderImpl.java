package com.baggage.service.impl;

import com.baggage.service.CustomPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("customPasswordEncoder")
public class CustomPasswordEncoderImpl implements CustomPasswordEncoder {
    private String salt = "SECRET";

    public CustomPasswordEncoderImpl() {
    }

    @Override
    public String encode(String password) {
        return password + salt;
    }
}
