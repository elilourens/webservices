package Web_Services_Assignment.demo;

import Web_Services_Assignment.demo.model.Customer;
import Web_Services_Assignment.demo.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadInitialCustomers(CustomerService customerService) {
		return args -> {
			
			Customer customer1 = new Customer(
				"C001",
				"John Smith",
				"john.smith@example.com",
				"123 Main Street, New York, NY 10001",
				0
			);
			customerService.addCustomer(customer1);

			
			Customer customer2 = new Customer(
				"C002",
				"Sarah Johnson",
				"sarah.johnson@example.com",
				"456 Oak Avenue, Los Angeles, CA 90001",
				0
			);
			customerService.addCustomer(customer2);

			
			Customer customer3 = new Customer(
				"C003",
				"Michael Chen",
				"michael.chen@example.com",
				"789 Pine Road, Chicago, IL 60601",
				0
			);
			customerService.addCustomer(customer3);

			System.out.println("Loaded 3 initial customers");
		};
	}

}
