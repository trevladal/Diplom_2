package diplom_2_classes;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsAPI {
    final static String API_INGREDIENTS = "/api/ingredients";

    public Response getIngredients(){

        return given()
                .header("Content-type", "application/json")
                .get(API_INGREDIENTS);
    }
}
