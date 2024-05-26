package com.viooh;

//Rule4: for each N items X, you get K items Y for free
//Rule4: Buy 3(N) bottles of water (X) and get 1(K) soda (Y) for free (N = 3, X=Water, K = 1, Y=Soda).

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * For each N items X: This means customer need to buy a specific number of items (N) of a particular product (X).
 * Customer get K items Y for free: Upon purchasing N items of X, customer can get another product (Y) completely free of charge. customer receive K number of free items Y.
 */
public class Rule4 implements DiscountRule {
  private final static String RULE_NAME = "BUY_N_OF_X_GET_K_OF_Y_FREE";
  private final String eligibleBuyItemX;
  private final int eligibleBuyItemXQuantityN;
  private final String eligibleFreeItemY;
  private final int eligibleFreeItemYQuantityK;

  public Rule4(String eligibleBuyItemX, int eligibleBuyItemXQuantityN, String eligibleFreeItemY, int eligibleFreeItemYQuantityK) {
    if (eligibleBuyItemX == null || eligibleBuyItemX.trim().isEmpty()) {
      throw new IllegalArgumentException("eligibleBuyItemX cannot be null or empty");
    }
    if (eligibleBuyItemXQuantityN <= 0) {
      throw new IllegalArgumentException("eligibleBuyItemXQuantityN must be greater than zero");
    }
    if (eligibleFreeItemY == null || eligibleFreeItemY.trim().isEmpty()) {
      throw new IllegalArgumentException("eligibleFreeItemY cannot be null or empty");
    }
    if (eligibleFreeItemYQuantityK <= 0) {
      throw new IllegalArgumentException("eligibleFreeItemYQuantityK must be greater than zero");
    }
    this.eligibleBuyItemX = eligibleBuyItemX;
    this.eligibleBuyItemXQuantityN = eligibleBuyItemXQuantityN;
    this.eligibleFreeItemY = eligibleFreeItemY;
    this.eligibleFreeItemYQuantityK = eligibleFreeItemYQuantityK;
  }

  @Override
  public String getRuleName() {
    return RULE_NAME;
  }

  @Override
  public boolean applies(List<CheckoutItem> items) {
    if (items == null || items.isEmpty()) {
      return false;
    }
    //Group all items by ids
    Map<String, Long> itemQuantities = items.stream()
        .collect(Collectors.groupingBy(CheckoutItem::getItemId, Collectors.summingLong(CheckoutItem::getQuantity)));
    //Check if there are enough items of x and y
    Long buyItemXQuantity = itemQuantities.getOrDefault(eligibleBuyItemX, 0L);
    Long freeItemYQuantity = itemQuantities.getOrDefault(eligibleFreeItemY, 0L);

    return buyItemXQuantity>= eligibleBuyItemXQuantityN && freeItemYQuantity >= eligibleFreeItemYQuantityK;
  }

  @Override
  public BigDecimal calculateDiscount(List<CheckoutItem> items) {
    if (!applies(items)) {
      return BigDecimal.ZERO;
    }
    // Calculate the total quantity of items of X
    long buyItemXQuantity = items.stream()
        .filter(item -> item.getItemId().equals(eligibleBuyItemX))
        .mapToLong(CheckoutItem::getQuantity)
        .sum();

    //Calculate the total quantity of items of Y
    long freeItemYQuantity = items.stream()
        .filter(item -> item.getItemId().equals(eligibleFreeItemY))
        .mapToLong(CheckoutItem::getQuantity)
        .sum();

    //Determine the max number of free items Y that can be given based on the quantity of items X purchased
    long maxFreeItemYQuantity = buyItemXQuantity / eligibleBuyItemXQuantityN;

    //Determine the actual number of free items Y based on available quantity of Y and maximum allowance
    long actualFreeItemYQuantity = Math.min(maxFreeItemYQuantity, freeItemYQuantity);

    //Find the unit price of free item Y
    final BigDecimal unitPriceOfY = items.stream()
        .filter(item -> item.getItemId().equals(eligibleFreeItemY))
        .map(CheckoutItem::getUnitPrice)
        .findFirst()
        .orElse(BigDecimal.ZERO);

    // Calculate the total discount
    return BigDecimal.valueOf(actualFreeItemYQuantity).multiply(unitPriceOfY);
  }

  @Override
  public List<String> getEligibleElementsForThisRule() {
    return List.of(eligibleBuyItemX, eligibleFreeItemY);
  }
}