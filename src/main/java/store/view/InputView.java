package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.dto.PurchaseRequest;
import store.exception.ErrorMessage;
import store.exception.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputView {
    private final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private final String PURCHASE_PATTERN = "^\\[([가-힣]+)-(\\d+)\\]$";
    private final String DELIMITER = ",";

    public List<PurchaseRequest> purchase() {
        System.out.println(PURCHASE_MESSAGE);
        try {
            String input = readLine();
            String[] purchaseInformation = input.split(DELIMITER);
            List<String> purchaseInformationList = Arrays.asList(purchaseInformation);

            Pattern pattern = Pattern.compile(PURCHASE_PATTERN);

            return parsePurchaseRequest(purchaseInformationList, pattern);

        } catch (IllegalArgumentException e) {
            return purchase();
        }

    }

    private String readLine() {
        while (true) {
            try {
                String input = Console.readLine().trim();
                validInput(input);
                return input;
            } catch (IllegalArgumentException e) {
                ExceptionHandler.inputException(ErrorMessage.INVALID_INPUT);
            }
        }
    }

    private void validInput(String input) throws IllegalArgumentException {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    private List<PurchaseRequest> parsePurchaseRequest(List<String> purchaseInformation, Pattern pattern) {
        return purchaseInformation.stream()
                .map(i -> {
                    Matcher matcher = pattern.matcher(i);

                    if (!matcher.matches()) {
                        ExceptionHandler.inputException(ErrorMessage.INVALID_INPUT_PATTERN);
                        throw new IllegalArgumentException();
                    }
                    System.out.println(matcher.group(1) + matcher.group(2));
                    return new PurchaseRequest(matcher.group(1), Integer.parseInt(matcher.group(2)));
                })
                .toList();
    }

}
