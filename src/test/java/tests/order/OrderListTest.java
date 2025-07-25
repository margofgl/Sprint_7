package tests.order;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @Test
    public void getOrdersListReturnsList() {
        Response response = orderSteps.getOrdersList();
        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("orders"), is(notNullValue()));
    }
}
