package com.viooh;

import java.math.BigDecimal;
import java.util.List;

// Rule2: buy 2 equal priced items for a special price
public class Rule2SpecialPriceRuleCalculator implements DiscountRuleCalculator {
  private static final RuleConstants RULE_NAME = RuleConstants.SPECIAL_PRICE_RULE;
  private static final int X = 2;
  private final List<String> eligibleItemsForThisRule;
  private final BigDecimal specialPricePerUnit;

  public Rule2SpecialPriceRuleCalculator(List<String> eligibleItemsForThisRule, BigDecimal specialPricePerUnit) {
    // Check if the eligibleItemsForThisRule is not empty, if it is throw an IllegalArgumentException
    if (eligibleItemsForThisRule == null || eligibleItemsForThisRule.isEmpty()) {
      throw new IllegalArgumentException("eligibleItemsForThisRule cannot be null or empty");
    }
    // Check if the specialPricePerUnit is greater than 0, if it is not throw an IllegalArgumentException
    if (specialPricePerUnit == null || specialPricePerUnit.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("SpecialPricePerUnit must be greater than zero");
    }
    this.eligibleItemsForThisRule = eligibleItemsForThisRule;
    this.specialPricePerUnit = specialPricePerUnit;
  }

  @Override
  public boolean applies(List<CheckoutItem> items) {
    // Check that the items in the list belong to the eligible items., if it is not throw an IllegalArgumentException
    for (CheckoutItem item : items) {
      if (!eligibleItemsForThisRule.contains(item.getItemId())) {
        throw new IllegalArgumentException("Item " + item.getItemId() + " is not eligible for this rule");
      }
    }
    // Validate that all eligible items have the same unit price, if it is not throw an IllegalArgumentException
    BigDecimal unitPrice = items.get(0).getUnitPrice();
    for (CheckoutItem item : items) {
      if (item.getUnitPrice().compareTo(unitPrice) != 0) {
        throw new IllegalArgumentException("All items must have the same unit price of £" + unitPrice + " to apply the " + RULE_NAME + ". But item " + item.getItemId() + " has a unit price of £" + item.getUnitPrice());
      }
    }
    // Ensure the specialPricePerUnit is less than the actual unit price, if it is not throw an IllegalArgumentException
    if (specialPricePerUnit.compareTo(unitPrice) >= 0) {
      throw new IllegalArgumentException("Special price per unit must be less than the actual unit price in order to apply the " + RULE_NAME + ". Special price per unit: £" + specialPricePerUnit + " is not less than the actual unit price: £" + unitPrice);
    }
    // Check if there are at least two items to apply the discount
    int totalQuantity = items.stream().mapToInt(CheckoutItem::getQuantity).sum();
    return totalQuantity >= X;
  }

  @Override
  public BigDecimal calculateDiscount(List<CheckoutItem> items) {
    // Ensure the rule applies
    if (!applies(items)) {
      return BigDecimal.ZERO;
    }

    BigDecimal unitPrice = items.get(0).getUnitPrice();
    int totalQuantity = items.stream().mapToInt(CheckoutItem::getQuantity).sum();

    // Calculate the number of pairs and remaining items
    int numOfPairs = totalQuantity / X;
    int remainingItems = totalQuantity % X;

    // Calculate the undiscounted total price of all items
    BigDecimal undiscountedTotal = unitPrice.multiply(BigDecimal.valueOf(totalQuantity));

    // Calculate the discounted total price of all items
    BigDecimal discountedTotal = specialPricePerUnit.multiply(BigDecimal.valueOf(numOfPairs * X)) // Special price for each pair
        .add(unitPrice.multiply(BigDecimal.valueOf(remainingItems))); // Regular price for the remaining items

    // Return the difference between the undiscounted total price and the discounted total price
    return undiscountedTotal.subtract(discountedTotal);
  }
}
