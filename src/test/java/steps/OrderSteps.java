package steps;

import client.OrderClient;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String ORDER_ACCEPT_PATH = "/api/v1/orders/accept";
    private static final String ORDER_TRACK_PATH = "/api/v1/orders/track";

    private final OrderClient orderClient = new OrderClient();

    public Response acceptOrder(Integer courierId, Integer orderId) {
        var request = given();

        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }

        if (orderId != null) {
            request.queryParam("orderId", orderId);
        }

        return request
                .when()
                .put(ORDER_ACCEPT_PATH);
    }

    public Response createOrder(Order order) {
        return orderClient.createOrder(order);
    }

    public Response getOrderByTrack(Integer track) {
        var request = given();

        if (track != null) {
            request.queryParam("t", track);
        }

        return request
                .when()
                .get(ORDER_TRACK_PATH);
    }

    public Response getOrdersList() {
        return orderClient.getOrdersList();
    }
}