package store.view;

import store.domain.Promotion;
import store.dto.ProductResponse;
import store.dto.ProductStock;

import java.text.DecimalFormat;
import java.util.Optional;

public class OutputView {
    private final String START_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n";
    private final String SOLD_OUT_MESSAGE = "재고 없음";
    private final DecimalFormat MONEY_FORMATTER = new DecimalFormat("###,###");
    private final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private final String AVAILABLE_PROMOTION_BONUS = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private final String UNEXPECTED_PROMOTION_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private final String ASKING_MEMBERSHIP_APPLY = "멤버십 할인을 받으시겠습니까? (Y/N)";

    public void showStock(ProductStock productStock) {
        StringBuilder stringBuilder = new StringBuilder(START_MESSAGE);
        productStock.getProductResponses().stream()
                .map(this::formatProduct)
                .forEach(stringBuilder::append);

        System.out.println(stringBuilder);
    }

    public void printAskingMembership(){
        System.out.println(ASKING_MEMBERSHIP_APPLY);
    }

    public void printPurchaseStartMessage(){
        System.out.println(PURCHASE_MESSAGE);
    }

    public void printAvailablePromotionBonus(String productName){
        String message = String.format(AVAILABLE_PROMOTION_BONUS, productName);
        System.out.println(message);
    }

    public void printUnExpectedPromotion(String productName, int quantity){
        String message = String.format(UNEXPECTED_PROMOTION_MESSAGE, productName, quantity);
        System.out.println(message);
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
