package com.viooh;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class Rule3CheapestFreeInGroupRuleCalculatorTest {

  @DisplayName("All positive tests for Rule3 applies")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule3_applies_positive_case.csv", numLinesToSkip = 1)
  void testAppliesPositive(String checkoutItemsStr, String eligibleGroupStr, boolean expectedResult, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Set<Integer> eligibleGroup = parseEligibleGroup(eligibleGroupStr);
    Rule3CheapestFreeInGroupRuleCalculator rule = new Rule3CheapestFreeInGroupRuleCalculator(eligibleGroup);

    assertEquals(expectedResult, rule.applies(checkoutItems), intent);
  }

  @DisplayName("All negative tests for Rule3 applies")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule3_applies_negative_case.csv", numLinesToSkip = 1)
  void testAppliesNegative(String checkoutItemsStr, String eligibleGroupStr, String exceptionMessage, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Set<Integer> eligibleGroup = parseEligibleGroup(eligibleGroupStr);
    Rule3CheapestFreeInGroupRuleCalculator rule = new Rule3CheapestFreeInGroupRuleCalculator(eligibleGroup);
    boolean actualResult = rule.applies(checkoutItems);
    assertFalse(actualResult, exceptionMessage+" - "+intent);
  }

  @DisplayName("All positive tests to calculate discount for Rule3")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule3_calculate_discount_positive_case.csv", numLinesToSkip = 1)
  void testCalculateDiscountPositive(String checkoutItemsStr, BigDecimal expectedResult, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Rule3CheapestFreeInGroupRuleCalculator rule = new Rule3CheapestFreeInGroupRuleCalculator(Set.of(1));

    BigDecimal actualResult = rule.calculateDiscount(checkoutItems);
    assertEquals(expectedResult, actualResult, intent);
  }

  @DisplayName("All negative tests to calculate discount for Rule3")
  @ParameterizedTest
  @CsvFileSource(resources = "/rule3_calculate_discount_negative_case.csv", numLinesToSkip = 1)
  void testCalculateDiscountNegative(String checkoutItemsStr, String exceptionMessage, String intent) {
    List<CheckoutItem> checkoutItems = parseCheckoutItems(checkoutItemsStr);
    Rule3CheapestFreeInGroupRuleCalculator rule = new Rule3CheapestFreeInGroupRuleCalculator(Set.of(1));

    final BigDecimal discount = rule.calculateDiscount(checkoutItems);
    assertEquals(BigDecimal.ZERO, discount, exceptionMessage+" - "+intent);
  }


  private List<CheckoutItem> parseCheckoutItems(String str) {
    if (str == null || str.isEmpty()) {
      return Collections.emptyList();
    }
    return Arrays.stream(str.split(";"))
        .map(itemStr -> {
          String[] parts = itemStr.split(":");
          return new CheckoutItem(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), new BigDecimal(parts[3]));
        })
        .collect(Collectors.toList());
  }

  private Set<Integer> parseEligibleGroup(String str) {
    return Arrays.stream(str.split(";"))
        .map(Integer::parseInt)
        .collect(Collectors.toSet());
  }
}
