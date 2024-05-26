package com.viooh;


import java.math.BigDecimal;
import java.util.List;
public interface DiscountRuleCalculator {
  boolean applies(List<CheckoutItem> items);
  BigDecimal calculateDiscount(List<CheckoutItem> items);
}