package store.dto;

import store.domain.Product;
import store.domain.Promotion;

import java.util.Optional;

public record ProductResponse(String name, int price, int quantity, String promotion) {

    ProductResponse(Product product){
        this(product.getName(),
                product.getPrice(),
                product.getQuantity(),
                promotionInformation(product.getPromotion()));
    }

    private static String promotionInformation(Optional<Promotion> promotion){
        return promotion
                .map(Promotion::name)
                .orElse("");
    }
}
