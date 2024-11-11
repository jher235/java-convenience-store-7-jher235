package store.dto;

import store.domain.Promotion;

import java.util.*;

public class PurchaseResult {
    private List<PurchaseResponse> purchaseResponses;
    private Map<String, Integer> promotionItems;
    private int totalAmount;
    private int totalProductQuantity;
    private int eventDiscountAmount;
    private int membershipDiscountAmount;
    private int excludePromotionAmount;

    public PurchaseResult(){
        purchaseResponses = new LinkedList<>();
        promotionItems = new LinkedHashMap<>();
        totalAmount = 0;
        totalProductQuantity = 0;
        eventDiscountAmount = 0;
        membershipDiscountAmount = 0;
        excludePromotionAmount = 0;
    }

    public void setMembershipDiscountAmount(int membershipDiscountAmount){
        this.membershipDiscountAmount = - membershipDiscountAmount;
    }

    public void addTotalAmount(int amount){
        this.totalAmount += amount;
    }

    public void addTotalQuantity(int quantity){
        this.totalProductQuantity += quantity;
    }

    public void addEventDiscountAmount(int eventDiscountAmount){
        this.eventDiscountAmount -= eventDiscountAmount;
    }
    public void addExcludePromotionAmount(int excludePromotionAmount){
        this.excludePromotionAmount += excludePromotionAmount;
    }

    public void addPurchaseResponses(PurchaseResponse purchaseResponse){
        this.purchaseResponses.add(purchaseResponse);
    }
    public void addPromotionItems(String name, int quantity){
        if(!promotionItems.containsKey(name)){
            this.promotionItems.put(name, quantity);
            return;
        }
        int preQuantity = this.promotionItems.get(name);
        this.promotionItems.put(name, preQuantity+quantity);
    }

    public List<PurchaseResponse> getPurchaseResponses() {
        return purchaseResponses;
    }

    public Map<String, Integer> getPromotionItems() {
        return promotionItems;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalProductQuantity() {
        return totalProductQuantity;
    }

    public int getEventDiscountAmount() {
        return eventDiscountAmount;
    }

    public int getMembershipDiscountAmount() {
        return membershipDiscountAmount;
    }

    public int getExcludePromotionAmount() {
        return excludePromotionAmount;
    }
}
