package ru.mastkey.fj_2024.lesson5.controller.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import ru.mastkey.fj_2024.lesson5.controller.dto.ChangePasswordRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.UserAuthRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.UserRegistrationRequest;
import ru.mastkey.fj_2024.lesson5.entity.User;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends IntegrationTestBase {

    @Test
    void registerUserSuccessTest() {
        var request = new UserRegistrationRequest();
        request.setPassword("reg");
        request.setUsername("reg");

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/registration")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();

        assertThat(response.getUsername()).isEqualTo(request.getUsername());

        var savedUser = userRepository.findByUsername(request.getUsername()).get();
        var roles = savedUser.getRoles();
        assertThat(roles).hasSize(1);
        assertThat(roles.stream().filter(role -> role.getName().equals("USER")).count()).isEqualTo(1);
    }

    @Test
    void registerUserConflictTest() {
        var user = new User();
        user.setUsername("reg2");
        user.setPassword("reg2");
        userRepository.save(user);

        var request = new UserRegistrationRequest();
        request.setPassword("reg2");
        request.setUsername("reg2");

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/registration")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void authUserSuccessTest() {
        var regRequest = new UserRegistrationRequest();
        regRequest.setPassword("password");
        regRequest.setUsername("username");

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/registration")
                        .build())
                .bodyValue(regRequest)
                .exchange()
                .expectStatus().isOk();

        var authRequest = new UserAuthRequest();
        authRequest.setPassword("password");
        authRequest.setUsername("username");

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/auth")
                        .build())
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
    }

    @Test
    void authUserUnauthorizedTest() {
        var regRequest = new UserRegistrationRequest();
        regRequest.setPassword("password");
        regRequest.setUsername("username");

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/registration")
                        .build())
                .bodyValue(regRequest)
                .exchange()
                .expectStatus().isOk();

        var authRequest = new UserAuthRequest();
        authRequest.setPassword("123");
        authRequest.setUsername("username");

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/auth")
                        .build())
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.getMessage()).isEqualTo("Incorrect password");
    }

    @Test
    void authUserWithRememberMeSuccessTest() {
        var regRequest = new UserRegistrationRequest();
        regRequest.setPassword("password");
        regRequest.setUsername("username");

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/registration")
                        .build())
                .bodyValue(regRequest)
                .exchange()
                .expectStatus().isOk();

        var request = new UserAuthRequest();
        request.setPassword("password");
        request.setUsername("username");

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/auth")
                        .queryParam("rememberMe", true)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
    }

    @Test
    @WithMockUser
    void getUserDataWIthUserRoleSuccessTest() {
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/user")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(response).isEqualTo("userData");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserDataWIthAdminRoleSuccessTest() {
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/user")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(response).isEqualTo("userData");
    }

    @Test
    @WithAnonymousUser
    void getUserDataWIthAnonymousRoleUnauthorizedTest() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/user")
                        .build())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void logoutUserSuccessTest() {
        var regRequest = new UserRegistrationRequest();
        regRequest.setPassword("password");
        regRequest.setUsername("username");

        webClient.post()
                .uri("/api/v1/registration")
                .bodyValue(regRequest)
                .exchange()
                .expectStatus().isOk();

        var authRequest = new UserAuthRequest();
        authRequest.setPassword("password");
        authRequest.setUsername("username");

        var token = webClient.post()
                .uri("/api/v1/auth")
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        webClient.post()
                .uri("/api/v1/logout")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();

        webClient.get()
                .uri("/api/v1/user")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void changePasswordSuccessTest() {
        var testUsername = "username";
        var oldPassword = "password";
        var newPassword = "pass";

        var user = new User();
        user.setUsername(testUsername);
        user.setPassword(oldPassword);
        userRepository.save(user);

        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword(newPassword);
        changePasswordRequest.setNewPassword(newPassword);
        changePasswordRequest.setUsername(testUsername);
        changePasswordRequest.setVerificationCode("0000");

        webClient.post()
                .uri("/api/v1/change-pass")
                .bodyValue(changePasswordRequest)
                .exchange()
                .expectStatus().isOk();

        var authRequest = new UserAuthRequest();
        authRequest.setPassword(newPassword);
        authRequest.setUsername(testUsername);

        var token = webClient.post()
                .uri("/api/v1/auth")
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(token).isNotNull();
    }

    @Test
    void changePasswordUserNotFoundTest() {
        var testUsername = "username";
        var newPassword = "pass";

        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword(newPassword);
        changePasswordRequest.setNewPassword(newPassword);
        changePasswordRequest.setUsername(testUsername);
        changePasswordRequest.setVerificationCode("0000");

        var error = webClient.post()
                .uri("/api/v1/change-pass")
                .bodyValue(changePasswordRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo(String.format("User with username %s not found", testUsername));
    }

    @Test
    void changePasswordVerificationCodeIncorrectTest() {
        var testUsername = "username";
        var newPassword = "pass";
        var oldPassword = "password";

        var user = new User();
        user.setUsername(testUsername);
        user.setPassword(oldPassword);
        userRepository.save(user);

        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword(newPassword);
        changePasswordRequest.setNewPassword(newPassword);
        changePasswordRequest.setUsername(testUsername);
        changePasswordRequest.setVerificationCode("INCORRECT");

        var error = webClient.post()
                .uri("/api/v1/change-pass")
                .bodyValue(changePasswordRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo(String.format("Wrong verification code", testUsername));
    }

    @Test
    void changePasswordPasswordMismatchTest() {
        var testUsername = "username";
        var newPassword = "pass";
        var oldPassword = "password";

        var user = new User();
        user.setUsername(testUsername);
        user.setPassword(oldPassword);
        userRepository.save(user);

        var changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setConfirmPassword(newPassword);
        changePasswordRequest.setNewPassword(oldPassword);
        changePasswordRequest.setUsername(testUsername);
        changePasswordRequest.setVerificationCode("0000");

        var error = webClient.post()
                .uri("/api/v1/change-pass")
                .bodyValue(changePasswordRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo(String.format("Passwords do not match", testUsername));
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }
}
