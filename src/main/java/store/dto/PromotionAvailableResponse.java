package store.dto;

public class PromotionAvailableResponse {
    private boolean available;
    private int unavailablePromotionCount;

    private PromotionAvailableResponse(boolean available, int unavailablePromotionCount){
        this.available = available;
        this.unavailablePromotionCount = unavailablePromotionCount;
    }

    public static PromotionAvailableResponse availableResponse(){
        return new PromotionAvailableResponse(true, 0);
    }

    public static PromotionAvailableResponse unavailableResponse(int count){
        return new PromotionAvailableResponse(false, count);
    }


    public boolean isAvailable() {
        return available;
    }

    public int getUnavailablePromotionCount() {
        return unavailablePromotionCount;
    }
}
