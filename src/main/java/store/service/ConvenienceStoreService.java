package store.service;

import store.domain.ConvenienceStore;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PurchaseInformation;
import store.dto.PromotionAvailableResponse;
import store.dto.PurchaseRequest;
import store.exception.ErrorMessage;
import store.exception.ExceptionHandler;

import java.util.List;
import java.util.Optional;

public class ConvenienceStoreService {

    public void purchase(List<PurchaseRequest> purchaseRequests, ConvenienceStore convenienceStore){

//        convenienceStore.findProductsByName()

    }

    public boolean isPossiblePurchase(final PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore){
        List<Product> products = convenienceStore.findProductsByName(purchaseRequest.getProductName());

        int productCount = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        return productCount >= purchaseRequest.getQuantity();
    }

    public PurchaseInformation mapPurchaseInformation(final PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore){
        List<Product> products = convenienceStore.findProductsByName(purchaseRequest.getProductName());
        validRequestAmount(products, purchaseRequest);
        return new PurchaseInformation(products, purchaseRequest);
    }


    public PromotionAvailableResponse isPromotionAvailable(PurchaseInformation purchaseInformation){
        int purchaseQuantity = purchaseInformation.getRequestQuantity();
        if(purchaseInformation.isPromotionInvalid()){
            Promotion promotion = purchaseInformation.getPromotion().get();
            int promotionQuantity = purchaseInformation.getPromotionProduct().get().getQuantity();
            int requestQuantity = purchaseInformation.getRequestQuantity();
            int buy = promotion.buy();
            int get = promotion.get();
            return resultPromotionAvailable(promotionQuantity, requestQuantity, buy, get);
        }
        return PromotionAvailableResponse.availableResponse();
    }

    private PromotionAvailableResponse resultPromotionAvailable(int promotionQuantity,
                                                                int requestQuantity,
                                                                int buy,
                                                                int get){
        int promotionSet = buy + get;
        if( requestQuantity<=promotionQuantity && requestQuantity % (promotionSet) == 0){
            return PromotionAvailableResponse.availableResponse();
        }

        int requiredPromotionSet = requestQuantity / (promotionSet);
        int purchasePromotionProduct = requiredPromotionSet * (promotionSet);
        return PromotionAvailableResponse.unavailableResponse(requestQuantity - purchasePromotionProduct);
    }

    private void validRequestAmount(List<Product> products, PurchaseRequest purchaseRequest){
        int productCount = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        if(productCount < purchaseRequest.getQuantity()){
            ExceptionHandler.inputException(ErrorMessage.OUT_OF_STOCK);
        }
    }

    public boolean isOmitPromotionBonus(PurchaseInformation purchaseInformation){
        if(purchaseInformation.isPromotionInvalid()){
            int purchaseQuantity = purchaseInformation.getRequestQuantity();
            int promotionQuantity = purchaseInformation.getPromotionProduct().get().getQuantity();
            Promotion promotion = purchaseInformation.getPromotion().get();
            int buy = promotion.buy();
            int get = promotion.get();
            return purchaseQuantity < promotionQuantity && purchaseQuantity %(buy+get) == buy;
        }
        return false;
    }

}
