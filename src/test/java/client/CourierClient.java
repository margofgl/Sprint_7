package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;

public class CourierClient {

    // Выносим базовый URI и пути в константы
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_PATH = COURIER_PATH + "/login";

    // В статическом блоке задаём базовый URI один раз при загрузке класса
    static {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создать курьера с логином {courier.login}")
    public Response createCourier(Courier courier) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизовать курьера с логином {courierLogin.login}")
    public Response loginCourier(CourierLogin courierLogin) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера с id {courierId}")
    public Response deleteCourier(int courierId) {
        return RestAssured.given()
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}
