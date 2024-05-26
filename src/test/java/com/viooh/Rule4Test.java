package com.viooh;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Rule4Test {

  private List<CheckoutItem> parseCheckoutItems(String checkoutItemsStr) {
    return Arrays.stream(checkoutItemsStr.split(";"))
        .map(itemStr -> {
          String[] parts = itemStr.split(":");
          return new CheckoutItem(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), new BigDecimal(parts[3]));
        })
        .collect(Collectors.toList());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/rule4_applies_positive_case.csv", numLinesToSkip = 1)
  void testAppliesPositive(String checkoutItemsStr, String eligibleBuyItemX, int eligibleBuyItemXQuantityN, String eligibleFreeItemY, int eligibleFreeItemYQuantityK, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Rule4 rule = new Rule4(eligibleBuyItemX, eligibleBuyItemXQuantityN, eligibleFreeItemY, eligibleFreeItemYQuantityK);
    boolean actualResult = rule.applies(checkoutItems);
    assertTrue(actualResult, intent);
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/rule4_applies_negative_case.csv", numLinesToSkip = 1)
  void testAppliesNegative(String checkoutItemsStr, String eligibleBuyItemX, int eligibleBuyItemXQuantityN, String eligibleFreeItemY, int eligibleFreeItemYQuantityK, String exceptionMessage, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Rule4 rule = new Rule4(eligibleBuyItemX, eligibleBuyItemXQuantityN, eligibleFreeItemY, eligibleFreeItemYQuantityK);
    boolean actualResult = rule.applies(checkoutItems);
    assertFalse(actualResult, exceptionMessage + " - " + intent);
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/rule4_calculate_discount_case.csv", numLinesToSkip = 1)
  void testCalculateDiscount(String checkoutItemsStr, String eligibleBuyItemX, int eligibleBuyItemXQuantityN, String eligibleFreeItemY, int eligibleFreeItemYQuantityK, BigDecimal expectedDiscount, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Rule4 rule = new Rule4(eligibleBuyItemX, eligibleBuyItemXQuantityN, eligibleFreeItemY, eligibleFreeItemYQuantityK);
    BigDecimal actualDiscount = rule.calculateDiscount(checkoutItems);
    assertEquals(expectedDiscount, actualDiscount, intent);
  }
}
