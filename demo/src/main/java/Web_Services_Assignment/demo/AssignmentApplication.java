package Web_Services_Assignment.demo;

import Web_Services_Assignment.demo.model.Customer;
import Web_Services_Assignment.demo.service.CustomerService;
import Web_Services_Assignment.demo.service.WholesaleApiService;
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
	public CommandLineRunner loadInitialData(CustomerService customerService, WholesaleApiService wholesaleApiService) {
		return args -> {

			Customer customer1 = new Customer(
				"1",
				"Hamish MacLeod",
				"Hamish.Macleod@example.com",
				"12 Royal Mile, Edinburgh, EH1 2PB",
				0
			);
			customerService.addCustomer(customer1);


			Customer customer2 = new Customer(
				"2",
				"Fiona Campbell",
				"Fiona.Campbell@example.com",
				"45 Sauchiehall Street, Glasgow, G2 3AT",
				0
			);
			customerService.addCustomer(customer2);


			Customer customer3 = new Customer(
				"3",
				"Angus Fraser",
				"Angus.Fraser@example.com",
				"78 Union Street, Aberdeen, AB11 6BD",
				0
			);
			customerService.addCustomer(customer3);

			System.out.println("Loaded 3 initial customers");

			System.out.println("Loading products from wholesale API...");
			wholesaleApiService.loadAllProducts();
		};
	}

}
