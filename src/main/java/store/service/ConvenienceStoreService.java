package store.service;

import store.domain.ConvenienceStore;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PurchaseInformation;
import store.dto.PromotionAvailableResponse;
import store.dto.PurchaseRequest;
import store.dto.PurchaseResponse;
import store.dto.PurchaseResult;
import store.exception.ErrorMessage;
import store.exception.ExceptionHandler;

import java.util.List;

public class ConvenienceStoreService {

    public void purchase(ConvenienceStore convenienceStore,
                         List<PurchaseInformation> purchaseInformations,
                         boolean membershipApplied){

        PurchaseResult purchaseResult = new PurchaseResult();
        purchaseInformations.forEach(purchaseInformation ->
                this.purchaseProduct(purchaseInformation, purchaseResult));

        if(membershipApplied){
            applyMembershipDiscount(purchaseResult);
        }

        System.out.println("1: "+purchaseResult.getTotalAmount());
        System.out.println("2: "+purchaseResult.getMembershipDiscountAmount());
        System.out.println("3: "+purchaseResult.getEventDiscountAmount());
        System.out.println("4: "+purchaseResult.getTotalProductQuantity());
        System.out.println("5: "+purchaseResult.getExcludePromotionAmount());



        //1. PurchaseResult라는 클래스 생성 후 작업하며 저장.
        //상품 별 정보. 수량, 금액, 프로모션 증정 수
        //총 금액, 행사 할인 금액. 프로모션 제외 금액.
        //이후 멤버십 할인 금액을 늦게 계산하기.
//        ==============W 편의점================
//        상품명		수량	금액
//        콜라		10 	10,000
//                =============증	정===============
//        콜라		2
//                ====================================
//        총구매액		10	10,000
//        행사할인			-2,000
//        멤버십할인			-0
//        내실돈			 8,000


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
        if(purchaseInformation.isPromotionValid()){
            Promotion promotion = purchaseInformation.getPromotion().get();
            int promotionQuantity = purchaseInformation.getPromotionProduct().get().getQuantity();
            int requestQuantity = purchaseInformation.getRequestQuantity();
            int buy = promotion.buy();
            int get = promotion.get();
            return resultPromotionAvailable(promotionQuantity, requestQuantity, buy, get);
        }
        return PromotionAvailableResponse.availableResponse();
    }

    private void applyMembershipDiscount(PurchaseResult purchaseResult){
        int excludePromotionAmount = purchaseResult.getExcludePromotionAmount();
        int membershipDiscount = (int) Math.round(excludePromotionAmount * (0.3));
        if(membershipDiscount > 8000){
            membershipDiscount = 8000;
        }
        purchaseResult.setMembershipDiscountAmount(membershipDiscount);
    }

    private void purchaseProduct(PurchaseInformation purchaseInformation, PurchaseResult purchaseResult){
        //상품 별 정보 - 이름. 수량, 금액, 프로모션 증정 수
        //총 금액, 총 구매 수량, 행사 할인 금액. 프로모션 제외 금액.
        Product product = purchaseInformation.getProduct();
        int requestQuantity = purchaseInformation.getRequestQuantity();

        addBasicPurchaseInformation(purchaseResult, purchaseInformation);

        if(purchaseInformation.isPromotionValid()){
            Promotion promotion = purchaseInformation.getPromotion().get();
            Product promotionProduct = purchaseInformation.getPromotionProduct().get();
            purchaseWithPromotion(promotion, promotionProduct, purchaseInformation, product, requestQuantity, purchaseResult);
            return;
        }
        purchaseWithoutPromotion(purchaseInformation, requestQuantity, product, purchaseResult);
    }

    private void purchaseWithoutPromotion(PurchaseInformation purchaseInformation, int requestQuantity,
                                          Product product, PurchaseResult purchaseResult){
        addExcludePromotionAmount(purchaseResult, requestQuantity * product.getPrice());

        if(purchaseInformation.getPromotionProduct().isPresent()){
            Product promotionProduct = purchaseInformation.getPromotionProduct().get();
            subtractRequestQuantity(product, promotionProduct, requestQuantity);
            return;
        }
        subtractRequestQuantity(product, requestQuantity);
    }

    private void addBasicPurchaseInformation(PurchaseResult purchaseResult, PurchaseInformation purchaseInformation){
        purchaseResult.addPurchaseResponses(PurchaseResponse.from(purchaseInformation));
        addTotalAmount(purchaseResult, purchaseInformation);
        addTotalProductQuantity(purchaseResult, purchaseInformation);
    }

    private void addTotalAmount(PurchaseResult purchaseResult, PurchaseInformation purchaseInformation){
        int requestQuantity = purchaseInformation.getRequestQuantity();
        int productPrice = purchaseInformation.getProduct().getPrice();
        purchaseResult.addTotalAmount(requestQuantity * productPrice);
    }

    private void addTotalProductQuantity(PurchaseResult purchaseResult, PurchaseInformation purchaseInformation){
        purchaseResult.addTotalQuantity(purchaseInformation.getRequestQuantity());
    }

    private void purchaseWithPromotion(Promotion promotion, Product promotionProduct, PurchaseInformation purchaseInformation,
                                       Product product, int requestQuantity, PurchaseResult purchaseResult){

        int promotionQuantity = promotionProduct.getQuantity();
        int productPrice = product.getPrice();
        int purchasePrice = productPrice * requestQuantity;
        int promotionSet = promotion.buy() + promotion.get();

        if(promotionQuantity >= requestQuantity){
            enoughPromotionQuantity(promotionProduct, requestQuantity, promotionSet, productPrice, purchaseResult);
        }
        if(promotionQuantity < requestQuantity){
            notEnoughPromotionQuantity(product, promotionProduct, requestQuantity, promotionSet,
                    productPrice, promotionQuantity, purchasePrice, purchaseResult);
        }
    }

    private void enoughPromotionQuantity(Product promotionProduct,
                                         int requestQuantity,
                                         int promotionSet,
                                         int productPrice,
                                         PurchaseResult purchaseResult){
        subtractRequestQuantity(promotionProduct, requestQuantity);
        addPromotionItem(purchaseResult, promotionProduct.getName(), requestQuantity, promotionSet);
        int promotionItemCount = requestQuantity/promotionSet;
        int excludePromotionAmount = requestQuantity % promotionSet * productPrice;
        addExcludePromotionAmount(purchaseResult, excludePromotionAmount);
        addEventDiscountAmount(purchaseResult, promotionItemCount, productPrice);
    }

    private void notEnoughPromotionQuantity(Product product,
                                            Product promotionProduct,
                                            int requestQuantity,
                                            int promotionSet,
                                            int productPrice,
                                            int promotionQuantity,
                                            int purchasePrice,
                                            PurchaseResult purchaseResult){
        subtractRequestQuantity(product, promotionProduct, requestQuantity);
        addPromotionItem(purchaseResult, promotionProduct.getName(), promotionQuantity, promotionSet);
        int promotionItemCount = promotionQuantity/promotionSet;
        int excludePromotionAmount = purchasePrice - promotionItemCount * promotionSet * productPrice;
        addExcludePromotionAmount(purchaseResult,excludePromotionAmount);
        addEventDiscountAmount(purchaseResult, promotionItemCount, productPrice);
    }

    private void addEventDiscountAmount(PurchaseResult purchaseResult, int promotionItemCount, int productPrice){
        int eventDiscountAmount = promotionItemCount * productPrice;
        purchaseResult.addEventDiscountAmount(eventDiscountAmount);
    }

    private void addExcludePromotionAmount(PurchaseResult purchaseResult, int excludePromotionAmount){
        purchaseResult.addExcludePromotionAmount(excludePromotionAmount);
    }

    private void addPromotionItem(PurchaseResult purchaseResult, String name,
                                  int quantity, int promotionSet){
        int promotionItemCount = quantity / promotionSet;
        if(promotionItemCount > 0){
            purchaseResult.addPromotionItems(name, promotionItemCount);
        }
    }

    private void subtractRequestQuantity(Product product, Product promotionProduct, int requestQuantity){
        int quantity = promotionProduct.popAllQuantity();
        product.subtractQuantity(requestQuantity - quantity);
    }

    private void subtractRequestQuantity(Product product, int requestQuantity){
        product.subtractQuantity(requestQuantity);
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

    public boolean isAppendPromotionBonus(PurchaseInformation purchaseInformation){
        if(purchaseInformation.isPromotionValid()){
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
