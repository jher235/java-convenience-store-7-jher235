package store.view.constant;

public class ReceiptConstant {

    public static final String RECEIPT_CONVENIENCE_NAME = "==============W 편의점================\n";
    public static final String RECEIPT_COLUMN_NAME = stringFormatWithSpace("상품명", 17)+
            stringFormatWithSpace("수량", 11) + "금액\n";

    public static final String RECEIPT_PRODUCT_FORMAT = "%-17s%-11d%-10s\n";
    public static final String RECEIPT_PROMOTION_ITEM_MESSAGE = "=============증\t\t정===============\n";
    public static final String RECEIPT_PROMOTION_ITEM = "%-17s%-11d\n";
    public static final String RECEIPT_LINE = "====================================\n";
    public static final String TOTAL_PURCHASE_AMOUNT = stringFormatWithSpace("총구매액",17) + "%-11d%s\n";
    public static final String EVENT_DISCOUNT = stringFormatWithSpace("행사할인", 28) + "-%s\n";
    public static final String MEMBERSHIP_DISCOUNT = stringFormatWithSpace("멤버십할인", 27) + "-%s\n";
    public static final String PAYMENT_AMOUNT = stringFormatWithSpace("내실돈", 29) + "%s\n";

    public static String stringFormatWithSpace(String string, int length) {
        return String.format("%-" + length + "s", string);
    }
}
