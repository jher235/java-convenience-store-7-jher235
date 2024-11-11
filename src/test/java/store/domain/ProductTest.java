package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product bungeoBbang;
    private Product pizza;
    private Product candy;

    @BeforeEach
    void 테스트_전_프로모션_및_상품_세팅(){
        Promotion promotionInProgress = new Promotion("테스트 프로모션1", 2, 1,
                LocalDate.of(2024,1,1),LocalDate.of(2025,1,1));
        Promotion overedPromotion= new Promotion("테스트 프로모션2", 2, 1,
                LocalDate.of(2023,1,1),LocalDate.of(2024,1,1));

        bungeoBbang = Product.from("붕어빵", 1000, 30);
        pizza = Product.from("피자", 10000, 10, promotionInProgress);
        candy = Product.from("사탕", 300, 20, overedPromotion);
    }

    @Test
    void isPromotionProduct() {
        assertEquals(false, bungeoBbang.isPromotionProduct());
        assertEquals(true, pizza.isPromotionProduct());
        assertEquals(true, candy.isPromotionProduct());
    }

    @Test
    void 프로모션이_이미_지난_경우() {
        Promotion promotion = candy.getPromotion().get();
        assertEquals(false, promotion.isPromotionPeriod());
    }

    @Test
    void popAllQuantity() {
        int bungeoBbangStock =  bungeoBbang.getQuantity();
        assertEquals(bungeoBbangStock, bungeoBbang.popAllQuantity());
        assertEquals(0, bungeoBbang.getQuantity());
    }
}