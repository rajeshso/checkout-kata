package com.viooh;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PromotionRulesFactory {

  private static volatile PromotionRulesFactory instance;
  private static final String DEFAULT_PROMOTION_RULES_FILE = "promotion_rules.json";
  private final ObjectMapper objectMapper;
  private PromotionRules promotionRules;

  private PromotionRulesFactory() {
    this.objectMapper = new ObjectMapper();
  }

  public static synchronized PromotionRulesFactory getInstance() {
    if (instance == null) {
      instance = new PromotionRulesFactory();
    }
    return instance;
  }

  public PromotionRules createPromotionRules() throws Exception {
    return createPromotionRules(DEFAULT_PROMOTION_RULES_FILE);
  }
  public PromotionRules createPromotionRules(String filePath) throws Exception {
    if (promotionRules == null) {
      try {
        // Read the contents of the JSON file
        byte[] jsonBytes = Files.readAllBytes(Paths.get(filePath));
        String jsonString = new String(jsonBytes);

        // Parse the JSON string into PromotionRules object
        promotionRules = objectMapper.readValue(jsonString, PromotionRules.class);
      } catch (IOException e) {
        System.out.println("Error reading promotion rules JSON file: " + e.getMessage()); //TODO : remember to use slf4j
        throw new Exception("Error reading promotion rules JSON file", e);
      }
    }
    return promotionRules;
  }
}