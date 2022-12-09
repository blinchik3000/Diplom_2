package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class OrderGetUsersOrdersTest {

    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String token;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.generateRandom();
        ValidatableResponse responseCreate = userClient.register(user);
        token = responseCreate.extract().path("accessToken");
        orderClient = new OrderClient();


    }

    @After
    public void cleanUp() {
        userClient.delete(token).assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Check getting orders for user")
    @Description("Check we can get order info for user")
    public void checkGettingOrderForUser() {
        Order order = new Order(OrderGenerator.getSomeIngredientsForOrder(1));
        orderClient.create(token, order);

        ValidatableResponse response = orderClient.getUserOrders(token);
        response.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and().body("total", notNullValue())
                .and().body("totalToday", notNullValue());
    }


    @Test
    @DisplayName("Check getting orders for user without authorization")
    @Description("Check we can get order info for user without authorization")
    public void checkGettingOrderForUserWithoutAuthorization() {
        Order order = new Order(OrderGenerator.getSomeIngredientsForOrder(1));
        orderClient.create(token, order);
        String tokenIsEmpty = "";

        ValidatableResponse response = orderClient.getUserOrders(tokenIsEmpty);
        response.assertThat().statusCode(401)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

}
