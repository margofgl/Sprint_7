package tests.order;

import io.restassured.response.Response;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final Order order;
    private final int expectedStatusCode;

    private final OrderSteps orderSteps = new OrderSteps();

    public OrderCreateTest(Order order, int expectedStatusCode) {
        this.order = order;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        String date = LocalDate.now().plusDays(1).toString();
        return Arrays.asList(new Object[][]{
                {new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, date, "Комментарий", Collections.singletonList("BLACK")), SC_CREATED},
                {new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, date, "Комментарий", Collections.singletonList("GREY")), SC_CREATED},
                {new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, date, "Комментарий", Arrays.asList("BLACK", "GREY")), SC_CREATED},
                {new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, date, "Комментарий", Collections.emptyList()), SC_CREATED},
        });
    }

    @Test
    public void orderCreationTest() {
        System.out.println("Создание заказа с цветами: " + order.getColor());

        Response response = orderSteps.createOrder(order);
        response.then().log().all().statusCode(expectedStatusCode);

        int track = response.jsonPath().getInt("track");
        assertThat("Трек должен быть больше 0", track, greaterThan(0));
    }
}
