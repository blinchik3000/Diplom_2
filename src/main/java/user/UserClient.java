package user;

import common.Client;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private final String USER_CREATE_ENDPOINT = "auth/register";
    private final String USER_LOGIN_ENDPOINT = "auth/login";
    private final String USER_INFO_ENDPOINT = "auth/user";

    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFiler = new ResponseLoggingFilter();

    @Step("Создаем пользователя в Stellar Burgers")
    public ValidatableResponse register(User user) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .body(user)
                .when().
                post(USER_CREATE_ENDPOINT)
                .then();
    }

    @Step("авторизуемся пользователем в Stellar Burgers")
    public ValidatableResponse login(User user) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .body(user)
                .when().
                post(USER_LOGIN_ENDPOINT)
                .then();
    }


    @Step("обновляем ифнормацию по пользователю в Stellar Burgers")
    public ValidatableResponse update(User user, String token) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .header("Authorization", token)
                .body(user)
                .when().
                patch(USER_INFO_ENDPOINT)
                .then();
    }


    @Step("получаем ифнормацию по пользователю в Stellar Burgers")
    public ValidatableResponse getInfo(String token) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .header("Authorization", token)
                .when().
                get(USER_INFO_ENDPOINT)
                .then();
    }


    @Step("удаляем ифнормацию по пользователю в Stellar Burgers")
    public ValidatableResponse delete(String token) {
        return given()
                .filters(requestFilter, responseFiler)
                .header("Authorization", token)
                .spec(getSpec())
                .when().
                delete(USER_INFO_ENDPOINT)
                .then();
    }


}
