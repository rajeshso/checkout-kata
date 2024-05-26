package com.viooh;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRuleCalculatorFactoryTest {

  private final DefaultRuleCalculatorFactory factory = new DefaultRuleCalculatorFactory();

  @Test
  void testCreateRuleCalculatorForBuyXPayYRule() {
    List<String> itemIds = Arrays.asList("A", "B", "C");
    DiscountRule ruleCalculator = factory.createRuleCalculator(RuleConstants.BUY_X_PAY_Y_RULE, itemIds);
    assertTrue(ruleCalculator instanceof Rule1);
  }

  @Test
  void testCreateRuleCalculatorForSpecialPriceRule() {
    List<String> itemIds = Arrays.asList("D", "E");
    BigDecimal specialPrice = BigDecimal.valueOf(9.99);
    DiscountRule ruleCalculator = factory.createRuleCalculator(RuleConstants.SPECIAL_PRICE_RULE, itemIds, specialPrice);
    assertTrue(ruleCalculator instanceof Rule2);
  }

  @Test
  void testCreateRuleCalculatorForCheapestFreeInGroupRule() {
    Set<Integer> groupIds = new HashSet<>(Arrays.asList(1, 2, 3));
    DiscountRule ruleCalculator = factory.createRuleCalculator(RuleConstants.CHEAPEST_FREE_IN_GROUP_RULE, groupIds);
    assertTrue(ruleCalculator instanceof Rule3);
  }

  @Test
  void testCreateRuleCalculatorForBuyNOfXGetKOfYFreeRule() {
    String itemX = "F";
    int nToTrigger = 3;
    String itemY = "G";
    int kToTrigger = 1;
    DiscountRule ruleCalculator = factory.createRuleCalculator(RuleConstants.BUY_N_OF_X_GET_K_OF_Y_FREE_RULE, itemX, nToTrigger, itemY, kToTrigger);
    assertTrue(ruleCalculator instanceof Rule4);
  }

  @Test
  void testCreateRuleCalculatorWithNullArgs() {
    Object[] invalidArgs = null;
    assertThrows(IllegalArgumentException.class, () -> factory.createRuleCalculator(RuleConstants.BUY_X_PAY_Y_RULE, invalidArgs));
  }

  @Test
  void testCreateRuleCalculatorWithEmptyArgs() {
    assertThrows(IllegalArgumentException.class, () -> factory.createRuleCalculator(RuleConstants.BUY_X_PAY_Y_RULE));
  }

  @Test
  void testCreateRuleCalculatorWithInvalidArgTypes() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.createRuleCalculator(RuleConstants.SPECIAL_PRICE_RULE, "invalid", 123));
  }

  @Test
  void testCreateRuleCalculatorWithInvalidRuleConstant() {
    assertThrows(IllegalArgumentException.class, () -> factory.createRuleCalculator(null, "arg1", "arg2"));
  }
}