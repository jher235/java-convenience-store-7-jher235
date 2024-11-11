package store.dto;

import store.domain.Product;
import store.domain.PurchaseInformation;

public class PurchaseResponse {
    private String productName;
    private int quantity;
    private int amount;

    private PurchaseResponse(String productName, int quantity, int amount){
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
    }

    public static PurchaseResponse from(PurchaseInformation purchaseInformation){
        Product product = purchaseInformation.getProduct();
        int purchaseAmount = purchaseInformation.getRequestQuantity() * product.getPrice();
        return new PurchaseResponse(purchaseInformation.getProductName(),
                purchaseInformation.getRequestQuantity(), purchaseAmount);
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmount() {
        return amount;
    }
}
