package order;

import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderGetByTrackTest {

    @Test
    public void getOrderByTrackSuccessfully() {
        int existingTrack = 12345;

        Response response = given()
                .queryParam("t", existingTrack)
                .when()
                .get("/api/v1/orders/track");
        response.then().statusCode(200);
        assertThat(response.jsonPath().getInt("order.track"), equalTo(existingTrack));
    }

    @Test
    public void getOrderByTrackWithoutTrackReturnsError() {
        Response response = given()
                .when()
                .get("/api/v1/orders/track");
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void getOrderByTrackWithInvalidTrackReturnsError() {
        Response response = given()
                .queryParam("t", 999999999)
                .when()
                .get("/api/v1/orders/track");
        response.then().statusCode(404);
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Заказ не найден"));
    }
}