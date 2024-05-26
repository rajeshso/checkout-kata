package com.viooh;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckoutService {
  private final List<DiscountRuleCalculator> discountRuleCalculators;
  private static final Logger LOGGER = Logger.getLogger(CheckoutService.class.getName());

  public CheckoutService(List<DiscountRuleCalculator> discountRuleCalculators) {
    this.discountRuleCalculators = discountRuleCalculators;
  }

  //The grand total of the checkout items after applying all the discounts is returned
  //actual price of every item after discount is not done in this release
  public BigDecimal checkout(final List<CheckoutItem> checkoutItems) {
    List<CheckoutItem> remainingItems = new ArrayList<>(checkoutItems.stream().collect(
        Collectors.toList()));
    BigDecimal totalDiscount = BigDecimal.ZERO;
    // Apply discounts and track the remaining items after each discount is applied
    for (DiscountRuleCalculator discountRuleCalculator : discountRuleCalculators) {
      try {
        //For each eligibleItem, collect the checkoutItems that are applicable to the DiscountRuleCalculator
        List<CheckoutItem> applicableItems = remainingItems.stream()
            .filter(item -> discountRuleCalculator.eligibleItemsForThisRule().contains(item.getItemId()) ||
                discountRuleCalculator.eligibleGroupsForThisRule().contains(item.getGroupId()))
            .collect(Collectors.toList());
        if (!applicableItems.isEmpty() && discountRuleCalculator.applies(applicableItems)) {
          BigDecimal discount = discountRuleCalculator.calculateDiscount(
              applicableItems);
          //Calculate the total discount by adding the discount
          totalDiscount = totalDiscount.add(discount);
          //Remove the consolidated checkoutItems from the checkoutItems list
          remainingItems.removeAll(applicableItems);
        }
      } catch (Exception e) {
         LOGGER.log(Level.SEVERE, "Error applying discount rule", e);
      }
    }
    //Calculate the total price
    BigDecimal totalPrice = checkoutItems.stream()
        .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Subtract the total discount from the total price to calculate the grand total
    BigDecimal grandTotalPrice = totalPrice.subtract(totalDiscount);
    return grandTotalPrice.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : grandTotalPrice;
  }
}
