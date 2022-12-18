package org.example.request;

public class OrderFactory {

    public static OrderRequest createOrder(String[] color) {
        var createOrder = new OrderRequest();
        createOrder.setFirstName("Lika");
        createOrder.setLastName("surname");
        createOrder.setAddress("Alanya");
        createOrder.setMetroStation("Politeh");
        createOrder.setPhone("89998887766");
        createOrder.setRentTime(4);
        createOrder.setDeliveryDate("2022-10-10");
        createOrder.setComment("No comments");
        createOrder.setColor(color);
        return createOrder;
    }
}
