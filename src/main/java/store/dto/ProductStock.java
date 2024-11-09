package store.dto;

import store.domain.Product;

import java.util.List;

public class ProductStock {
    private List<ProductResponse> productResponses;

    private ProductStock(List<ProductResponse> productResponses){
        this.productResponses = productResponses;
    }

    public static ProductStock from(AllProducts allProducts){
        return new ProductStock(allProducts.products().stream()
                .map(ProductResponse::new)
                .toList());
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}
