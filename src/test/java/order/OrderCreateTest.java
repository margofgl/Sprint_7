package order;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final Order order;
    private final int expectedStatusCode;

    public OrderCreateTest(Order order, int expectedStatusCode) {
        this.order = order;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                { new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, "2025-07-20", "Комментарий", Collections.singletonList("BLACK")), 201 },
                { new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, "2025-07-20", "Комментарий", Collections.singletonList("GREY")), 201 },
                { new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, "2025-07-20", "Комментарий", Arrays.asList("BLACK", "GREY")), 201 },
                { new Order("Марго", "Тестировщик", "ул. Гейдара Алиева, 1", "2", "+79991234567",
                        5, "2025-07-20", "Комментарий", Collections.emptyList()), 201 },
        });
    }

    @Test
    public void orderCreationTest() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.createOrder(order);
        response.then().statusCode(expectedStatusCode);

        int track = response.jsonPath().getInt("track");
        assertThat("Трек должен быть больше 0", track, greaterThan(0));
    }
}