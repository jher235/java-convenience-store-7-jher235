package store.domain;

import store.exception.ErrorMessage;
import store.exception.ExceptionHandler;

import java.util.Optional;

public class Product {

    private final String name;
    private final int price;
    private int quantity;
    private Optional<Promotion> promotion;

    public static Product from(String name, int price, int quantity) {
        return new Product(name, price, quantity, Optional.empty());
    }

    public static Product from(String name, int price, int quantity, Promotion promotion) {
        return new Product(name, price, quantity, Optional.of(promotion));
    }

    protected Product(String name, int price, int quantity, Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<Promotion> getPromotion() {
        return this.promotion;
    }

    public boolean isPromotionProduct() {
        return this.promotion.isPresent();
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            return;
        }
        ExceptionHandler.inputException(ErrorMessage.OUT_OF_STOCK);
    }

    public int popAllQuantity() {
        int productQuantity = this.quantity;
        this.quantity = 0;
        return productQuantity;
    }
}
