package Web_Services_Assignment.demo.service;

import Web_Services_Assignment.demo.model.Product;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WholesaleApiService {

    private static final String BASE_URL = "https://pmaier.eu.pythonanywhere.com/wss";
    private final ProductService productService;

    public WholesaleApiService(ProductService productService) {
        this.productService = productService;
    }

    public void loadAllProducts() {
        try {
            List<String> categories = fetchCategories();
            System.out.println("Found " + categories.size() + " categories");

            for (String category : categories) {
                System.out.println("Loading products from category: " + category);
                List<String> productIds = fetchProductIdsByCategory(category);

                for (String productId : productIds) {
                    Product product = fetchProductDetails(productId);
                    if (product != null) {
                        productService.addProduct(product);
                        System.out.println("Loaded product: " + product.getId() + " - " + product.getDescription());
                    }
                }
            }

            System.out.println("Successfully loaded " + productService.getAllProducts().size() + " products");
        } catch (Exception e) {
            System.err.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> fetchCategories() throws Exception {
        List<String> categories = new ArrayList<>();
        String response = makeHttpGetRequest(BASE_URL);

        Pattern pattern = Pattern.compile("\"category\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            categories.add(matcher.group(1));
        }

        return categories;
    }

    private List<String> fetchProductIdsByCategory(String category) throws Exception {
        List<String> productIds = new ArrayList<>();
        String url = BASE_URL + "/category/" + category;
        String response = makeHttpGetRequest(url);

        Pattern pattern = Pattern.compile("/wss/product/([A-Z0-9]+)");
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            String productId = matcher.group(1);
            if (!productIds.contains(productId)) {
                productIds.add(productId);
            }
        }

        return productIds;
    }

    private Product fetchProductDetails(String productId) {
        try {
            String url = BASE_URL + "/product/" + productId;
            String response = makeHttpGetRequest(url);

            String id = extractJsonValue(response, "id");
            String description = extractJsonValue(response, "description");
            double wholesalePrice = Double.parseDouble(extractJsonValue(response, "price"));
            int stockLevel = Integer.parseInt(extractJsonValue(response, "in_stock"));

            double retailPrice = wholesalePrice * 1.30;

            return new Product(id, description, retailPrice, stockLevel, wholesalePrice);

        } catch (Exception e) {
            System.err.println("Error fetching product " + productId + ": " + e.getMessage());
            return null;
        }
    }

    public void refreshProduct(String productId) {
        Product product = fetchProductDetails(productId);
        if (product != null) {
            productService.addProduct(product);
            System.out.println("Refreshed product: " + productId);
        }
    }

    private String makeHttpGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP GET request failed with response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();

        return response.toString();
    }

    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":(\"[^\"]*\"|[^,}]+)");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String value = matcher.group(1);
            return value.replaceAll("\"", "");
        }

        return "";
    }
}