package order;
import courier.Courier;
import courier.CourierLogin;
import courier.CourierClient;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AcceptOrderTest {
    private int courierId;
    private int orderTrack;

    @Before
    public void setUp() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();

        Courier courier = new Courier("accept_login_" + System.currentTimeMillis(), "pass123", "AcceptMe");
        courierClient.createCourier(courier);
        Response loginResponse;
        loginResponse = courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        courierId = loginResponse.jsonPath().getInt("id");

        Order order = new Order("Марго", "Тестировщик", "ул. Ленина, 10", "1", "+79991234567",
                3, "2025-07-20", "Комментарий", null);
        Response orderResponse = orderClient.createOrder(order);
        orderTrack = orderResponse.jsonPath().getInt("track");
    }

    @Test
    public void acceptOrderSuccessfully() {
        Response response = given()
                .queryParam("courierId", courierId)
                .queryParam("orderId", orderTrack)
                .when()
                .put("/api/v1/orders/accept");
        response.then().statusCode(200);
        boolean ok = response.jsonPath().getBoolean("ok");
        assertThat(ok, equalTo(true));
    }

    @Test
    public void acceptOrderWithoutCourierIdReturnsError() {
        Response response = given()
                .queryParam("orderId", orderTrack)
                .when()
                .put("/api/v1/orders/accept");
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для обработки запроса"));
    }

    @Test
    public void acceptOrderWithInvalidCourierIdReturnsError() {
        Response response = given()
                .queryParam("courierId", 999999999)
                .queryParam("orderId", orderTrack)
                .when()
                .put("/api/v1/orders/accept");
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Курьер не найден"));
    }

    @Test
    public void acceptOrderWithoutOrderIdReturnsError() {
        Response response = given()
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept");
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для обработки запроса"));
    }

    @Test
    public void acceptOrderWithInvalidOrderIdReturnsError() {
        Response response = given()
                .queryParam("courierId", courierId)
                .queryParam("orderId", 999999999)
                .when()
                .put("/api/v1/orders/accept");
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Заказ не найден"));
    }
}