package ru.mastkey.fj_2024.lesson5.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequest {
    private String username;
    private String newPassword;
    private String confirmPassword;
    private String verificationCode;
}
