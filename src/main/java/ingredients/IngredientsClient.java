package ingredients;

import common.Client;
import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends Client {
    private final String INGREDIENTS_ENDPOINT = "ingredients";


    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFiler = new ResponseLoggingFilter();


    @Step("Получаем ингредиенты в Stellar Burgers")
    public ValidatableResponse getList() {
        return given()
                .filters(requestFilter, responseFiler)
                .spec(getSpec())
                .when().
                get(INGREDIENTS_ENDPOINT)
                .then();
    }
}
