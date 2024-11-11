package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

public record Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {

    public boolean isPromotionPeriod() {
        return !startDate.isAfter(ChronoLocalDate.from(DateTimes.now())) &&
                !endDate.isBefore(ChronoLocalDate.from(DateTimes.now()));
    }

}
