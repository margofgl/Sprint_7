package order;

import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    @Test
    public void getOrdersListReturnsList() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrdersList();
        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("orders"), is(notNullValue()));
    }
}
