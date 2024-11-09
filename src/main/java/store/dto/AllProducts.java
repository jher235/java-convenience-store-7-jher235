package store.dto;

import store.domain.Product;

import java.util.List;

public record AllProducts(List<Product> products) {
}
