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
    }

    public ConvenienceStore() throws IOException {
        initializePromotion();
    }

    public ProductStock getStock(){
        List<Product> stock = products;
        AllProducts allProducts = new AllProducts(stock);
        return ProductStock.from(allProducts);
    }

    public List<Product> findProductsByName(String productName){
        List<Product> productList = products.stream()
                .filter(product -> productName.equals(product.getName()))
                .toList();
        if(productList.isEmpty()){
            ExceptionHandler.inputException(ErrorMessage.NOT_FOUND_PRODUCT);
        }
        return productList;
    }
}
