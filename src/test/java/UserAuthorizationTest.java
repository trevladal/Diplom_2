import diplom_2_classes.User;
import diplom_2_classes.UserAPI;
import diplom_2_classes.UserSession;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationTest {
    private final static String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private final UserAPI userAPI = new UserAPI();
    private UserSession userSession;

    private final String email = "test123645@aboba.com";
    private final String name = "boba";
    private final String password = "123test";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Successful authorization user")
    @Description("Verifying successful user authorization")
    public void checkSuccessfulAuthorizationUser() {

        Response response = userAPI.creatingUser(email, password, name);

        userSession = response.body().as(UserSession.class);
        userSession.getUser().setPassword(password);

        Response responseLoginUser = userAPI.loginUser(userSession);

        responseLoginUser
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("Failure authorization user")
    @Description("Verifying failure user authorization with incorrect fields")
    public void checkFailureAuthorizationUser() {

        Response response = userAPI.creatingUser(email, password, name);

        userSession = response.body().as(UserSession.class);

        User incorrectEmailUser = new User("wrongEmail@wrong.com", password, "gfds");

        userSession.setUser(incorrectEmailUser);

        Response responseLoginUserWrongEmail = userAPI.loginUser(userSession);

        responseLoginUserWrongEmail
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(401);

        User incorrectPasswordUser = new User(email, "5434", "gfds");

        userSession.setUser(incorrectPasswordUser);

        Response responseLoginUserWrongPassword = userAPI.loginUser(userSession);

        responseLoginUserWrongPassword
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .statusCode(401);

    }

    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
