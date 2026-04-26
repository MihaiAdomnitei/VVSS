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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste ECP si BVA pentru ProductService.updateProduct()")
class ProductServiceTest {

    private ProductService productService;

    // Dummy repository (clasa anonima / interna) pentru izolare, conf. design patterns & lab rules
    static class DummyProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    @BeforeEach
    void setUp() {
        DummyProductRepo repo = new DummyProductRepo();
        productService = new ProductService(repo);
        
        // Arrange - Adaugam un produs existent in repository pentru a avea ce face update
        Product p = new Product(1, "Apa", 5.0, CategorieBautura.ALL, TipBautura.ALL);
        repo.save(p);
    }

    // ==========================================
    // ECP - Equivalence Class Partitioning
    // ==========================================

    @Test
    @DisplayName("ECP TC1: Date valide -> Success")
    void testUpdateProduct_ECP_Valid() {
        // Arrange
        int id = 1;
        String name = "Bere Neagra";
        double price = 12.5;

        // Act
        // Sablon AAA (Arrange, Act, Assert)
        assertDoesNotThrow(() -> {
            productService.updateProduct(id, name, price, CategorieBautura.ALL, TipBautura.ALL);
        });

        // Assert
        Product updated = productService.findById(id);
        assertNotNull(updated, "Produsul ar fi trebuit gasit!");
        assertEquals(price, updated.getPret());
        assertEquals(name, updated.getNume());
    }

    @ParameterizedTest(name = "ECP Invalide: id={0}, price={1}")
    @CsvSource({
            "-1, 10.0",   // TC2: Id Invalid, Pret Valid
            "1, -5.0",    // TC3: Id Valid, Pret Invalid
            "-2, -5.0"    // TC4: Id Invalid, Pret Invalid
    })
    @DisplayName("ECP TC2, TC3, TC4: Parametri Invalizi -> Arunca ValidationException")
    void testUpdateProduct_ECP_Invalid(int id, double price) {
        // Arrange (Parametri injectati de CsvSource)
        String name = "Test";

        // Act & Assert
        Exception exception = assertThrows(ValidationException.class, () -> {
            productService.updateProduct(id, name, price, CategorieBautura.ALL, TipBautura.ALL);
        });

        // Verifica ca exceptia are un mesaj asociat
        assertNotNull(exception.getMessage());
    }

    // ==========================================
    // BVA - Boundary Value Analysis
    // ==========================================

    @ParameterizedTest(name = "BVA Valid ID Boundaries: id={0}")
    @ValueSource(ints = {1, 2, Integer.MAX_VALUE})
    @DisplayName("BVA Valide (frontiere pt id)")
    void testUpdateProduct_BVA_ValidId(int id) {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            productService.updateProduct(id, "BVA Test", 10.0, CategorieBautura.ALL, TipBautura.ALL);
        });
    }

    @Test
    @DisplayName("BVA Invalid ID Boundary (id=0)")
    void testUpdateProduct_BVA_InvalidId0() {
        assertThrows(ValidationException.class, () -> {
            productService.updateProduct(0, "BVA Test", 10.0, CategorieBautura.ALL, TipBautura.ALL);
        });
    }

    @ParameterizedTest(name = "BVA Valid Price: price={0}")
    @ValueSource(doubles = {0.01, 1000.0})
    @DisplayName("BVA Valide (frontiere pt pret)")
    void testUpdateProduct_BVA_ValidPrice(double price) {
        assertDoesNotThrow(() -> {
            productService.updateProduct(5, "BVA Pret Valid", price, CategorieBautura.ALL, TipBautura.ALL);
        });
    }

    @Test
    @DisplayName("BVA Invalid Price Boundary (price=0.0)")
    void testUpdateProduct_BVA_InvalidPrice0() {
        assertThrows(ValidationException.class, () -> {
            productService.updateProduct(5, "BVA Pret Invalid", 0.0, CategorieBautura.ALL, TipBautura.ALL);
        });
    }
}
