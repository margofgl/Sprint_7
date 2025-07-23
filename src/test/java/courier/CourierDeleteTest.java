package courier;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierDeleteTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("delete_login_" + System.currentTimeMillis(), "pass123", "DeleteMe");
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @Test
    public void deleteCourierSuccessfully() {
        Response response = courierClient.deleteCourier(courierId);
        response.then().statusCode(200);
        boolean ok = response.jsonPath().getBoolean("ok");
        assertThat(ok, equalTo(true));
    }

    @Test
    public void deleteCourierWithoutIdReturnsError() {
        Response response = given()
                .when()
                .delete("/api/v1/courier/"); // без id
        response.then().statusCode(404); // может быть 404, зависит от API
    }

    @Test
    public void deleteCourierWithInvalidIdReturnsError() {
        Response response = courierClient.deleteCourier(999999999);
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Курьер с таким id не найден"));
    }

    @After
    public void tearDown() {
        // на всякий случай пытаемся удалить (если не удалили)
        courierClient.deleteCourier(courierId);
    }
}