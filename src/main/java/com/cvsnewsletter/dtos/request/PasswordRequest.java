package com.cvsnewsletter.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {
    private String password;
    private String confirmationPassword;
    private String ohrId;
}
