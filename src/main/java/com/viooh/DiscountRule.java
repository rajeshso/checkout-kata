package com.viooh;


import java.math.BigDecimal;
import java.util.List;

interface DiscountRule {
  String getRuleName();
  boolean applies(List<CheckoutItem> items);
  BigDecimal calculateDiscount(List<CheckoutItem> items);
  List<String> getEligibleItemsForThisRule();
}