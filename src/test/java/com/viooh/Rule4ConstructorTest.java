package com.viooh;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Rule4ConstructorTest {

  @ParameterizedTest
  @CsvFileSource(resources = "/rule4_constructor_exception_case.csv", numLinesToSkip = 1)
  void testConstructorExceptions(String eligibleBuyItemX, String eligibleBuyItemXQuantityNStr, String eligibleFreeItemY, String eligibleFreeItemYQuantityKStr, String exceptionMessage, String intent) {
    Integer eligibleBuyItemXQuantityN = parseInteger(eligibleBuyItemXQuantityNStr);
    Integer eligibleFreeItemYQuantityK = parseInteger(eligibleFreeItemYQuantityKStr);

    String finalEligibleBuyItemX = "null".equals(eligibleBuyItemX) ? null : eligibleBuyItemX;
    String finalEligibleFreeItemY = "null".equals(eligibleFreeItemY) ? null : eligibleFreeItemY;

    IllegalArgumentException thrown = assertThrows(
        IllegalArgumentException.class,
        () -> new Rule4(finalEligibleBuyItemX, eligibleBuyItemXQuantityN, finalEligibleFreeItemY, eligibleFreeItemYQuantityK),
        intent
    );
    assertEquals(exceptionMessage, thrown.getMessage(), intent);
  }

  private Integer parseInteger(String str) {
    if ("null".equals(str)) {
      return -1;
    }
    return Integer.parseInt(str);
  }
}
