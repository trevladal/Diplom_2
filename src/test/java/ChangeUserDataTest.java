import diplom_2_classes.User;
import diplom_2_classes.UserAPI;
import diplom_2_classes.UserSession;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChangeUserDataTest {
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
    @DisplayName("Check successfully change user data")
    @Description("Checking that the user data can be changed with auth")
    public void changeUserDataSuccessfullyWithAuthTest() {

        Response response = userAPI.creatingUser(email, password, name);

        userSession = response.body().as(UserSession.class);

        userAPI.loginUser(userSession);


        userSession.setUser(new User("test4@aboba.com","1234test", "boba2"));

        Response responsePatchUser = userAPI.patchUserInfo(userSession);

        responsePatchUser.then().assertThat().body("success", equalTo(true));
        responsePatchUser.then().assertThat().body("user.email", equalTo("test4@aboba.com"));
        responsePatchUser.then().assertThat().body("user.name", equalTo("boba2"));

        Response loginPatchedUser = userAPI.loginUser(userSession);

        loginPatchedUser
                .then()
                .assertThat()
                .body("success", Matchers.equalTo(true))
                .statusCode(200);


    }


    @Ignore
    @Test
    @DisplayName("Check successfully change user data")
    @Description("Checking that the user data can be changed")
    public void changeUserDataFailureTest() {
        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);



        //вызываем метод изменения без авторизации

    }



    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
