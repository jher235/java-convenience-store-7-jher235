package store.controller;

import store.domain.ConvenienceStore;
import store.dto.ProductStock;
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

        ProductStock stock = convenienceStore.getStock();
        outputView.showStock(stock);
        purchase(convenienceStore);


    }

    //출력부를 다 분리해서 작업하고 메서드를 재귀로 처리하는 것고 고려해야할 듯.
    public void purchase(ConvenienceStore convenienceStore){
        List<PurchaseRequest> purchaseRequests = inputView.purchase();
        purchaseRequests.forEach(i-> System.out.println(i.toString()));
        convenienceStoreService.purchase(purchaseRequests, convenienceStore);
    }



    private ConvenienceStore initializeConvenienceStore(){
        try {
            return new ConvenienceStore();
        } catch (IOException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }
}
