package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создать курьера с логином {courier.login}")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Авторизовать курьера с логином {courierLogin.login}")
    public Response loginCourier(CourierLogin courierLogin) {
        return given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера с id {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}
