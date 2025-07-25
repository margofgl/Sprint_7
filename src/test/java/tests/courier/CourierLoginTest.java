package tests.courier;

import client.CourierClient;
import model.CourierLogin;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("login" + System.currentTimeMillis(), "password123", "Loginer");
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @Test
    public void courierCanLoginSuccessfully() {
        Response response = courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        response.then().statusCode(200);
        int id = response.jsonPath().getInt("id");
        assertThat(id > 0, equalTo(true));
    }

    @Test
    public void loginFailsWhenLoginIsWrong() {
        Response response = courierClient.loginCourier(new CourierLogin("wrongLogin", courier.getPassword()));
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginFailsWhenPasswordIsWrong() {
        Response response = courierClient.loginCourier(new CourierLogin(courier.getLogin(), "wrongPassword"));
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginFailsWhenLoginIsMissing() {
        Response response = courierClient.loginCourier(new CourierLogin(null, courier.getPassword()));
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginFailsWhenPasswordIsMissing() {
        Response response = courierClient.loginCourier(new CourierLogin(courier.getLogin(), null));
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }
}