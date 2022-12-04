package order;

import ingredients.IngredientsClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    @Step("Получаем список из нескольких интредиентов")
    public static List<String> getSomeIngredientsForOrder(int countOfIngredients) {
        IngredientsClient ingredientsClient = new IngredientsClient();
        ValidatableResponse response = ingredientsClient.getList();
        List<String> ingredientsIds = new ArrayList<>();
        if (countOfIngredients > 0) {
            for (int i = 0; i < countOfIngredients; i++) {
                String ingredientId = response.extract().body().path(String.format("data[%d]._id", i));
                ingredientsIds.add(ingredientId);
            }
        }
        return ingredientsIds;
    }
}
