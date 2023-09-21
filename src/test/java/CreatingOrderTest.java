import diplom_2_classes.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreatingOrderTest {

    private UserAPI userAPI = new UserAPI();
    private OrderAPI orderAPI = new OrderAPI();
    private IngredientsAPI ingredientsAPI = new IngredientsAPI();

    private UserSession userSession;
    private final String email = "test1234@boba.com";
    private final String name = "boba";
    private final String password = "123test";

    public final static String BASE_URI =  "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Successful creating order with auth")
    @Description("Verifying successful order creation with authorization and ingredients")
    public void checkSuccessfullyCreatingOrderWithAuthAndIngredients() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient(ingredientsData.getData().get(0).getHash());
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);


        responseCreatingOrder
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);

    }

    //тест на создание заказа без авторизации не проходит, т.к. в приложении баг (как сказал наставник из чата, потому что заказ создаётся и без авторизации),
    // поэтому проверяем из ожидаемого результата пользователя (т.к. в доке результат не описан,
    // поэтому предположительно проверяем 401 статус (может быть нужно 403))

    @Test
    @DisplayName("Failure creating order without auth")
    @Description("Verifying failure order creation without authorization")
    public void checkFailureCreatingOrderWithoutAuth() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient(ingredientsData.getData().get(0).getHash());
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession.noAuth());

        //ЭТО ОЖИДАЕМЫЙ РЕЗУЛЬТАТ, НО ЕГО НЕТ, Т.К. В ПРИЛОЖЕНИИ БАГ, КАК СКАЗАЛ НАСТАВНИК (см. коммент выше)
        responseCreatingOrder
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(401);
    }
    @Test
    @DisplayName("Failure creating order without ingredients")
    @Description("Verifying failure order creation without ingredients")
    public void checkFailureCreatingOrderWithoutIngredients() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        userAPI.loginUser(userSession);

        Ingredients ingredientsWrapper = Ingredients.empty();

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);

        responseCreatingOrder
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(400);
    }

    @Test
    @DisplayName("Successful creating order with auth")
    @Description("Verifying successful order creation with authorization and ingredients")
    public void checkFailureCreatingOrderWithWrongHash() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient("wrongHash");
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);

        responseCreatingOrder.then().assertThat().statusCode(500);

    }

    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
