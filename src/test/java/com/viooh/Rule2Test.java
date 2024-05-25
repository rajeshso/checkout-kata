package com.viooh;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Rule2Test {

  @DisplayName("All positive tests for Rule2 calculate discount")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule2_positive_case.csv", numLinesToSkip = 1)
  void testCalculateDiscount_PositiveCases(String checkoutItemsStr, String eligibleItemsStr, String specialPriceStr, String expectedDiscountStr, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    List<String> eligibleItems = Arrays.asList(eligibleItemsStr.split(";"));
    BigDecimal specialPrice = new BigDecimal(specialPriceStr);
    BigDecimal expectedDiscount = new BigDecimal(expectedDiscountStr);

    Rule2 rule2 = new Rule2(eligibleItems, specialPrice);

    BigDecimal discount = rule2.calculateDiscount(checkoutItems);

    assertEquals(expectedDiscount, discount, intent);
  }

  @DisplayName("All negative tests for Rule2 calculate discount")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule2_negative_case.csv", numLinesToSkip = 1)
  void testCalculateDiscount_NegativeCases(String checkoutItemsStr, String eligibleItemsStr, String specialPriceStr, String exceptionMessage, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    List<String> eligibleItems = Arrays.asList(eligibleItemsStr.split(";"));
    BigDecimal specialPrice = new BigDecimal(specialPriceStr);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Rule2 rule2 = new Rule2(eligibleItems, specialPrice);
      rule2.calculateDiscount(checkoutItems);
    });

    assertEquals(exceptionMessage, exception.getMessage(), intent);
  }

  @DisplayName("All positive tests for Rule2 applies")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule2_applies_positive_case.csv", numLinesToSkip = 1)
  void testAppliesPositive(String checkoutItemsStr, String eligibleItemsStr, BigDecimal specialPricePerUnit, boolean expectedResult, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    List<String> eligibleItems = parseEligibleItems(eligibleItemsStr);
    Rule2 rule = new Rule2(eligibleItems, specialPricePerUnit);
    assertTrue(expectedResult);
    assertTrue(rule.applies(checkoutItems), intent);
  }

  @DisplayName("All negative tests for Rule2 applies")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule2_applies_negative_case.csv", numLinesToSkip = 1)
  void testAppliesNegative(String checkoutItemsStr, String eligibleItemsStr, BigDecimal specialPricePerUnit, String exceptionMessage, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    List<String> eligibleItems = parseEligibleItems(eligibleItemsStr);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Rule2 rule = new Rule2(eligibleItems, specialPricePerUnit);
      rule.applies(checkoutItems);
    });

    assertEquals(exceptionMessage, exception.getMessage(), intent);
  }

  private List<CheckoutItem> parseCheckoutItems(String checkoutItemsStr) {
    List<CheckoutItem> items = new ArrayList<>();
    String[] itemArray = checkoutItemsStr.split(";");
    for (String itemStr : itemArray) {
      String[] parts = itemStr.split(":");
      String itemId = parts[0];
      int groupId = Integer.parseInt(parts[1]);
      int quantity = Integer.parseInt(parts[2]);
      BigDecimal unitPrice = new BigDecimal(parts[3]);
      items.add(new CheckoutItem(itemId, groupId, quantity, unitPrice));
    }
    return items;
  }
  private List<String> parseEligibleItems(String str) {
    return Arrays.asList(str.split(";"));
  }
}
