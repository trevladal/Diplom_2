import diplom_2_classes.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetUserOrdersTest {
    private final static String BASE_URI =  "https://stellarburgers.nomoreparties.site";
    private OrderAPI orderAPI = new OrderAPI();
    private UserAPI userAPI = new UserAPI();
    private IngredientsAPI ingredientsAPI = new IngredientsAPI();
    private UserSession userSession;

    private final String email = "testUser123@aboba.com";
    private final String name = "boba";
    private final String password = "123test";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

    }

    @Test
    @DisplayName("Successful getting user orders")
    @Description("Verifying successful getting user orders")
    public void checkSuccessfulGettingUserOrders() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient(ingredientsData.getData().get(0).getHash());
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);

        responseCreatingOrder.then().assertThat().statusCode(200);

        Response responseGetUserOrders = orderAPI.getUserOrders(userSession);


        responseGetUserOrders.then().assertThat().body("success", is(true)).statusCode(200);
        responseGetUserOrders.then().assertThat().body("orders", is(notNullValue()));

    }

    @Test
    @DisplayName("Failure getting user orders")
    @Description("Verifying failure getting user orders")
    public void checkFailureGettingUserOrders() {
        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient(ingredientsData.getData().get(0).getHash());
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);

        responseCreatingOrder.then().assertThat().statusCode(200);

        Response responseGetUserOrders = orderAPI.getUserOrders(userSession.noAuth());

        responseGetUserOrders
                .then()
                .assertThat()
                .body("message", is("You should be authorised"))
                .statusCode(401);

    }

    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
