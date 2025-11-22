package Web_Services_Assignment.demo.controller;

import Web_Services_Assignment.demo.model.Customer;
import Web_Services_Assignment.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Map<String, Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // get customer by id endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        return customerOpt.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
}