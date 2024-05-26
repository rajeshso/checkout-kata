package com.viooh;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckoutController {
  private static final Logger LOGGER = Logger.getLogger(CheckoutController.class.getName());

  private final CheckoutService checkoutService;

  public CheckoutController() throws Exception {
    // Create PromotionRules using the PromotionRulesFactory
    PromotionRulesFactory promotionRulesFactory = PromotionRulesFactory.getInstance();
    try {
      PromotionRules promotionRules = promotionRulesFactory.createPromotionRules();

      // Use the PromotionRules to generate the List of DiscountRuleCalculators using the DefaultRuleCalculatorFactory
      RuleCalculatorFactory ruleCalculatorFactory = new DefaultRuleCalculatorFactory();

      List<DiscountRuleCalculator> buyXPayYRulesList = promotionRules.getBuyXPayYRules().stream()
          .map(rule -> ruleCalculatorFactory.createRuleCalculator(RuleConstants.BUY_X_PAY_Y_RULE, rule.getItemIds()))
          .toList();

      List<DiscountRuleCalculator> specialPriceRulesList = promotionRules.getSpecialPriceRules().stream()
          .map(rule -> ruleCalculatorFactory.createRuleCalculator(RuleConstants.SPECIAL_PRICE_RULE, rule.getItemIds(), BigDecimal.valueOf(rule.getSpecialPrice())))
          .toList();

      List<DiscountRuleCalculator> cheapestFreeInGroupRulesList = promotionRules.getCheapestFreeInGroupRules().stream()
          .map(rule -> ruleCalculatorFactory.createRuleCalculator(RuleConstants.CHEAPEST_FREE_IN_GROUP_RULE, Set.of(rule.getGroupId())))
          .toList();

      List<DiscountRuleCalculator> buyNOfXGetKOfYFreeRulesList = promotionRules.getBuyNOfXGetKOfYFreeRules().stream()
          .map(rule -> ruleCalculatorFactory.createRuleCalculator(RuleConstants.BUY_N_OF_X_GET_K_OF_Y_FREE_RULE, rule.getItemX(), rule.getNtoTrigger(), rule.getItemY(), rule.getKtoTrigger()))
          .toList();

      // Combine all the above lists
      List<DiscountRuleCalculator> discountRuleCalculators = new ArrayList<>();
      discountRuleCalculators.addAll(buyXPayYRulesList);
      discountRuleCalculators.addAll(specialPriceRulesList);
      discountRuleCalculators.addAll(cheapestFreeInGroupRulesList);
      discountRuleCalculators.addAll(buyNOfXGetKOfYFreeRulesList);

      // Construct the CheckoutService with the List<DiscountRuleCalculator>
      checkoutService = new CheckoutService(discountRuleCalculators);

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error creating promotion rules, discount rules, and the checkout service", e);
      throw new Exception("Error creating promotion rules", e);
    }
  }

  public BigDecimal checkout(final List<CheckoutItem> checkoutItems) {
    return checkoutService.checkout(checkoutItems);
  }
}
