package com.viooh;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    System.out.println("Hello and welcome!");

    try {
      // Parse checkout items from a CSV file
      final List<CheckoutItem> checkoutItems = CheckoutItemParser.parseCheckoutItems("checkoutItems1.csv");

      // Initialize the CheckoutController
      CheckoutController checkoutController = new CheckoutController();

      // Perform the checkout operation
      final BigDecimal grandTotalPrice = checkoutController.checkout(checkoutItems);

      // Output the results
      System.out.println("checkoutItems = " + checkoutItems);
      System.out.println("grandTotalPrice = " + grandTotalPrice);

    } catch (Exception e) {
      // Handle any exceptions that occur during the process
      LOGGER.log(Level.SEVERE, "An error occurred during checkout", e);
    }
  }
}
