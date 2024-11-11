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

    private final String PURCHASE_PATTERN = "^\\[([가-힣]+)-(\\d+)\\]$";
    private final String DELIMITER = ",";

    private final String YES = "Y";
    private final String NO = "N";

    public List<PurchaseRequest> purchase() {
        try {
            String input = readLine();
            List<String> purchaseInformationList = Arrays.stream(input.split(DELIMITER))
                    .map(String::trim)
                    .toList();
            Pattern pattern = Pattern.compile(PURCHASE_PATTERN);
            return parsePurchaseRequest(purchaseInformationList, pattern);
        } catch (IllegalArgumentException e) {
            return purchase();
        }
    }

    public boolean readYesOrNo() {
        while (true) {
            try {
                String input = readLine();
                if (input.equals(YES)) return true;
                if (input.equals(NO)) return false;
                ExceptionHandler.inputException(ErrorMessage.INVALID_INPUT);
            } catch (IllegalArgumentException ignore) {
            }
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
                    }
                    return new PurchaseRequest(matcher.group(1), Integer.parseInt(matcher.group(2)));
                })
                .toList();
    }
}
