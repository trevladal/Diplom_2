package diplom_2_classes;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderAPI {
    final static String API_ORDERS = "/api/orders";


    @Step("Creating order")
    public Response creatingOrder(Order order, UserSession userSession) {

        RequestSpecification requestSpecification = given()
                .header("Content-type", "application/json");

        if (userSession.getAccessToken() != null) {
            requestSpecification
                    .header("Authorization", userSession.getAccessToken());
        }
            return requestSpecification
                    .body(order)
                    .post(API_ORDERS);

    }
    @Step("Get user's orders")
    public Response getUserOrders(UserSession userSession) {

        RequestSpecification requestSpecification = given()
                .header("Content-type", "application/json");
        if (userSession.getAccessToken() != null) {
            requestSpecification
                    .header("Authorization", userSession.getAccessToken());
        }

        return requestSpecification
                .get(API_ORDERS);

        //предусмотреть, что можно передать список без логина (передать null вместо userSesson)
    }

}
