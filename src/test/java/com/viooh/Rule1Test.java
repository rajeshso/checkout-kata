package com.viooh;

import java.util.Arrays;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Rule1Test {
  private static final List<String> eligibleItemsForThisRule = Arrays.asList("item1", "item2");

  private static Stream<TestData> provideTestData() {
    return Stream.of(
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 3, BigDecimal.TEN)),
            true,
            "Rule1 should apply for three or more items with the same ID because they have the same price",
            BigDecimal.TEN, // Expected discount for 3 items with price 10 each
            "Discount should be equal to the price of one item"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 2, BigDecimal.TEN)),
            false,
            "Rule1 should not apply for less than three items with the same ID with the same price",
            BigDecimal.ZERO, // No discount should be applied
            "Discount should be zero for less than three items"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 1, BigDecimal.TEN),
                new CheckoutItem("item2", 2, 1, BigDecimal.TEN)),
            false,
            "Rule1 should not apply for different items if there are less than three of each",
            BigDecimal.ZERO, // No discount should be applied
            "Discount should be zero for less than three items of each type"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 2, BigDecimal.TEN),
                new CheckoutItem("item2", 2, 1, BigDecimal.TEN)),
            true,
            "Rule1 should apply for at least one set of three items of same price even if there are less than three of each item but both items have the same price",
            BigDecimal.TEN, // Discount should be for one set of three items
            "Discount should be equal to the price of one item"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 6, BigDecimal.TEN),
                new CheckoutItem("item2", 2, 6, BigDecimal.TEN)),
            true,
            "Rule1 should apply for at least one set of three items when there are more than three of each item",
            BigDecimal.valueOf(40), // Discount should be for two sets of three items (20)
            "Discount should be equal to the price of two items"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 2, BigDecimal.TEN),
                new CheckoutItem("item1", 1, 2, BigDecimal.TEN)),
            true,
            "Rule1 should apply for items of the same id with the same price even if they are in multiples of two",
            BigDecimal.TEN, // Discount should be for one set of three items
            "Discount should be equal to the price of one item"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 6, BigDecimal.TEN),
                new CheckoutItem("item2", 2, 6, BigDecimal.ONE)),
            true,
            "Rule1 should apply for at least one set of three items when there are more than three of each item and they have different prices",
            BigDecimal.valueOf(22),
            "Discount should be equal to the price of two sets of items with different prices"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item1", 1, 1, BigDecimal.TEN),
                new CheckoutItem("item1", 1, 2, BigDecimal.TEN),
                new CheckoutItem("item2", 2, 2, BigDecimal.ONE)),
            true,
            "Rule1 should apply for one set of three items when there are more than three of one itemid and less than three of them for the other itemid with a different price",
            BigDecimal.TEN, // Discount should be for one set of three items
            "Discount should be equal to the price of one item"),
        new TestData(eligibleItemsForThisRule,
            List.of(new CheckoutItem("item100", 1, 1, BigDecimal.TEN),
                new CheckoutItem("item100", 1, 2, BigDecimal.TEN),
                new CheckoutItem("item200", 2, 2, BigDecimal.ONE)),
            false,
            "Rule1 should not apply because none of the checkout items are eligible for this rule",
            BigDecimal.ZERO, // Discount should be zero
            "Discount should be zero"));
  }

  @ParameterizedTest
  @MethodSource("provideTestData")
  public void testApplies(TestData testData) {
    // Arrange
    Rule1 rule1 = new Rule1(testData.ruleItems);
    List<CheckoutItem> items = new ArrayList<>(testData.checkoutItems);

    // Act
    boolean applies = rule1.applies(items);

    // Assert
    if (testData.expectedApplies) {
      assertTrue(applies, testData.appliesMessage);
    } else {
      assertFalse(applies, testData.appliesMessage);
    }
  }

  @ParameterizedTest
  @MethodSource("provideTestData")
  public void testCalculateDiscount(TestData testData) {
    // Arrange
    Rule1 rule1 = new Rule1(testData.ruleItems);
    List<CheckoutItem> items = new ArrayList<>(testData.checkoutItems);

    // Act
    BigDecimal discount = rule1.calculateDiscount(items);

    // Assert
    assertEquals(testData.expectedDiscount, discount, testData.discountMessage);
  }

  private static class TestData {
    List<String> ruleItems;
    List<CheckoutItem> checkoutItems;
    boolean expectedApplies;
    String appliesMessage;
    BigDecimal expectedDiscount;
    String discountMessage;

    TestData(List<String> ruleItems, List<CheckoutItem> checkoutItems, boolean expectedApplies, String appliesMessage, BigDecimal expectedDiscount, String discountMessage) {
      this.ruleItems = ruleItems;
      this.checkoutItems = checkoutItems;
      this.expectedApplies = expectedApplies;
      this.appliesMessage = appliesMessage;
      this.expectedDiscount = expectedDiscount;
      this.discountMessage = discountMessage;
    }
  }
}
