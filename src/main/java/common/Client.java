package common;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    public static String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

}
