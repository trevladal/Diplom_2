import diplom_2_classes.User;
import diplom_2_classes.UserAPI;
import diplom_2_classes.UserSession;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;


public class ChangeUserDataTest {
    public final static String BASE_URI =  "https://stellarburgers.nomoreparties.site";
    private final UserAPI userAPI = new UserAPI();
    private UserSession userSession;

    private final String email = "test8778@aboba.com";
    private final String name = "boba";
    private final String password = "123test";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Check successfully change user data")
    @Description("Checking whether all fields can be changed with authorization")
    public void changeUserDataSuccessfullyWithAuthTest() {

        Response response = userAPI.creatingUser(email, password, name);

        userSession = response.body().as(UserSession.class);

        userAPI.loginUser(userSession);

        userSession.setUser(new User("test9999@aboba.com","1234test", "boba2"));

        Response responsePatchUser = userAPI.patchUserInfo(userSession);

        responsePatchUser.then().assertThat().body("success", equalTo(true));
        responsePatchUser.then().assertThat().body("user.email", equalTo("test9999@aboba.com"));
        responsePatchUser.then().assertThat().body("user.name", equalTo("boba2"));

        Response loginPatchedUser = userAPI.loginUser(userSession);

        loginPatchedUser
                .then()
                .assertThat()
                .body("success", Matchers.equalTo(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("Check failure change user data")
    @Description("Checking the impossibility of changing fields without authorization")
    public void changeUserDataFailureTest() {

        Response response = userAPI.creatingUser(email, name, password);

        userSession = response.body().as(UserSession.class);

        UserSession userSessionNullAuth = userSession.noAuth();

        userSessionNullAuth.setUser(new User("test9999@aboba.com","1234test", "boba2"));

        Response responsePatchUser = userAPI.patchUserInfo(userSessionNullAuth);

        responsePatchUser
                .then()
                .assertThat()
                .body("success", Matchers.equalTo(false))
                .statusCode(401);

    }
    @After
    public void tearDown() {
        if (userSession != null) {
            userAPI.deletingUser(userSession);
        }
    }
}
