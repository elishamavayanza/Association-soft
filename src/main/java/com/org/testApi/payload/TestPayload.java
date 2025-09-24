package com.org.testApi.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class TestPayload {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}