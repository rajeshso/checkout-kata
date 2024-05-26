package com.viooh;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

//Rule3: buy 3 (in a group of X=3 items) and the cheapest is free
public class Rule3CheapestFreeInGroupRuleCalculator implements DiscountRuleCalculator {

  private final static int X = 3;
  private final Set<Integer> eligibleGroupForThisRule;

  public Rule3CheapestFreeInGroupRuleCalculator(Set<Integer> eligibleGroupForThisRule) {
    if (eligibleGroupForThisRule == null || eligibleGroupForThisRule.isEmpty()) {
      throw new IllegalArgumentException("Eligible group for this rule cannot be null or empty");
    }
    this.eligibleGroupForThisRule = eligibleGroupForThisRule;
  }

  @Override
  public boolean applies(List<CheckoutItem> items) {
    if (items == null || items.isEmpty()) {
      return false;
    }
    if (items.stream().anyMatch(item -> !eligibleGroupForThisRule.contains(item.getGroupId()))) {
      return false;
    }
    Map<Integer, List<CheckoutItem>> groupedItems = items.stream()
        .filter(item -> eligibleGroupForThisRule.contains(item.getGroupId()))
        .collect(Collectors.groupingBy(CheckoutItem::getGroupId));

    for (List<CheckoutItem> group : groupedItems.values()) {
      int totalQuantity = group.stream().mapToInt(CheckoutItem::getQuantity).sum();
      if (totalQuantity >= X) {
        return true;
      }
    }
    return false;
  }

  @Override
  public BigDecimal calculateDiscount(List<CheckoutItem> items) {
    if (!applies(items)) {//Verify if the rule applies to the items.
      return BigDecimal.ZERO;
    }
    //Group items by groupId and sort each group by unit price
    Map<Integer, List<CheckoutItem>>  groupIDItemsMap = items.stream()
        .filter(item -> eligibleGroupForThisRule.contains(item.getGroupId()))
        .collect(Collectors.groupingBy(CheckoutItem::getGroupId));
    BigDecimal totalDiscount = BigDecimal.ZERO;
    //For each group, sort the items by price [groupId -> Price -> CheckoutItem]
    for (List<CheckoutItem> group : groupIDItemsMap.values()) {
      //Sort the group
      List<CheckoutItem> sortedGroup = group.stream()
          .sorted(Comparator.comparing(CheckoutItem::getUnitPrice))
          .toList();
      int totalQuantity = sortedGroup.stream().mapToInt(CheckoutItem::getQuantity).sum();
      int numberOfFreeItems = totalQuantity / X;//For each group, calculate the discount by identifying the number of cheapest items eligible to be free (one per X items).
      BigDecimal priceOfCheapestItem = sortedGroup.get(0).getUnitPrice();
      BigDecimal totalDiscountOfGroup = priceOfCheapestItem.multiply(BigDecimal.valueOf(numberOfFreeItems));
      totalDiscount = totalDiscount.add(totalDiscountOfGroup);//Sum up the discounts across all groups
    }
    return totalDiscount;
  }
  @Override
  public List<String> eligibleItemsForThisRule() {
    return List.of();
  }

  @Override
  public List<Integer> eligibleGroupsForThisRule() {
    return eligibleGroupForThisRule.stream().collect(Collectors.toList());
  }
}
