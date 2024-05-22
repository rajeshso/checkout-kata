package com.viooh;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CheckoutItemParserTest {
  String userDir = System.getProperty("user.dir");
  @Test
  public void testParseCheckoutItems_ValidFile() throws Exception {
    String filePath = userDir + "/src/test/resources/valid_checkout_items.csv";

    List<CheckoutItem> items = CheckoutItemParser.parseCheckoutItems(filePath);

    // Assertions to verify parsed data
    assertEquals(2, items.size(), "Expected 2 checkout items");

    CheckoutItem item1 = items.get(0);
    assertEquals("ITEM_A", item1.getItemId());
    assertEquals(1, item1.getGroupId());
    assertEquals(2, item1.getQuantity());
    assertEquals(BigDecimal.valueOf(19.99), item1.getUnitPrice());

    CheckoutItem item2 = items.get(1);
    assertEquals("ITEM_B", item2.getItemId());
    assertEquals(2, item2.getGroupId());
    assertEquals(1, item2.getQuantity());
    assertThat(BigDecimal.valueOf(12.50)).isEqualByComparingTo(BigDecimal.valueOf(12.50));
  }
  @Test
  public void testParseCheckoutItems_InvalidFormat() {
    String filePath = userDir + "/src/test/resources/invalid_checkout_items.csv"; // Replace with your test file path

    try {
      CheckoutItemParser.parseCheckoutItems(filePath);
      fail("Expected Exception to be thrown for invalid format");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Invalid data format"), "Expected specific error message");
    }
  }
  @Test
  public void testParseCheckoutItems_FileNotFound() {
    String filePath = userDir + "/src/test/resources/invalid_file.csv"; // Replace with your test file path

    try {
      CheckoutItemParser.parseCheckoutItems(filePath);
      fail("Expected Exception to be thrown for file not found");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("No such file or directory"), "Expected specific error message");
    }
  }
  @Test
  public void testParseCheckoutItems_EmptyFile() {
    String filePath = userDir + "/src/test/resources/empty_file.csv"; // Replace with your test file path

    try {
      List<CheckoutItem> items = CheckoutItemParser.parseCheckoutItems(filePath);
      assertTrue(items.isEmpty(), "Expected empty list for empty file");
    } catch (Exception e) {
      fail("Unexpected Exception: " + e.getMessage());
    }
  }
  @Test
  public void testParseCheckoutItems_NullFilePath() {
    assertThrows(NullPointerException.class, () -> CheckoutItemParser.parseCheckoutItems(null));
  }
  @Test
  public void testParseCheckoutItems_EmptyFilePath() {
    assertThrows(FileNotFoundException.class, () -> CheckoutItemParser.parseCheckoutItems(""));
  }
  @Test
  public void testParseCheckoutItems_WhitespaceFilePath() {
    assertThrows(FileNotFoundException.class, () -> CheckoutItemParser.parseCheckoutItems(" "));
  }
  @Test
  public void testParseCheckoutItems_InvalidFilePath() {
    assertThrows(FileNotFoundException.class, () -> CheckoutItemParser.parseCheckoutItems("invalid_file_path.csv"));
  }
  @Test
  public void testParseCheckoutItems_InvalidData() {
    String filePath = userDir + "/src/test/resources/invalid_data.csv";

    try {
      final List<CheckoutItem> checkoutItems = CheckoutItemParser.parseCheckoutItems(filePath);
      assertThat(checkoutItems).isEmpty();
    } catch (Exception e) {
      fail("Expected No Exception to be thrown for invalid data, just an empty list should be returned");
    }
  }
}
