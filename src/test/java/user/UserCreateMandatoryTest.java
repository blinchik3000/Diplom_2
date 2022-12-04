package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreateMandatoryTest {

    private final User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();

    }

    public UserCreateMandatoryTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] getUserWithoutMandatoryFields() {
        User user1 = UserGenerator.generateRandom();
        user1.setEmail(null);
        User user2 = UserGenerator.generateRandom();
        user2.setPassword(null);
        User user3 = UserGenerator.generateRandom();
        user3.setName(null);

        return new Object[][]{
                {user1},
                {user2},
                {user3},
        };
    }

    @Test
    @DisplayName("Check User Mandatory fields - email/password/name")
    @Description("Negative tests of User's mandatory fields")
    public void checkUserWithoutRequiredFieldsIsNotCreated(){
        ValidatableResponse response = userClient.register(user);
        response.assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

}
