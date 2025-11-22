package Web_Services_Assignment.demo.service;

import Web_Services_Assignment.demo.model.Customer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    private final Map<String, Customer> customers = new HashMap<>();

    public Customer addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Optional<Customer> getCustomerById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Map<String, Customer> getAllCustomers() {
        return new HashMap<>(customers);
    }

    public Customer updateCustomer(String id, Customer updatedCustomer) {
        if (customers.containsKey(id)) {
            updatedCustomer.setId(id);
            customers.put(id, updatedCustomer);
            return updatedCustomer;
        }
        return null;
    }

    public boolean deleteCustomer(String id) {
        return customers.remove(id) != null;
    }

    public boolean customerExists(String id) {
        return customers.containsKey(id);
    }

    public void addRevenue(String customerId, double amount) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.setRevenue(customer.getRevenue() + amount);
        }
    }

    public void subtractRevenue(String customerId, double amount) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.setRevenue(customer.getRevenue() - amount);
        }
    }
}