package tests.order;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderGetByTrackTest {

    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @Test
    public void getOrderByTrackSuccessfully() {
        int existingTrack = 12345;

        Response response = orderSteps.getOrderByTrack(existingTrack);
        response.then().statusCode(SC_OK);
        assertThat(response.jsonPath().getInt("order.track"), equalTo(existingTrack));
    }

    @Test
    public void getOrderByTrackWithoutTrackReturnsError() {
        Response response = orderSteps.getOrderByTrack(null);
        response.then().statusCode(SC_BAD_REQUEST);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void getOrderByTrackWithInvalidTrackReturnsError() {
        Response response = orderSteps.getOrderByTrack(999999999);
        response.then().statusCode(SC_NOT_FOUND);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Заказ не найден"));
    }
}