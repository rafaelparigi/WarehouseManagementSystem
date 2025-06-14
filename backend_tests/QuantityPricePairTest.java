/**
 * @Rafael
 * @29May
 */

package backend_tests;
import backend.*;
import UI.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuantityPricePairTest
{
    QuantityPricePair pair;
    static final double DELTA = 0.01;
    
    @BeforeEach
    public void setUp()
    {
        pair = new QuantityPricePair(5, 20.0);
    }
    
    @Test
    public void testQuantityPricePairCreation() {
        QuantityPricePair pair = new QuantityPricePair(5, 20.0);

        assertEquals(5, pair.getQuantity());
        assertEquals(20.0, pair.getUnitPrice(), DELTA);
    }

    @Test
    public void testCalculateCost() {
        QuantityPricePair pair = new QuantityPricePair(3, 15.0);
        assertEquals(45.0, pair.calculateCost(), DELTA);
    }
}
