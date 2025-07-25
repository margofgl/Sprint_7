package steps;

import client.OrderClient;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String ORDER_ACCEPT_PATH = "/api/v1/orders/accept";
    private static final String ORDER_TRACK_PATH = "/api/v1/orders/track";

    private final OrderClient orderClient = new OrderClient();

    /**
     * Отправляет запрос на принятие заказа.
     * Если courierId или orderId == null, параметр не добавляется.
     */
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

    /**
     * Отправляет запрос на создание заказа через OrderClient
     */
    public Response createOrder(Order order) {
        return orderClient.createOrder(order);
    }

    /**
     * Отправляет запрос на получение заказа по треку.
     * Если track == null, запрос отправляется без параметра.
     */
    public Response getOrderByTrack(Integer track) {
        var request = given();

        if (track != null) {
            request.queryParam("t", track);
        }

        return request
                .when()
                .get(ORDER_TRACK_PATH);
    }

    /**
     * Отправляет запрос на получение списка заказов.
     */
    public Response getOrdersList() {
        return orderClient.getOrdersList();
    }
}