package com.viooh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CheckoutControllerTest {

  private CheckoutController checkoutController;
  private CheckoutService checkoutService;

  @BeforeEach
  void setUp() throws Exception {
    checkoutService = mock(CheckoutService.class);
    checkoutController = new CheckoutController();
  }

  @Test
  void testCheckoutWithNoDiscounts() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item5", 1, 1, new BigDecimal("10.00"))
    );
    when(checkoutService.checkout(checkoutItems)).thenReturn(new BigDecimal("10.00"));

    BigDecimal total = checkoutController.checkout(checkoutItems);
    assertEquals(new BigDecimal("10.00"), total);
  }

  @Test
  void testCheckoutWithMixedItems() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item1", 1, 2, new BigDecimal("6.00")),
        new CheckoutItem("item3", 2, 3, new BigDecimal("4.00"))
    );
    when(checkoutService.checkout(checkoutItems)).thenReturn(new BigDecimal("13.00"));

    BigDecimal total = checkoutController.checkout(checkoutItems);
    assertEquals(new BigDecimal("24.00"), total);
  }

  @Test
  void testCheckoutWithMoreThanTwoEligibleItems() {
    List<CheckoutItem> checkoutItems = List.of(
        new CheckoutItem("item1", 1, 3, new BigDecimal("6.00"))
    );
    when(checkoutService.checkout(checkoutItems)).thenReturn(new BigDecimal("16.00"));

    BigDecimal total = checkoutController.checkout(checkoutItems);
    assertEquals(new BigDecimal("18.00"), total);
  }

  @Test
  void testCheckoutWithEmptyItemList() {
    List<CheckoutItem> checkoutItems = List.of();
    when(checkoutService.checkout(checkoutItems)).thenReturn(BigDecimal.ZERO);

    BigDecimal total = checkoutController.checkout(checkoutItems);
    assertEquals(BigDecimal.ZERO, total);
  }
}
