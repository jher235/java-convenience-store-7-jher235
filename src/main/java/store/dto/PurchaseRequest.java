package store.dto;

public class PurchaseRequest {

    private String productName;
    private int quantity;

    public PurchaseRequest(String productName, int quantity){
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName(){
        return productName;
    }

    public void increaseQuantity(){
        this.quantity++;
    }
}
