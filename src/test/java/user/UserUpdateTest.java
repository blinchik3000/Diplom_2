package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest {

    private User user;
    private UserClient userClient;
    private ValidatableResponse responseCreate;
    private String token;
    private String tempEmail;
    private String tempPassword;
    private String tempName;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.generateRandom();
        responseCreate = userClient.register(user);

        token = responseCreate.extract().path("accessToken");
        tempEmail = user.getEmail();
        tempPassword = user.getPassword();
        tempName = user.getName();

    }

    @After
    public void cleanUp() {
        userClient.delete(token).assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Check User can be updated")
    @Description("Checking user fields have changed after update - return 200")
    public void checkUserCanBeUpdated() {

        user.setEmail(tempEmail + "1");
        user.setPassword(tempPassword + "1");
        user.setName(tempName + "1");

        ValidatableResponse responseUpdate = userClient.update(user, token);
        responseUpdate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name", equalTo(user.getName()));

        ValidatableResponse responseGetInfoAfterUpdate = userClient.getInfo(token);
        responseGetInfoAfterUpdate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name", equalTo(user.getName()));

        ValidatableResponse responseWithNewPassword = userClient.login(user);
        responseWithNewPassword.assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Check User can be updated without authorization")
    @Description("Checking user fields have not changed after update without authorization")
    public void checkUserCanBeUpdatedWithoutAuthorization() {
        String tokenNull = "";

        user.setEmail(tempEmail + "1");
        user.setPassword(tempPassword + "1");
        user.setName(tempName + "1");
        ValidatableResponse responseUpdate = userClient.update(user, tokenNull);
        responseUpdate.assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));

        ValidatableResponse responseGetInfoAfterUpdate = userClient.getInfo(token);
        responseGetInfoAfterUpdate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(tempEmail.toLowerCase()))
                .and().body("user.name", equalTo(tempName));

        ValidatableResponse responseWithNewPassword = userClient.login(user);
        responseWithNewPassword.assertThat().statusCode(401);

    }

}
