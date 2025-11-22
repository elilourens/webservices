package Web_Services_Assignment.demo.controller;

import Web_Services_Assignment.demo.model.Product;
import Web_Services_Assignment.demo.service.ProductService;
import Web_Services_Assignment.demo.service.WholesaleApiService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loadProducts() {
        wholesaleApiService.loadAllProducts();
        return ResponseEntity.ok("Products loaded successfully! Total: " + productService.getAllProducts().size());
    }

    @GetMapping
    public Map<String, Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> productOpt = productService.getProductById(id);
        return productOpt.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/refresh/{id}")
    public ResponseEntity<String> refreshProduct(@PathVariable String id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        wholesaleApiService.refreshProduct(id);
        return ResponseEntity.ok("Product " + id + " refreshed");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProductPrice(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOpt.get();

        if (updates.containsKey("retailPrice")) {
            double newRetailPrice = ((Number) updates.get("retailPrice")).doubleValue();

            // check that new price is higher than wholesale price
            if (newRetailPrice <= product.getWholesalePrice()) {
                return ResponseEntity.badRequest()
                    .body("Retail price must be higher than wholesale price (£" +
                          String.format("%.2f", product.getWholesalePrice()) + ")");
            }

            product.setRetailPrice(newRetailPrice);
            productService.updateProduct(id, product);
        }

        return ResponseEntity.ok(product);
    }
}