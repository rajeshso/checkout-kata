package com.viooh;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutItem {
  private String itemId;
  private int groupId;
  private int quantity;
  private BigDecimal unitPrice;
}