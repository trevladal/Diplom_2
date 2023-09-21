package diplom_2_classes;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class UserAPI {
    final static String API_AUTH_REGISTER = "/api/auth/register";
    final static String API_AUTH_LOGIN = "/api/auth/login";
    final static String API_AUTH_USER = "/api/auth/user";

    @Step("Creating user")
    public Response creatingUser(String email, String password, String name) {

        User user = new User(email, password, name);

        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(API_AUTH_REGISTER);
    }

    @Step("Login user")
    public Response loginUser(UserSession userSession) {

        AuthRequest authRequest = new AuthRequest(userSession.getUser().getEmail(), userSession.getUser().getPassword());

        return given()
                .header("Content-type", "application/json")
                .body(authRequest)
                .post(API_AUTH_LOGIN);

    }
    @Step("Patch user info")
    public Response patchUserInfo(UserSession userSession) {

        RequestSpecification requestSpecification = given()
                .header("Content-type", "application/json");
        if (userSession.getAccessToken() != null) {
            requestSpecification
                    .header("Authorization", userSession.getAccessToken());
        }
        return requestSpecification
                .body(userSession.getUser())
                .patch(API_AUTH_USER);
    }

    @Step("Deleting user")
    public Response deletingUser(UserSession userSession) {

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userSession.getAccessToken())
                .delete(API_AUTH_USER);
    }

}
