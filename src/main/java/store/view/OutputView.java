package store.view;

import store.domain.Promotion;
import store.dto.ProductResponse;
import store.dto.ProductStock;

import java.text.DecimalFormat;
import java.util.Optional;

public class OutputView {
    private static final String START_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n";
    private static final String SOLD_OUT_MESSAGE = "재고 없음";
    private final DecimalFormat MONEY_FORMATTER = new DecimalFormat("###,###");

    public void showStock(ProductStock productStock) {
        StringBuilder stringBuilder = new StringBuilder(START_MESSAGE);
        productStock.getProductResponses().stream()
                .map(this::formatProduct)
                .forEach(stringBuilder::append);

        System.out.println(stringBuilder);
    }

    private String formatProduct(ProductResponse productResponse) {
        return String.format("- %s %s원 %s %s\n",
                productResponse.name(),
                MONEY_FORMATTER.format(productResponse.price()),
                getCount(productResponse.quantity()),
                productResponse.promotion()
        );
    }

    private String getCount(int count) {
        if (count > 0) {
            return String.valueOf(count) + "개";
        }
        return SOLD_OUT_MESSAGE;
    }

}
