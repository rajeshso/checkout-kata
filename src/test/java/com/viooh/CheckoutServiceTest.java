package com.viooh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutServiceTest {

  private CheckoutService checkoutService;
  private DiscountRuleCalculator rule2SpecialPriceRuleCalculator;

  @BeforeEach
  void setUp() {
    rule2SpecialPriceRuleCalculator = new Rule2SpecialPriceRuleCalculator(
        List.of("item1", "item2"), new BigDecimal("5.00")
    );
    DiscountRuleCalculator rule1BuyXPayYRuleCalculator = new Rule1BuyXPayYRuleCalculator(
        List.of("item3", "item4")
    );
    checkoutService = new CheckoutService(List.of(rule2SpecialPriceRuleCalculator,
        rule1BuyXPayYRuleCalculator));
  }

  @Test
  void testCheckoutWithNoDiscounts() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item5", 1, 1, new BigDecimal("10.00"))
    );
    BigDecimal total = checkoutService.checkout(checkoutItems);
    assertEquals(new BigDecimal("10.00"), total);
  }

  @Test
  void testCheckoutWithSpecialPriceRule() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item1", 1, 2, new BigDecimal("6.00"))
    );
    BigDecimal total = checkoutService.checkout(checkoutItems);
    // Original price would be 2 * 6.00 = 12.00, special price for 2 items is 5.00 each, 2 * 5.00 = 10.00
    assertEquals(new BigDecimal("10.00"), total);
  }

  @Test
  void testCheckoutWithBuyXPayYRule() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item3", 1, 3, new BigDecimal("6.00"))
    );
    BigDecimal total = checkoutService.checkout(checkoutItems);
    // Original price: 3 * 6.00 = 18.00, special price for 3 items is 2 * 6.00 = 12.00
    assertEquals(new BigDecimal("12.00"), total);
  }

  @Test
  void testCheckoutWithMixedItems() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item1", 1, 2, new BigDecimal("6.00")),
        new CheckoutItem("item3", 2, 3, new BigDecimal("6.00")),
        new CheckoutItem("item5", 3, 1, new BigDecimal("10.00"))
    );
    BigDecimal total = checkoutService.checkout(checkoutItems);
    // Discounted item1 price: 2 * 5.00 = 10.00
    // Discounted item3 price: 2 * 6.00 = 12.00 (Buy 3, Pay for 2)
    // Item5 price: 10.00
    // Total: 10.00 + 12.00 + 10.00 = 32.00
    assertEquals(new BigDecimal("32.00"), total);
  }

  @Test
  void testCheckoutWithEmptyItemList() {
    List<CheckoutItem> checkoutItems = List.of();
    BigDecimal total = checkoutService.checkout(checkoutItems);
    assertEquals(BigDecimal.ZERO, total);
  }

  @Test
  void testCheckoutWithInvalidItemForRule() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item5", 1, 2, new BigDecimal("6.00"))
    );

    assertThrows(IllegalArgumentException.class, () -> rule2SpecialPriceRuleCalculator.applies(checkoutItems));
  }

  @Test
  void testCheckoutWithIneligibleUnitPrices() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item1", 1, 1, new BigDecimal("6.00")),
        new CheckoutItem("item1", 1, 1, new BigDecimal("7.00"))
    );

    assertThrows(IllegalArgumentException.class, () -> rule2SpecialPriceRuleCalculator.applies(checkoutItems));
  }
}
