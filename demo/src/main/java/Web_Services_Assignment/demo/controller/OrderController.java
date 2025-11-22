package Web_Services_Assignment.demo.controller;

import Web_Services_Assignment.demo.model.Order;
import Web_Services_Assignment.demo.model.Product;
import Web_Services_Assignment.demo.service.CustomerService;
import Web_Services_Assignment.demo.service.OrderService;
import Web_Services_Assignment.demo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, ProductService productService, CustomerService customerService) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            String customerId = (String) orderRequest.get("customerId");
            String productId = (String) orderRequest.get("productId");
            int quantity = (Integer) orderRequest.get("quantity");

            // validate product exists - kinda optional i think  
            Optional<Product> productOpt = productService.getProductById(productId);
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Product not found");
            }

            Product product = productOpt.get();

            // check stock availability so you cant buy more than is in stock
            if (product.getWholesaleStockLevel() < quantity) {
                return ResponseEntity.badRequest().body("Insufficient stock. Available: " + product.getWholesaleStockLevel());
            }

            // calculate total price
            double totalPrice = product.getRetailPrice() * quantity;

            // generate order ID
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // create order with status by defauly on creating: "pending"
            Order order = new Order(
                orderId,
                customerId,
                productId,
                quantity,
                totalPrice,
                "pending",
                LocalDateTime.now()
            );

            orderService.addOrder(order);

            // update customer revenue
            customerService.addRevenue(customerId, totalPrice);

            return ResponseEntity.status(HttpStatus.CREATED).body(order);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping
    public Map<String, Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        return orderOpt.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable String customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOpt.get();
        if ("shipped".equals(order.getStatus())) {
            return ResponseEntity.badRequest().body("Cannot cancel a shipped order");
        }

        // subtract revenue from customer before deleting order
        customerService.subtractRevenue(order.getCustomerId(), order.getOrderPrice());

        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String id, @RequestBody Map<String, String> updates) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOpt.get();
        String newStatus = updates.get("status");

        if (newStatus != null) {
            order.setStatus(newStatus);
            orderService.updateOrder(id, order);
        }

        return ResponseEntity.ok(order);
    }
}