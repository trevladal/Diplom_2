import diplom_2_classes.UserSession;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom_2_classes.UserAPI;

import static org.hamcrest.Matchers.equalTo;

public class CreatingUserTest {
    public final static String BASE_URI =  "https://stellarburgers.nomoreparties.site";
    private final UserAPI userAPI = new UserAPI();
    private UserSession userSession;
    private final String email = "test3@aboba.com";
    private final String name = "boba";
    private final String password = "123test";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

    }

    @Test
    @DisplayName("Successful creating user")
    @Description("Verifying successful user creation")
    public void checkSuccessfulCreatingUser() {

        Response response = userAPI.creatingUser(email, name, password);

        response
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);

        userSession = response.body().as(UserSession.class);
    }

    @Test
    @DisplayName("Checking already created user")
    @Description("Checking that user already created")
    public void checkAlreadyCreatedUser() {

        Response response = userAPI.creatingUser(email, name, password);

        response
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);

        userSession = response.body().as(UserSession.class);

        Response responseSecond = userAPI.creatingUser(email, name, password);

        responseSecond
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(403);

    }
    @Test
    @DisplayName("Check failure created user")
    @Description("Checking that the user cannot be created without field")
    public void checkFailureCreatedUser() {

        Response response = userAPI.creatingUser("", name, password);

        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(403);
    }
    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
