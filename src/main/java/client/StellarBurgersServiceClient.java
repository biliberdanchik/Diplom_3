package client;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class StellarBurgersServiceClient {

    private final String baseURI = "https://stellarburgers.nomoreparties.site";

    @Step("Клиент - создание нового пользователя и получение токена")
    public String createUserAndGetToken(String name, String email, String password) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(String.format("{\"email\": \"%s\", \"password\": \"%s\",\"name\": \"%s\"}", email, password, name))
                .when()
                .post("/api/auth/register")
                .then()
                .log()
                .all().extract().jsonPath().getString("accessToken");
    }

    @Step("Клиент - Получение токена пользователя")
    public String getAccessToken(String email, String password) {
        return given()
                    .log()
                    .all()
                    .baseUri(baseURI)
                    .header("Content-Type", "application/json")
                    .body(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password))
                    .when()
                    .post("/api/auth/login")
                    .then()
                    .log()
                    .all().extract().jsonPath().getString("accessToken");
    }


    @Step("Клиент - Удаление созданного пользователя")
    public void deleteUser(String accessToken) {
        given()
                .log()
                .all()
                .baseUri(baseURI)
                .auth().oauth2(accessToken.replace("Bearer ", ""))
                .when()
                .delete("/api/auth/user")
                .then()
                .log()
                .all();
    }
}
