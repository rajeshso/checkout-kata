package com.viooh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public final class PromotionRules {
  private final List<BuyXPayYRule> buyXPayYRules;
  private final List<SpecialPriceRule> specialPriceRules;
  private final List<CheapestFreeInGroupRule> cheapestFreeInGroupRules;
  private final List<BuyNOfXGetKOfYFreeRule> buyNOfXGetKOfYFreeRules;

  @JsonCreator
  public PromotionRules(@JsonProperty("buyXPayYRules") List<BuyXPayYRule> buyXPayYRules,
      @JsonProperty("specialPriceRules") List<SpecialPriceRule> specialPriceRules,
      @JsonProperty("cheapestFreeInGroupRules") List<CheapestFreeInGroupRule> cheapestFreeInGroupRules,
      @JsonProperty("buyNOfXGetKOfYFreeRules") List<BuyNOfXGetKOfYFreeRule> buyNOfXGetKOfYFreeRules) {
    this.buyXPayYRules = buyXPayYRules;
    this.specialPriceRules = specialPriceRules;
    this.cheapestFreeInGroupRules = cheapestFreeInGroupRules;
    this.buyNOfXGetKOfYFreeRules = buyNOfXGetKOfYFreeRules;
  }
}

@Data
final class BuyXPayYRule {
  private final int id;
  private final List<String> itemIds;
  private final int triggerQuantity;
  private final int discountQuantity;
  private final String comment;
  private final static RuleConstants RULE_NAME = RuleConstants.BUY_X_PAY_Y_RULE;

  @JsonCreator
  public BuyXPayYRule(@JsonProperty("id") int id,
      @JsonProperty("itemIds") List<String> itemIds,
      @JsonProperty("triggerQuantity") int triggerQuantity,
      @JsonProperty("discountQuantity") int discountQuantity,
      @JsonProperty("comment") String comment) {
    this.id = id;
    this.itemIds = itemIds;
    this.triggerQuantity = triggerQuantity;
    this.discountQuantity = discountQuantity;
    this.comment = comment;
  }
}

@Data
final class SpecialPriceRule {
  private final int id;
  private final List<String> itemIds;
  private final double specialPrice;
  private final String comment;
  private static final RuleConstants RULE_NAME = RuleConstants.SPECIAL_PRICE_RULE;

  @JsonCreator
  public SpecialPriceRule(@JsonProperty("id") int id,
      @JsonProperty("itemIds") List<String> itemIds,
      @JsonProperty("specialPrice") double specialPrice,
      @JsonProperty("comment") String comment) {
    this.id = id;
    this.itemIds = itemIds;
    this.specialPrice = specialPrice;
    this.comment = comment;
  }
}

@Data
final class CheapestFreeInGroupRule {
  private final int id;
  private final int groupId;
  private final String comment;
  private final static RuleConstants RULE_NAME = RuleConstants.CHEAPEST_FREE_IN_GROUP_RULE;

  @JsonCreator
  public CheapestFreeInGroupRule(@JsonProperty("id") int id,
      @JsonProperty("groupId") int groupId,
      @JsonProperty("comment") String comment) {
    this.id = id;
    this.groupId = groupId;
    this.comment = comment;
  }
}

@Data
final class BuyNOfXGetKOfYFreeRule {
  private final int id;
  private final String itemX;
  private final String itemY;
  private final int ntoTrigger;
  private final int ktoTrigger;
  private final String comment;
  private final static RuleConstants RULE_NAME = RuleConstants.BUY_N_OF_X_GET_K_OF_Y_FREE_RULE;

  @JsonCreator
  public BuyNOfXGetKOfYFreeRule(@JsonProperty("id") int id,
      @JsonProperty("itemX") String itemX,
      @JsonProperty("itemY") String itemY,
      @JsonProperty("ntoTrigger") int ntoTrigger,
      @JsonProperty("ktoTrigger") int ktoTrigger,
      @JsonProperty("comment") String comment) {
    this.id = id;
    this.itemX = itemX;
    this.itemY = itemY;
    this.ntoTrigger = ntoTrigger;
    this.ktoTrigger = ktoTrigger;
    this.comment = comment;
  }
}