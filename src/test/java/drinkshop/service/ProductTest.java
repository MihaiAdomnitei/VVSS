package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("P231-95, P231-96, P231-97 - ProductTest ECP & BVA Valid")
public class ProductTest {

    private ProductService productService;

    static class DummyProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    @BeforeEach
    void setUp() {
        productService = new ProductService(new DummyProductRepo(), new ProductValidator());
    }

    @Test
    @DisplayName("P231-95: ECP - Valid - Adaugare produs cu toate campurile valide")
    void addProduct_ECP_Valid() {
        Product p = new Product(100, "Cola", 7.5, CategorieBautura.JUICE, TipBautura.BASIC);
        assertDoesNotThrow(() -> {
            productService.addProduct(p);
        });
        
        assertNotNull(productService.findById(100));
    }

    @Test
    @DisplayName("P231-96: ECP - Invalid - Adaugare produs cu nume gol")
    void addProduct_ECP_Invalid() {
        Product p = new Product(101, "", 7.5, CategorieBautura.JUICE, TipBautura.BASIC);
        assertThrows(ValidationException.class, () -> {
            productService.addProduct(p);
        });
    }

    @Test
    @DisplayName("P231-97: BVA - Valid - Adaugare produs cu nume valid")
    void addProduct_BVAValid() {
        Product p = new Product(102, "A", 1.0, CategorieBautura.ALL, TipBautura.ALL);
        assertDoesNotThrow(() -> {
            productService.addProduct(p);
        });
        
        assertNotNull(productService.findById(102));
    }
}
