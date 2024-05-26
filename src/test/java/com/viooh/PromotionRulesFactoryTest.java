package com.viooh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionRulesFactoryTest {

  private static final String userDir = System.getProperty("user.dir");

  @Test
  public void testCreatePromotionRules_validJson_success() throws Exception {
    String filePath = userDir + "/src/test/resources/valid_promotion_rules.json";

    PromotionRulesFactory factory = PromotionRulesFactory.getInstance();
    PromotionRules promotionRules = factory.createPromotionRules(filePath);

    assertNotNull(promotionRules);
    assertNotEquals(0, promotionRules.getBuyXPayYRules().size());
    assertNotEquals(0, promotionRules.getSpecialPriceRules().size());
    assertNotEquals(0, promotionRules.getCheapestFreeInGroupRules().size());
    assertNotEquals(0, promotionRules.getBuyNOfXGetKOfYFreeRules().size());
  }
  @Test
  public void testCreatePromotionRules_fileNotFound_throwsException() {
    String filePath = "non-existent_file.json";
    PromotionRulesFactory factory = PromotionRulesFactory.getInstance();

    assertThrows(Exception.class, () -> factory.createPromotionRules(filePath));
  }
}
