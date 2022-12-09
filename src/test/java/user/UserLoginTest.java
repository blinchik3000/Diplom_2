package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {


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
    public void cleanUp(){
        String token = responseCreate.extract().path("accessToken");
        userClient.delete(token).assertThat().statusCode(202);
    }




    @Test
    @DisplayName("Check User can login")
    @Description("Basic test for User logining - return 200/id")
    public void checkUserCanLogin() {
        ValidatableResponse responseLogin = userClient.login(user);
        responseLogin.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("accessToken",notNullValue())
                .and().body("refreshToken",notNullValue())
                .and().body("user.email",equalTo(user.getEmail().toLowerCase()))
                .and().body("user.name",equalTo(user.getName()));
    }

    @Test
    @DisplayName("Check User can't login with wrong email")
    @Description("Negative test for User login - wrong email")
    public void checkErrorWhenEmailWrong(){
        String tempEmail = user.getEmail();
        user.setEmail("wrongEmail"+user.getEmail());
        ValidatableResponse responseLogin = userClient.login(user);
        responseLogin.assertThat().statusCode(401)
                .and().body("success",equalTo(false))
                .and().body("message",equalTo("email or password are incorrect"));
        user.setEmail(tempEmail);

    }

    @Test
    @DisplayName("Check User can't login with wrong password")
    @Description("Negative test for User login - wrong password")
    public void checkErrorWhenPassWrong(){
        String tempPass = user.getPassword();
        user.setPassword(user.getPassword()+"wrongPassword");
        ValidatableResponse responseLogin = userClient.login(user);
        responseLogin.assertThat().statusCode(401)
                .and().body("success",equalTo(false))
                .and().body("message",equalTo("email or password are incorrect"));
        user.setPassword(tempPass);
    }
}
