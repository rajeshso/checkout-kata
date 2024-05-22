package com.viooh;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckoutItemParser {
  public static List<CheckoutItem> parseCheckoutItems(String filePath) throws Exception {
    List<CheckoutItem> items = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      // Skip the header row
      reader.readLine();

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        // Ensure data has 4 elements (item-id, group-id, quantity, unit-price)
        if (data.length != 4) {
          throw new Exception("Invalid data format in file: " + filePath);
        }

        String itemId = data[0].trim();
        int groupId = Integer.parseInt(data[1].trim());
        int quantity = Integer.parseInt(data[2].trim());
        BigDecimal unitPrice = new BigDecimal(data[3].trim());

        items.add(new CheckoutItem(itemId, groupId, quantity, unitPrice));
      }
    }

    return items;
  }
}
