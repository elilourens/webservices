package Web_Services_Assignment.demo.service;

import Web_Services_Assignment.demo.model.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final Map<String, Order> orders = new HashMap<>();

    public Order addOrder(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> getOrderById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public Map<String, Order> getAllOrders() {
        return new HashMap<>(orders);
    }

    public Order updateOrder(String id, Order updatedOrder) {
        if (orders.containsKey(id)) {
            updatedOrder.setId(id);
            orders.put(id, updatedOrder);
            return updatedOrder;
        }
        return null;
    }

    public boolean deleteOrder(String id) {
        return orders.remove(id) != null;
    }

    public boolean orderExists(String id) {
        return orders.containsKey(id);
    }
}