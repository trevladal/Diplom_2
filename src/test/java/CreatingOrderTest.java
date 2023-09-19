import diplom_2_classes.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class CreatingOrderTest {

    UserAPI userAPI = new UserAPI();
    OrderAPI orderAPI = new OrderAPI();
    IngredientsAPI ingredientsAPI = new IngredientsAPI();

    private UserSession userSession;
    private final String email = "test3@aboba.com";
    private final String name = "boba";
    private final String password = "123test";

    public final static String BASE_URI =  "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Successful creating order")
    @Description("Verifying successful order creation")
    public void checkSucsesfullCreatingOrderWithAuth() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        userAPI.loginUser(userSession);

        Response responseIngredientsData = ingredientsAPI.getIngredients();

        IngredientsData ingredientsData = responseIngredientsData.body().as(IngredientsData.class);

        Ingredients ingredientsWrapper = Ingredients.empty();

        ingredientsWrapper.addIngredient(ingredientsData.getData().get(0).getHash());
        ingredientsWrapper.addIngredient(ingredientsData.getData().get(1).getHash());

        Order order = new Order(ingredientsWrapper.getIngredients());

        Response responseCreatingOrder = orderAPI.creatingOrder(order, userSession);

        //тест не проходит, т.к. в приложении баг (как сказал наставник из чата, потому что заказ создаётся и без авторизации),
        // поэтому проверяем из ожидаемого результата (но его тоже нет в доке, поэтому 403)
        responseCreatingOrder.then().assertThat().statusCode(200);

    }

     ///

}
