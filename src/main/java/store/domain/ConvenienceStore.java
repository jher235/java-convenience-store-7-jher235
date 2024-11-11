package store.domain;

import store.di.InitializerFromFile;
import store.dto.AllProducts;
import store.dto.ProductStock;
import store.exception.ErrorMessage;
import store.exception.ExceptionHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConvenienceStore {
    private List<Promotion> promotions;
    private List<Product> products;

    public void initializePromotion() throws IOException {
        InitializerFromFile initializerFromFile = new InitializerFromFile();
        this.promotions = initializerFromFile.initializePromotion();
        AllProducts allProducts = initializerFromFile.initializeAllProduct(promotions);
        this.products = allProducts.products();
        initializeMissedProduct();
    }

    public ConvenienceStore() throws IOException {
        initializePromotion();
    }

    public ProductStock getStock() {
        List<Product> stock = products;
        AllProducts allProducts = new AllProducts(stock);
        return ProductStock.from(allProducts);
    }

    public List<Product> findProductsByName(String productName) {
        List<Product> productList = products.stream()
                .filter(product -> productName.equals(product.getName()))
                .toList();
        if (productList.isEmpty()) {
            ExceptionHandler.inputException(ErrorMessage.NOT_FOUND_PRODUCT);
        }
        return productList;
    }

    private void initializeMissedProduct(){
        List<Product> promotionProducts = products.stream()
                .filter(Product::isPromotionProduct)
                .toList();
        List<Product> nonePromotionProducts = products.stream()
                .filter(product -> !product.isPromotionProduct())
                .toList();
        addMissedProduct(promotionProducts, nonePromotionProducts);
    }

    private void addMissedProduct(List<Product> promotionProducts, List<Product> nonePromotionProducts){
        promotionProducts.forEach(product -> {
            boolean match = nonePromotionProducts.stream().anyMatch(nonePromotionProduct ->
                nonePromotionProduct.getName().equals(product.getName()));
            if(!match){
                int index = products.indexOf(product);
                Product newProduct = Product.from(product.getName(), product.getPrice(), 0);
                products.add(index, newProduct);
            }
        });
    }
}
