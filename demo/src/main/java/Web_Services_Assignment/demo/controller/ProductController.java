package Web_Services_Assignment.demo.controller;

import Web_Services_Assignment.demo.model.Product;
import Web_Services_Assignment.demo.service.ProductService;
import Web_Services_Assignment.demo.service.WholesaleApiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final WholesaleApiService wholesaleApiService;

    public ProductController(ProductService productService, WholesaleApiService wholesaleApiService) {
        this.productService = productService;
        this.wholesaleApiService = wholesaleApiService;
    }

    @PostMapping("/load")
    public String loadProducts() {
        wholesaleApiService.loadAllProducts();
        return "Products loaded successfully! Total: " + productService.getAllProducts().size();
    }

    @GetMapping
    public Map<String, Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping("/refresh/{id}")
    public String refreshProduct(@PathVariable String id) {
        wholesaleApiService.refreshProduct(id);
        return "Product " + id + " refreshed";
    }
}