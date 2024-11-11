package store.controller;

import store.domain.ConvenienceStore;
import store.domain.PurchaseInformation;
import store.dto.ProductStock;
import store.dto.PromotionAvailableResponse;
import store.dto.PurchaseRequest;
import store.dto.PurchaseResult;
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


    public void run() {
        ConvenienceStore convenienceStore = initializeConvenienceStore();
        while (true) {
            outputView.showStock(convenienceStore.getStock());
            List<PurchaseInformation> purchaseInformations = getPurchaseInformation(convenienceStore);
            PurchaseResult purchaseResult = purchase(convenienceStore, purchaseInformations);
            if (!displayResultAndAskContinue(purchaseResult)) {
                break;
            }
        }
    }

    private boolean displayResultAndAskContinue(PurchaseResult purchaseResult) {
        printResult(purchaseResult);
        return isContinue();
    }

    private void printResult(PurchaseResult purchaseResult) {
        if (!purchaseResult.getPurchaseResponses().isEmpty()) {
            outputView.printReceipt(purchaseResult);
        }
    }

    public PurchaseResult purchase(ConvenienceStore convenienceStore, List<PurchaseInformation> purchaseInformations) {
        purchaseInformations = purchaseConfirmed(purchaseInformations);
        if (purchaseInformations.isEmpty()) {
            return new PurchaseResult();
        }
        boolean membershipApplied = isMembershipApplied();
        return convenienceStoreService.purchase(convenienceStore, purchaseInformations, membershipApplied);
    }

    private boolean isContinue() {
        outputView.printContinueMessage();
        return inputView.readYesOrNo();
    }

    private boolean isMembershipApplied() {
        outputView.printAskingMembership();
        return inputView.readYesOrNo();
    }

    private List<PurchaseInformation> getPurchaseInformation(ConvenienceStore convenienceStore) {
        outputView.printPurchaseStartMessage();
        while (true) {
            try {
                List<PurchaseRequest> purchaseRequests = inputView.purchase();
                return purchaseRequests.stream()
                        .map(purchaseRequest -> mapPurchaseInformation(purchaseRequest, convenienceStore)).toList();
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private ConvenienceStore initializeConvenienceStore() {
        try {
            return new ConvenienceStore();
        } catch (IOException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }

    private PurchaseInformation mapPurchaseInformation(PurchaseRequest purchaseRequest, ConvenienceStore convenienceStore) {
        return convenienceStoreService.mapPurchaseInformation(purchaseRequest, convenienceStore);
    }

    private List<PurchaseInformation> purchaseConfirmed(List<PurchaseInformation> purchaseInformations) {
        purchaseInformations.forEach(this::announcePromotion);
        purchaseInformations.forEach(this::checkPromotionApply);
        return purchaseInformations.stream()
                .filter(PurchaseInformation::isValidQuantity)
                .toList();
    }

    /**
     * ConvenienceStoreService 가 View 에 의존성을 갖지 않도록 하기 위해 Controller 에서 처리.
     */
    private void checkPromotionApply(PurchaseInformation purchaseInformation) {
        PromotionAvailableResponse response = convenienceStoreService.isPromotionAvailable(purchaseInformation);
        if (!response.isAvailable()) {
            int retailPriceProduct = response.getUnavailablePromotionCount();
            outputView.printUnExpectedPromotion(purchaseInformation.getProductName(), response.getUnavailablePromotionCount());
            if (inputView.readYesOrNo()) {
                return;
            }
            purchaseInformation.subtractRetailPriceProduct(retailPriceProduct);
        }
    }

    private void announcePromotion(PurchaseInformation purchaseInformation) {
        if (convenienceStoreService.isAppendPromotionBonus(purchaseInformation)) {
            outputView.printAvailablePromotionBonus(purchaseInformation.getProductName());
            if (inputView.readYesOrNo()) {
                purchaseInformation.increaseQuantity();
            }
        }
    }
}
