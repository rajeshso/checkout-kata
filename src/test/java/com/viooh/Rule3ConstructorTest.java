package com.viooh;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Rule3ConstructorTest {

  @ParameterizedTest
  @CsvFileSource(resources = "/rule3_constructor_exception_case.csv", numLinesToSkip = 1)
  void testConstructorExceptions(String eligibleGroupForThisRuleStr, String exceptionMessage, String intent) {
    if (eligibleGroupForThisRuleStr!=null && eligibleGroupForThisRuleStr.equals("null")) {
      eligibleGroupForThisRuleStr = null;
    }
    Set<Integer> eligibleGroupForThisRule = eligibleGroupForThisRuleStr == null ? null : parseGroupIds(eligibleGroupForThisRuleStr);

    IllegalArgumentException thrown = assertThrows(
        IllegalArgumentException.class,
        () -> new Rule3(eligibleGroupForThisRule),
        intent
    );
    assertEquals(exceptionMessage, thrown.getMessage(), intent);
  }

  private Set<Integer> parseGroupIds(String groupIdsStr) {
    if (groupIdsStr.isEmpty()) {
      return Set.of();
    }
    return Arrays.stream(groupIdsStr.split(";"))
        .map(Integer::valueOf)
        .collect(Collectors.toSet());
  }
}
