package ru.netology.web;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static io.restassured.RestAssured.given;

@Data
public class DataGenerator {

    public static class Autharization {
        private Autharization() {
        }

        public static ClientData generateAutharizationForActiveUser() {
            Faker faker = new Faker(new Locale("en"));
            return new ClientData(
                    faker.name().username(),
                    faker.internet().password(),
                    "active");
        }

        public static ClientData generateAutharizationForBlockedUser() {
            Faker faker = new Faker(new Locale("en"));
            return new ClientData(
                    faker.name().username(),
                    faker.internet().password(),
                    "blocked");
        }

        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        public static ClientData registrationOfActiveUser() {
            ClientData clientData = generateAutharizationForActiveUser();
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonClientData = gsonBuilder.toJson(clientData);
            given()
                    .spec(requestSpec)
                    .body(jsonClientData)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
            return clientData;
        }

        public static ClientData registrationOfBlockedUser() {
            ClientData clientData = generateAutharizationForBlockedUser();
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonClientData = gsonBuilder.toJson(clientData);
            given()
                    .spec(requestSpec)
                    .body(jsonClientData)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
            return clientData;
        }

        public static String incorrectLogin() {
            Faker faker = new Faker(new Locale("en"));
            return faker.name().username();
        }

        public static String incorrectPassword() {
            Faker faker = new Faker(new Locale("en"));
            return faker.internet().password();
        }
    }
}





