package ru.mastkey.fj_2024.lesson5.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminControllerTest extends IntegrationTestBase {

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminDataSuccessTest() {
        var response = webClient.get()
                .uri("/api/v1/admin")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(response.getResponseBody()).isEqualTo("adminData");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAdminDataForbiddenTest() {
        webClient.get()
                .uri("/api/v1/admin")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithAnonymousUser
    void testAdminDataUnauthorizedTest() {
        webClient.get()
                .uri("/api/v1/admin")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
