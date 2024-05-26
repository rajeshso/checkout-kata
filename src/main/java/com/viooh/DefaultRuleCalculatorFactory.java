package com.viooh;

import static com.viooh.RuleConstants.BUY_N_OF_X_GET_K_OF_Y_FREE_RULE;
import static com.viooh.RuleConstants.BUY_X_PAY_Y_RULE;
import static com.viooh.RuleConstants.CHEAPEST_FREE_IN_GROUP_RULE;
import static com.viooh.RuleConstants.SPECIAL_PRICE_RULE;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class DefaultRuleCalculatorFactory implements RuleCalculatorFactory {
  @Override
  public DiscountRule createRuleCalculator(RuleConstants ruleConstant, Object... args) {
    if (ruleConstant == null) {
      throw new IllegalArgumentException("Rule constant cannot be null");
    }
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("Arguments cannot be null or empty");
    }

    try {
      if (ruleConstant.equals(BUY_X_PAY_Y_RULE)) {
        if (args.length > 0 && args[0] instanceof List<?>) {
          List<?> itemIds = (List<?>) args[0];
          if (itemIds.stream().allMatch(String.class::isInstance)) {
            @SuppressWarnings("unchecked")// checked above
            List<String> stringItemIds = (List<String>) itemIds;
            return new Rule1(stringItemIds);
          } else {
            throw new IllegalArgumentException("Invalid argument type for BUY_X_PAY_Y_RULE");
          }
        } else {
          throw new IllegalArgumentException("Invalid argument type for BUY_X_PAY_Y_RULE");
        }
      } else if (ruleConstant.equals(SPECIAL_PRICE_RULE)) {
        if (args.length > 1 && args[0] instanceof List<?> && args[1] instanceof BigDecimal) {
          List<?> itemIds = (List<?>) args[0];
          if (itemIds.stream().allMatch(String.class::isInstance)) {
            @SuppressWarnings("unchecked") // checked above
            List<String> stringItemIds = (List<String>) itemIds;
            BigDecimal specialPrice = (BigDecimal) args[1];
            return new Rule2(stringItemIds, specialPrice);
          } else {
            throw new IllegalArgumentException("Invalid argument type for SPECIAL_PRICE_RULE");
          }
        } else {
          throw new IllegalArgumentException("Invalid argument types for SPECIAL_PRICE_RULE");
        }
      } else if (ruleConstant.equals(CHEAPEST_FREE_IN_GROUP_RULE)) {
        if (args.length > 0 && args[0] instanceof Set<?>) {
          Set<?> groupIds = (Set<?>) args[0];
          if (groupIds.stream().allMatch(Integer.class::isInstance)) {
            @SuppressWarnings("unchecked")// checked above
            Set<Integer> integerGroupIds = (Set<Integer>) groupIds;
            return new Rule3(integerGroupIds);
          } else {
            throw new IllegalArgumentException("Invalid argument type for CHEAPEST_FREE_IN_GROUP_RULE");
          }
        } else {
          throw new IllegalArgumentException("Invalid argument type for CHEAPEST_FREE_IN_GROUP_RULE");
        }
      } else if (ruleConstant.equals(BUY_N_OF_X_GET_K_OF_Y_FREE_RULE)) {
        if (args.length > 3 && args[0] instanceof String && args[1] instanceof Integer && args[2] instanceof String && args[3] instanceof Integer) {
          String itemX = (String) args[0];
          int nToTrigger = (int) args[1];
          String itemY = (String) args[2];
          int kToTrigger = (int) args[3];
          return new Rule4(itemX, nToTrigger, itemY, kToTrigger);
        } else {
          throw new IllegalArgumentException("Invalid argument types for BUY_N_OF_X_GET_K_OF_Y_FREE_RULE");
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid number of arguments for rule: " + ruleConstant, e);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Invalid argument type for rule: " + ruleConstant, e);
    }

    throw new IllegalArgumentException("Invalid rule constant: " + ruleConstant);
  }
}