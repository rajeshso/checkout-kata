package com.viooh;

public interface RuleCalculatorFactory {
  DiscountRuleCalculator createRuleCalculator(RuleConstants ruleConstants, Object... args);
}
