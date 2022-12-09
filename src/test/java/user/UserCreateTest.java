package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private User user;
    private UserClient userClient;
    private ValidatableResponse responseCreate;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.generateRandom();
        responseCreate = userClient.register(user);

    }

    @After
    public void cleanUp() {
        String token = responseCreate.extract().path("accessToken");
        userClient.delete(token).assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Check User can be created")
    @Description("Basic test for User creation - 200/response fields correct")
    public void checkUniqueUserCanBeCreated() {
        responseCreate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Check system reaction to double User")
    @Description("Negative tests of User's double")
    public void checkDuplicateUserCanNotBeCreated() {
        ValidatableResponse responseCreateDouble = userClient.register(user);
        responseCreateDouble.assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"));
    }
}
