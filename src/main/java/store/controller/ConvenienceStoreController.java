package store.controller;

import store.domain.ConvenienceStore;
import store.domain.PurchaseInformation;
import store.dto.ProductStock;
import store.dto.PromotionAvailableResponse;
import store.dto.PurchaseRequest;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;

import static store.exception.ErrorMessage.INITIALIZE_FROM_FILE_ERROR;

public class ConvenienceStoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStoreService convenienceStoreService;

    public ConvenienceStoreController(InputView inputView, OutputView outputView, ConvenienceStoreService convenienceStoreService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStoreService = convenienceStoreService;
    }


    public void run(){
        ConvenienceStore convenienceStore = initializeConvenienceStore();
        while (true){
            ProductStock stock = convenienceStore.getStock();
            outputView.showStock(stock);
            List<PurchaseInformation> purchaseInformations = getPurchaseInformation(convenienceStore);
            purchase(convenienceStore, purchaseInformations);
        }

    }

    //출력부를 다 분리해서 작업하고 메서드를 재귀로 처리하는 것고 고려해야할 듯.
    public void purchase(ConvenienceStore convenienceStore, List<PurchaseInformation> purchaseInformations){
        purchaseConfirmed(purchaseInformations);
        boolean applyMembership = isMembershipApplied();

    }

    private boolean isMembershipApplied(){
        outputView.printAskingMembership();
        return inputView.readYesOrNo();
    }

    private List<PurchaseInformation> getPurchaseInformation(ConvenienceStore convenienceStore){
        outputView.printPurchaseStartMessage();
        while (true){
            try {
                List<PurchaseRequest> purchaseRequests = inputView.purchase();
                return purchaseRequests.stream()
                        .map(purchaseRequest -> mapPurchaseInformation(purchaseRequest, convenienceStore)).toList();
            }catch (IllegalArgumentException ignored){
            }
        }
    }


    private ConvenienceStore initializeConvenienceStore(){
        try {
            return new ConvenienceStore();
        } catch (IOException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }

    private PurchaseInformation mapPurchaseInformation(PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore){
        return convenienceStoreService.mapPurchaseInformation(purchaseRequest, convenienceStore);
    }

    private void purchaseConfirmed(List<PurchaseInformation> purchaseInformations){
        purchaseInformations.forEach(this::announcePromotion);
        purchaseInformations.forEach(this::checkPromotionApply);
    }

    /**
     * ConvenienceStoreService 가 View 에 의존성을 갖지 않도록 하기 위해 Controller 에서 처리.
     */
    private void checkPromotionApply (PurchaseInformation purchaseInformation){
        PromotionAvailableResponse response = convenienceStoreService.isPromotionAvailable(purchaseInformation);
        if(!response.isAvailable()){
            int retailPriceProduct = response.getUnavailablePromotionCount();
            outputView.printUnExpectedPromotion(purchaseInformation.getProductName(), response.getUnavailablePromotionCount());
            if(inputView.readYesOrNo()){
                return ;
            }
            purchaseInformation.subtractRetailPriceProduct(retailPriceProduct);
        }
    }

    private void announcePromotion(PurchaseInformation purchaseInformation){
        if(convenienceStoreService.isOmitPromotionBonus(purchaseInformation)){
            outputView.printAvailablePromotionBonus(purchaseInformation.getProductName());
            if(inputView.readYesOrNo()){
                purchaseInformation.increaseQuantity();
            }
        }
    }
}
