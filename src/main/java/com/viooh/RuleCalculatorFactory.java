package com.viooh;

public interface RuleCalculatorFactory {
  DiscountRule createRuleCalculator(RuleConstants ruleConstants, Object... args);
}
