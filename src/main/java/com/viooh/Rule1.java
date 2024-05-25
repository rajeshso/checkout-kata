package com.viooh;

import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
//Rule1: buy any 3 equal priced items and pay for 2
public class Rule1 implements DiscountRule {

  private final static String RULE_NAME = "BUY_X_PAY_Y";
  private final static int X = 3;

  private final List<String> eligibleItemsForThisRule;

  public Rule1(List<String> eligibleItemsForThisRule) {
    this.eligibleItemsForThisRule = eligibleItemsForThisRule;
  }

  @Override
  public String getRuleName() {
    return RULE_NAME;//Buy3PayFor2Rule
  }

  @Override
  public boolean applies(List<CheckoutItem> items) {
    // Group all items by price and check if any group has 3 or more items
    Map<BigDecimal, Long> priceCountMap = items.stream()
        .filter(item -> eligibleItemsForThisRule.contains(item.getItemId()))
        .collect(Collectors.groupingBy(CheckoutItem::getUnitPrice, Collectors.summingLong(CheckoutItem::getQuantity)));

    return priceCountMap.values().stream().anyMatch(quantity -> quantity >= X);
  }

  @Override
  public BigDecimal calculateDiscount(List<CheckoutItem> items) {
    // Group all rules-eligible CheckoutItems with the same price
    Map<BigDecimal, Long> priceCountMap = items.stream()
        .filter(item -> eligibleItemsForThisRule.contains(item.getItemId()))
        .collect(Collectors.groupingBy(CheckoutItem::getUnitPrice, Collectors.summingLong(CheckoutItem::getQuantity)));

    BigDecimal totalDiscount = BigDecimal.ZERO;

    for (Map.Entry<BigDecimal, Long> entry : priceCountMap.entrySet()) {
      BigDecimal price = entry.getKey();
      Long quantity = entry.getValue();

      // Calculate the discount for each group of X items (Buy 3 Get 1 Free)
      long discountQuantity = quantity / X; // Number of free items

      // Total discount is the price of free items
      totalDiscount = totalDiscount.add(price.multiply(BigDecimal.valueOf(discountQuantity)));
    }

    // Return the total of all discounts
    return totalDiscount;
  }


  @Override
  public List<String> getEligibleElementsForThisRule() {
    return eligibleItemsForThisRule;
  }
}
