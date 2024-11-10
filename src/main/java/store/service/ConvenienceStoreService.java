package store.service;

public class ConvenienceStoreService {

    public void purchase(List<PurchaseRequest> purchaseRequests, ConvenienceStore convenienceStore){

//        convenienceStore.findProductsByName()

    }

    public boolean isPossiblePurchase(final PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore){
        List<Product> products = convenienceStore.findProductsByName(purchaseRequest.productName());

        int productCount = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        return productCount >= purchaseRequest.quantity();
    }

    public void isPromotionAvailable(PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore)

}
