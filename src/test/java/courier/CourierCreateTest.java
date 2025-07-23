package courier;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierCreateTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("test_login_" + System.currentTimeMillis(), "1234", "Softpaw");
    }

    @Test
    public void courierCanBeCreated() {
        Response response = courierClient.createCourier(courier);
        response.then().statusCode(201);

        boolean isOk = response.jsonPath().getBoolean("ok");
        assertThat(isOk, equalTo(true));

        Response loginResponse = courierClient.loginCourier(
                new CourierLogin(courier.getLogin(), courier.getPassword())
        );
        courierId = loginResponse.jsonPath().getInt("id");
        assertThat(courierId > 0, equalTo(true));
    }

    @Test
    public void cannotCreateDuplicateCourier() {
        courierClient.createCourier(courier).then().statusCode(201);
        Response secondResponse = courierClient.createCourier(courier);

        secondResponse.then().statusCode(409);
        String message = secondResponse.jsonPath().getString("message");
        assertThat(message, equalTo("Этот логин уже используется. Попробуйте другой."));

        Response loginResponse = courierClient.loginCourier(
                new CourierLogin(courier.getLogin(), courier.getPassword())
        );
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @Test
    public void cannotCreateCourierWithoutRequiredFields() {
        Courier missingLogin = new Courier(null, "1234", "NoLogin");
        Response response1 = courierClient.createCourier(missingLogin);
        response1.then().statusCode(400);
        String message1 = response1.jsonPath().getString("message");
        assertThat(message1, equalTo("Недостаточно данных для создания учетной записи"));

        Courier missingPassword = new Courier("login_" + System.currentTimeMillis(), null, "NoPass");
        Response response2 = courierClient.createCourier(missingPassword);
        response2.then().statusCode(400);
        String message2 = response2.jsonPath().getString("message");
        assertThat(message2, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }
}