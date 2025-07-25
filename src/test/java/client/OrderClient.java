package client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public OrderClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    public Response getOrdersList() {
        return given()
                .when()
                .get("/api/v1/orders");
    }
}
