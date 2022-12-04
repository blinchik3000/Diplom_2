package order;

import common.Client;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private final String ORDER_ENDPOINT = "orders";

    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFiler = new ResponseLoggingFilter();


    @Step("Создаем заказ для пользователя в Stellar Burgers")
    public ValidatableResponse create(String token, Order order) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .when().
                post(ORDER_ENDPOINT)
                .then();
    }


    @Step("Получаем заказы конкретного пользователя заказ в Stellar Burgers")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .header("Authorization", token)
                .when().
                get(ORDER_ENDPOINT)
                .then();
    }


}
