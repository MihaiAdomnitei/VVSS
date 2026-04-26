package drinkshop.ut.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Scenariul ales: (1) V <--- S ---> R și ulterior integrare top-down, breadth first
 * V = ProductValidator
 * R = Repository
 * S = ProductService
 * 
 * Se testează în izolare ProductService (S) utilizând obiecte mock pentru 
 * Validator (V) și Repository (R).
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceMockitoTest {

    @Mock
    private Repository<Integer, Product> mockRepo;

    @Mock
    private ProductValidator mockValidator;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Test updateProduct() - verifica interactiunea cu ambele dependente (Validator si Repo)")
    void testUpdateProductWithValidData() {
        // Arrange
        int targetId = 1;
        String name = "Cola";
        double price = 5.0;
        
        // Nu avem nevoie de stubbing pe mock-uri deoarece metodele apelate (validate, update) returneaza void sau nu ne pasa de return in acest flux (update).
        // Mockito by default returns null for objects, or just does nothing for void methods.
        // Asiguram ca validatorul nu arunca exceptie (comportament default al unui mock pentru o metoda void).

        // Act
        // Assert de stare/comportament de baza: sa nu arunce exceptii
        assertDoesNotThrow(() -> {
            productService.updateProduct(targetId, name, price, CategorieBautura.JUICE, TipBautura.BASIC);
        });

        // Assert: Verifică dacă metoda validate a fost apelata exact o data (verify comportament V)
        verify(mockValidator, times(1)).validate(any(Product.class));

        // Assert: Verifică dacă metoda update din repository a fost apelata exact o data (verify comportament R)
        verify(mockRepo, times(1)).update(any(Product.class));
    }

    @Test
    @DisplayName("Test updateProduct() cand Validator arunca exceptie")
    void testUpdateProductWhenValidatorFails() {
        // Arrange
        int targetId = 2;
        
        // Setam mock-ul validatorului sa arunce exceptie la validare
        doThrow(new IllegalArgumentException("Invalid product")).when(mockValidator).validate(any(Product.class));

        // Act & Assert (Evaluarea starii / fluxului cu assert)
        try {
            productService.updateProduct(targetId, "", -5.0, CategorieBautura.ALL, TipBautura.ALL);
        } catch (IllegalArgumentException ignored) {
            // Este normal sa se arunce exceptie
        }

        // Assert (Evaluarea cu verify):
        // Verificam ca s-a apelat validarea
        verify(mockValidator, times(1)).validate(any(Product.class));
        
        // Verificam ca NU s-a mai ajuns la repository (Repository a ramas neapelata)
        verify(mockRepo, never()).update(any(Product.class));
    }
}
