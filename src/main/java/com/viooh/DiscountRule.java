package com.viooh;


import java.math.BigDecimal;
import java.util.List;
//TODO: Rename this interface to DiscountRuleCalculator and also its implementation classes should be renamed to Rule1Calculator, Rule2Calculator, Rule3Calculator, Rule4Calculator
//TODO: Consider renaming Rule1, Rule2, Rule3, Rule4 to Rule1BuyXPayYRuleCalculator, Rule2SpecialPriceRuleCalculator, Rule3CheapestFreeInGroupRuleCalculator, Rule4BuyNOfXGetKOfYFreeRuleCalculator
//TODO: Remove unused methods from this interface - getRuleName, getEligibleElementsForThisRule
public interface DiscountRule {
  RuleConstants getRuleName();
  boolean applies(List<CheckoutItem> items);
  BigDecimal calculateDiscount(List<CheckoutItem> items);
  List<String> getEligibleElementsForThisRule();
}