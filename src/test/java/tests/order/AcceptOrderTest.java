package tests.order;

import client.CourierClient;
import client.OrderClient;
import model.Courier;
import model.CourierLogin;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AcceptOrderTest {

    private int courierId;
    private int orderTrack;
    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        orderSteps = new OrderSteps();

        Courier courier = new Courier("accept_login_" + System.currentTimeMillis(), "pass123", "AcceptMe");
        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()));
        courierId = loginResponse.jsonPath().getInt("id");

        Order order = new Order("Марго", "Тестировщик", "ул. Ленина, 10", "1", "+79991234567",
                3, "2025-07-20", "Комментарий", null);
        Response orderResponse = orderClient.createOrder(order);
        orderTrack = orderResponse.jsonPath().getInt("track");
    }

    @Test
    public void acceptOrderSuccessfully() {
        Response response = orderSteps.acceptOrder(courierId, orderTrack);
        response.then().statusCode(SC_OK);
        boolean ok = response.jsonPath().getBoolean("ok");
        assertThat(ok, equalTo(true));
    }

    @Test
    public void acceptOrderWithoutCourierIdReturnsError() {
        Response response = orderSteps.acceptOrder(null, orderTrack);
        response.then().statusCode(SC_BAD_REQUEST);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для обработки запроса"));
    }

    @Test
    public void acceptOrderWithInvalidCourierIdReturnsError() {
        Response response = orderSteps.acceptOrder(999999999, orderTrack);
        response.then().statusCode(SC_NOT_FOUND);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Курьер не найден"));
    }

    @Test
    public void acceptOrderWithoutOrderIdReturnsError() {
        Response response = orderSteps.acceptOrder(courierId, null);
        response.then().statusCode(SC_BAD_REQUEST);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для обработки запроса"));
    }

    @Test
    public void acceptOrderWithInvalidOrderIdReturnsError() {
        Response response = orderSteps.acceptOrder(courierId, 999999999);
        response.then().statusCode(SC_NOT_FOUND);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Заказ не найден"));
    }
}