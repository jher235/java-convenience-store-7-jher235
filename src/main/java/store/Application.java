package store;

import camp.nextstep.edu.missionutils.Console;
import store.controller.ConvenienceStoreController;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;


public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService();
        ConvenienceStoreController controller = new ConvenienceStoreController(
                inputView, outputView, convenienceStoreService);
        controller.run();
        Console.close();
    }
}
