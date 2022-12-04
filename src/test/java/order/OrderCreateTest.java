package order;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {
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

        userClient.register(user);
        orderClient = new OrderClient();

    }

    @After
    public void cleanUp() {
        userClient.delete(token).assertThat().statusCode(202);
    }


    @Test
    @DisplayName("Check Order can be created")
    @Description("Basic test for Order creation - 200/response fields correct")
    public void checkOrderCanBeCreated() {
        List<String> ingredientsList = OrderGenerator.getSomeIngredientsForOrder(1);
        Order order = new Order(ingredientsList);
        ValidatableResponse responseCreate = orderClient.create(token, order);
        responseCreate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("name", notNullValue())
                .and().body("order.ingredients[0]._id", equalTo(ingredientsList.get(0)))
                .and().body("order._id", notNullValue())
                .and().body("order.owner.name", equalTo(user.getName()))
                .and().body("order.owner.email", equalTo(user.getEmail().toLowerCase()))
                .and().body("order.owner.createdAt", notNullValue())
                .and().body("order.owner.updatedAt", notNullValue())
                .and().body("order.status", equalTo("done"))
                .and().body("order.name", notNullValue())
                .and().body("order.createdAt", notNullValue())
                .and().body("order.updatedAt", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("order.price", notNullValue());
    }


    @Test
    @DisplayName("Check Order can be created without authorization")
    @Description("Order can be created without authorization")
    public void checkOrderwithotAuthorizationCanBeCreated() {
        List<String> ingredientsList = OrderGenerator.getSomeIngredientsForOrder(1);
        Order order = new Order(ingredientsList);
        String emptyToken = "";
        ValidatableResponse responseCreate = orderClient.create(emptyToken, order);
        responseCreate.assertThat().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
    }


    @Test
    @DisplayName("Check Order can not be created with wrong ingredient code")
    @Description("Order can not be created with wrong ingredient code")
    @Issue("Bug - 500 status code instead of 400")
    public void checkOrderCanNotBeCreatedWithWrongIngredient() {
        List<String> ingredientsList = OrderGenerator.getSomeIngredientsForOrder(1);
        ingredientsList.set(0, ingredientsList.get(0) + "wrongHash");
        Order order = new Order(ingredientsList);
        ValidatableResponse responseCreate = orderClient.create(token, order);
        responseCreate.assertThat().statusCode(400)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Check Order can not be created with null ingredient code")
    @Description("Order can not be created with null ingredient code")
    @Issue("Bug - 400 status code instead of 500")
    public void checkOrderCanNotBeCreatedWithNullIngredient() {
        List<String> ingredientsList = OrderGenerator.getSomeIngredientsForOrder(1);
        ingredientsList.set(0, null);
        Order order = new Order(ingredientsList);
        ValidatableResponse responseCreate = orderClient.create(token, order);
        responseCreate.assertThat().statusCode(500);
    }

}
