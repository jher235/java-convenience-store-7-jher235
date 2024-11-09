package store.exception;

public enum ErrorMessage {


    NOT_FOUND_PROMOTION("프로모션을 찾을 수 없습니다."),
    INITIALIZE_FROM_FILE_ERROR("해당 파일 데이터 형식을 사용할 수 없습니다.")
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
