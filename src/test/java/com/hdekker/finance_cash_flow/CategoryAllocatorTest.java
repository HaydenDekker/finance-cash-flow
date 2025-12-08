package com.hdekker.finance_cash_flow;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CategoryAllocatorTest {
    @Test
    void assignsGroceryToFoodCategory() {
        String description = "Grocery store";
        assertEquals("Food & Groceries", CategoryAllocator.allocateCategory(description));
    }
}

class CategoryAllocator {
    static String allocateCategory(String description) {
        return description.toLowerCase().contains("grocery") ? "Food & Groceries" : "Uncategorized";
    }
}