package diplom_2_classes;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsAPI {
    final static String API_INGREDIENTS = "/api/ingredients";

    @Step("Get list of ingredients")
    public Response getIngredients(){

        return given()
                .header("Content-type", "application/json")
                .get(API_INGREDIENTS);
    }
}
