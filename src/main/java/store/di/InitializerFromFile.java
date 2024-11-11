package store.di;

import store.domain.Product;
import store.domain.Promotion;
import store.dto.AllProducts;
import store.exception.ErrorMessage;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static store.exception.ErrorMessage.INITIALIZE_FROM_FILE_ERROR;

public class InitializerFromFile {

    private static final String PROMOTION_DATA_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_DATA_PATH = "src/main/resources/products.md";
    private static final String DELIMITER = ",";
    private static final String NA_VALUE = "null";
    private static final List<String> PROMOTION_COLUMN = Arrays.asList("name", "buy", "get", "start_date", "end_date");
    private static final List<String> PRODUCT_COLUMN = Arrays.asList("name", "price", "quantity", "promotion");

    public List<Promotion> initializePromotion() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(PROMOTION_DATA_PATH))) {
            String[] columns = br.readLine().split(DELIMITER);
            validateColumn(PROMOTION_COLUMN, Arrays.asList(columns));

            return parsePromotionsFrom(br);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }

    public AllProducts initializeAllProduct(List<Promotion> promotions) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_DATA_PATH))) {
            String[] columns = br.readLine().split(DELIMITER);
            validateColumn(PRODUCT_COLUMN, Arrays.asList(columns));

            return parseAllProductsFrom(br, promotions);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
        }
    }

    private List<Promotion> parsePromotionsFrom(BufferedReader bufferedReader) throws IOException {
        List<Promotion> promotions = new LinkedList<>();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            registerPromotion(promotions, line);
        }
        return promotions;
    }

    private void registerPromotion(List<Promotion> promotions, String line){
        String[] rowData = line.split(DELIMITER);
        String name = rowData[0];
        int buy = Integer.parseInt(rowData[1]);
        int get = Integer.parseInt(rowData[2]);
        LocalDate startDate = LocalDate.parse(rowData[3]);
        LocalDate endDate = LocalDate.parse(rowData[4]);

        promotions.add(new Promotion(name, buy, get, startDate, endDate));
    }

    private void validateColumn(List<String> column, List<String> parsedColumn) {
        if (column.equals(parsedColumn)) {
            return;
        }
        throw new IllegalArgumentException(INITIALIZE_FROM_FILE_ERROR.getMessage());
    }

    private AllProducts parseAllProductsFrom(BufferedReader bufferedReader, List<Promotion> promotions) throws IOException {
        List<Product> products = new LinkedList<>();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
           addProduct(products, line, promotions);
        }
        return new AllProducts(products);
    }

    private void addProduct(List<Product> products, String line, List<Promotion> promotions){
        String[] rowData = line.split(DELIMITER);
        String name = rowData[0];
        int price = Integer.parseInt(rowData[1]);
        int quantity = Integer.parseInt(rowData[2]);
        String promotionName = rowData[3];
        if (promotionName.equals(NA_VALUE)) {
            addBasicProduct(products, name, price, quantity);
            return;
        }
        addPromotionProduct(products, name, price, quantity, promotionName, promotions);
    }

    private void addBasicProduct(List<Product> products, String name, int price, int quantity){
        products.add(Product.from(name, price, quantity));
    }

    private void addPromotionProduct(List<Product> products, String name, int price,
                                     int quantity, String promotionName, List<Promotion> promotions){
        Promotion promotion = findPromotionByPromotionName(promotionName, promotions);
        products.add(Product.from(name, price, quantity, promotion));
    }


    private Promotion findPromotionByPromotionName(String promotionName, List<Promotion> promotions) {
        Optional<Promotion> promotion = promotions.stream()
                .filter(p -> p.name().equals(promotionName))
                .findAny();
        return promotion.orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.NOT_FOUND_PROMOTION.getMessage()));
    }
}
