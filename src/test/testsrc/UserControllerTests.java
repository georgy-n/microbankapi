
import com.baggage.entity.httpRequests.AuthenticationRequest;
import com.baggage.entity.httpRequests.RegistationRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.baggage.utils.Constants.AUTH_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UserControllerTests {
    private RequestSpecification preset = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setPort(5000)
            .build();

    private RegistationRequest registationRequest = new RegistationRequest(
            "qwerty", "qwerty", "VASYA", "PULYA2");

    private AuthenticationRequest authenticationRequest = new AuthenticationRequest(
            "qwerty", "qwerty");


    @Test
    public void OneEqualsOne() {
        assertThat(1, equalTo(1));
    }

    @Test
    public void TryLoginWithNothingMustFail() {
        given().spec(preset).post("/authenticate").then().statusCode(400);
    }

    @Test
    public void TryLoginWithNonExistantUserWarnYouAboutIt() {
        AuthenticationRequest request = new AuthenticationRequest("nonExist", "nonExist");
        given().spec(preset).body(request).post("/authenticate")
                .then().statusCode(200)
        .assertThat().body("status", equalTo("INTERNAL_ERROR"))
        .assertThat().body("message", equalTo("login or password is wrong"))
        .assertThat().body("payload", equalTo(null));
    }

    @Test
    public void GettingUserInfoWithoutTokenMustBeForbidden() {
        given().spec(preset).post("/api/userInfo").then().statusCode(403);
    }

    @Test
    public  void EmptyRegistrationWillNotBeSubmitted() {
        given().spec(preset).post("/registration").then().statusCode(400);
    }

    @Test
    public void ExistantUserWillBeLoggedInSuccessfully() {
        given().spec(preset).body(authenticationRequest).post("/authenticate").then().statusCode(200);
    }

    @Test
    public void ExistantUserWillBeLoggedInSuccessfullyAndGetToken() {
        given().spec(preset).body(authenticationRequest).post("/authenticate").then().statusCode(200)
        .assertThat().body("isEmpty()", Matchers.is(false));
    }

    @Test
    public void ExistantUserWontBeRegistered() {
        given().spec(preset).body(registationRequest).post("/registration").then().statusCode(200)
                .assertThat().body("message", equalTo("Try later"));
    }

    @Test
    public void GettingUserInfoWithTokenSuccessful() {
        AuthenticationRequest rightRequest = new AuthenticationRequest("test", "test");
        String token = given().spec(preset).body(authenticationRequest).post("/authenticate").body().print();
        token = token.substring(2, token.length()-2);
        System.out.print(token);
        given().spec(preset).header(AUTH_HEADER_NAME, token).get("/api/userInfo").then().statusCode(200);
    }
}
