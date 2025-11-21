package Web_Services_Assignment.demo.service;

import Web_Services_Assignment.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final Map<String, Product> products = new HashMap<>();

    public Product addProduct(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public Map<String, Product> getAllProducts() {
        return new HashMap<>(products);
    }

    public Product updateProduct(String id, Product updatedProduct) {
        if (products.containsKey(id)) {
            updatedProduct.setId(id);
            products.put(id, updatedProduct);
            return updatedProduct;
        }
        return null;
    }

    public boolean deleteProduct(String id) {
        return products.remove(id) != null;
    }

    public boolean productExists(String id) {
        return products.containsKey(id);
    }
}