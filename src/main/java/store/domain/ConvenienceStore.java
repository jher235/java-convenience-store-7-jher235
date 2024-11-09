package store.domain;

import store.di.InitializerFromFile;
import store.dto.AllProducts;
import store.dto.ProductStock;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConvenienceStore {
    private List<Promotion> promotions;
    private List<Product> products;
//    private List<PromotionProduct> promotionProducts;

    public void initializePromotion() throws IOException {
        InitializerFromFile initializerFromFile = new InitializerFromFile();
        this.promotions = initializerFromFile.initializePromotion();
        AllProducts allProducts = initializerFromFile.initializeAllProduct(promotions);
        this.products = allProducts.products();
//        this.promotionProducts = allProducts.promotionProducts();
    }

    public ConvenienceStore() throws IOException {
        initializePromotion();
    }

    public ProductStock getStock(){
        List<Product> stock = products;
        AllProducts allProducts = new AllProducts(stock);
        return ProductStock.from(allProducts);
    }
}
