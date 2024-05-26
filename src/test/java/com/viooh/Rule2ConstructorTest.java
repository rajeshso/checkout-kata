package com.viooh;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Rule2ConstructorTest {

  @ParameterizedTest
  @CsvFileSource(resources = "/rule2_constructor_exception_case.csv", numLinesToSkip = 1)
  void testConstructorExceptions(String eligibleItemsForRuleStr, String specialPricePerUnitStr, String exceptionMessage, String intent) {
    if (eligibleItemsForRuleStr != null && eligibleItemsForRuleStr.equals("null")) {
      eligibleItemsForRuleStr = null;
    }
    List<String> eligibleItemsForRule = eligibleItemsForRuleStr == null ? null : Arrays.asList(eligibleItemsForRuleStr.split(";"));
    BigDecimal specialPricePerUnit = new BigDecimal(specialPricePerUnitStr);

    IllegalArgumentException thrown = assertThrows(
        IllegalArgumentException.class,
        () -> new Rule2(eligibleItemsForRule, specialPricePerUnit),
        intent
    );
    assertEquals(exceptionMessage, thrown.getMessage(), intent);
  }
}
