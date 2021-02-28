package ru.netology.web;

import com.codeborne.selenide.conditions.Text;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static java.time.Duration.ofSeconds;


public class AuthorizationTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestHappyPath() {
        ClientData clientData = DataGenerator.Autharization.registrationOfActiveUser();
        $("[data-test-id='login'] input").setValue(clientData.getLogin());
        $("[data-test-id='password'] input").setValue(clientData.getPassword());
        $(withText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }
    @Test
    void shouldTestIncorrectLoginOfValidUser() {
        ClientData clientData = DataGenerator.Autharization.registrationOfActiveUser();
        $("[data-test-id='login'] input").setValue(DataGenerator.Autharization.incorrectLogin());
        $("[data-test-id='password'] input").setValue(clientData.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }
    @Test
    void shouldTestIncorrectPasswordOfValidUser() {
        ClientData clientData = DataGenerator.Autharization.registrationOfActiveUser();
        $("[data-test-id='login'] input").setValue(clientData.getLogin());
        $("[data-test-id='password'] input").setValue(DataGenerator.Autharization.incorrectPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }
    @Test
    void shouldTestBlockedUser() {
        ClientData clientData = DataGenerator.Autharization.registrationOfBlockedUser();
        $("[data-test-id='login'] input").setValue(clientData.getLogin());
        $("[data-test-id='password'] input").setValue(clientData.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldHave(text("Пользователь заблокирован"));
    }
    @Test
    void shouldTestUnauthorithedUser() {
        $("[data-test-id='login'] input").setValue(DataGenerator.Autharization.generateAutharizationForActiveUser().getLogin());
        $("[data-test-id='password'] input").setValue(DataGenerator.Autharization.generateAutharizationForActiveUser().getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }
}
