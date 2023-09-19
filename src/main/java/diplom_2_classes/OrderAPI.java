package diplom_2_classes;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAPI {
    final static String API_ORDERS = "/api/orders";


    @Step("Creating order")
    public Response creatingOrder(Order order, UserSession userSession) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userSession.getAccessToken())
                .body(order)
                .post(API_ORDERS);
    }
    @Step("Get user's orders")
    public Response getUserOrders(UserSession userSession) {
        return given()
                .header("Content-type", "application/json")
                .get(API_ORDERS);

        //предусмотреть, что можно передать список без логина (передать null вместо userSesson)
    }

}
