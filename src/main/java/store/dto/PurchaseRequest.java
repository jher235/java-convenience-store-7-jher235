package store.dto;

import java.util.stream.Stream;

public record PurchaseRequest(String productName, int count) {
}
