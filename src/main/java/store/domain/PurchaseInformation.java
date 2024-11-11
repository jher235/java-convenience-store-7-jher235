package store.domain;

import store.dto.PurchaseRequest;
import store.exception.ExceptionHandler;

import java.util.List;
import java.util.Optional;

import static store.exception.ErrorMessage.NOT_FOUND_PRODUCT;

public class PurchaseInformation {
    private Product product;
    private Optional<Product> promotionProduct;
    private String productName;
    private int requestQuantity;
    private Optional<Promotion> promotion;

    public PurchaseInformation(List<Product> products, PurchaseRequest purchaseRequest) {
        promotionProduct = products.stream()
                .filter(Product::isPromotionProduct)
                .findFirst();
        product = products.stream()
                .filter(product -> !product.isPromotionProduct())
                .findFirst()
                .orElse(createFromPromotionProduct());
        productName = purchaseRequest.getProductName();
        requestQuantity = purchaseRequest.getQuantity();
        initializePromotion();
    }

    public Product getProduct() {
        return product;
    }

    public Optional<Product> getPromotionProduct() {
        return promotionProduct;
    }

    public String getProductName() {
        return productName;
    }

    public int getRequestQuantity() {
        return requestQuantity;
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    public boolean isPromotionValid() {
        if (promotion.isPresent() && promotionProduct.isPresent()) {
            Promotion thisPromotion = promotion.get();
            if (thisPromotion.isPromotionPeriod()) {
                return true;
            }
        }
        return false;
    }

    public void increaseQuantity() {
        this.requestQuantity++;
    }

    public void subtractRetailPriceProduct(int retailPriceProduct) {
        this.requestQuantity -= retailPriceProduct;
    }

    public boolean isValidQuantity() {
        return requestQuantity > 0;
    }

    private void initializePromotion() {
        if (promotionProduct.isPresent()) {
            Product product = promotionProduct.get();
            this.promotion = Optional.of(product.getPromotion().get());
            return;
        }
        this.promotion = Optional.empty();
    }

    private Product createFromPromotionProduct(){
        if(this.promotionProduct.isEmpty()){
            ExceptionHandler.inputException(NOT_FOUND_PRODUCT);
        }
        Product product = promotionProduct.get();
        return Product.from(product.getName(),
                product.getPrice(),
                0);
    }

}
