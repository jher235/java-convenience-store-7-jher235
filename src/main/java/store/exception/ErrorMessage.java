package store.exception;

public enum ErrorMessage {


    NOT_FOUND_PROMOTION("프로모션을 찾을 수 없습니다."),
    INITIALIZE_FROM_FILE_ERROR("해당 파일 데이터 형식을 사용할 수 없습니다."),
    INVALID_INPUT_PATTERN("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    OUT_OF_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
    ;

    private static final String HEADER_MESSAGE = "[ERROR] ";


    private final String message;

    ErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return HEADER_MESSAGE + message;
    }
}
