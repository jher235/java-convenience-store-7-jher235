package store.controller;

import store.domain.ConvenienceStore;
import store.dto.ProductStock;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;

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
//        while (true){
//
//        }
    }



    private ConvenienceStore initializeConvenienceStore(){
        try {
            return new ConvenienceStore();
        } catch (IOException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }
}
