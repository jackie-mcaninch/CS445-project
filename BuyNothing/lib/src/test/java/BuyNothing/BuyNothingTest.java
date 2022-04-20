package BuyNothing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iit.cs445.spring2022.buynothing.Account;

public class BuyNothingTest {
    Account l;

    @BeforeEach
    public void setUp() throws Exception {
        l = new Account();
    }

    @Test
    public void test_whatever() {
        assertFalse(false);
    }
}

