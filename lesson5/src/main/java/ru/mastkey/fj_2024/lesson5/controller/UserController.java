package ru.mastkey.fj_2024.lesson5.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mastkey.fj_2024.lesson5.controller.dto.ChangePasswordRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.UserAuthRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.UserRegistrationRequest;
import ru.mastkey.fj_2024.lesson5.entity.User;
import ru.mastkey.fj_2024.lesson5.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserAuthRequest request,
                                      @RequestParam(name = "rememberMe", required = false) Boolean rememberMe) {
        return ResponseEntity.ok(userService.authenticate(request, rememberMe));
    }

    @GetMapping("/user")
    public ResponseEntity<String> userData() {
        return ResponseEntity.ok("userData");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-pass")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
