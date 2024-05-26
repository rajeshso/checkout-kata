# _The checkout technical challenge_

This challenge is meant to demonstrate your technical ability and your proficiency with Java technologies.  You are welcome to use any software package and consult external reference materials (web, textbooks, articles, product manuals etc.). All work must be your own.

Implement a Supermarket checkout that calculates the total price of a number of items. Some discount rules may apply.

Some items have multiple prices based on price rules such as:
- *Rule1*: buy any 3 equal priced items and pay for 2
- *Rule2*: buy 2 equal priced items for a special price
- *Rule3*: buy 3 (in a group of items) and the cheapest is free
- *Rule4*: for each `N` items `X`, you get `K` items `Y` for free

Write a program which takes input from a CSV file with the checkout items. The file contains the `item-id`, the `group-id`, the quantity and the unit price.
A second input file should contain the configuration of the rules and to which items they apply. For example, *Rule2* needs to know to which `item-id` it applies to and what is the final price to charge. Similarly, *Rule4* needs `N`, `X`, `K` and `Y` to be defined. Rule1 and Rule3 should work out of the box without needing extra configuration.
Each item can have at most only one discount applied. If more than one rule applies, pick the best discount for the user.
The output required is the receipt with the actual price of every item and the grand total.

The code should be simple and flexible so that any new rule should be added with the minimum effort.

You should not spend more than a few hours on this problem. Please provide the source code along with `README.md` instructions on how to build/test/run the system. If you have any questions, feel free to open a Github Issue. **When you have completed the challenge please submit a Pull Request.**

# Solution Overview

## Input Files

- **checkout_items.csv**: Contains cart items with columns for item-id, group-id, quantity, and unit-price.
- **promotion_rules.json**: Defines discount rules in JSON format (see example below).

## Validation Script

**validate_checkout_files.sh**: This script performs basic data sanity checks on the input files. You can run it using:

```bash
chmod +x validate_checkout_files.sh
./validate_checkout_files.sh <checkout_items.csv>
```
## Discount Rule Types
The promotion_rules.json file defines four types of discount rules:

1. buyXPayYRules: Offers a discount when a specific quantity of items is purchased (e.g., buy 3 of item X, pay for 2).
2. specialPriceRules: Sets a special price for a particular item or group of items.
3. cheapestFreeInGroupRules: Offers the cheapest item in a group for free when a certain quantity is purchased.
4. buyNOfXGetKOfYFreeRules: Provides a free item (Y) for every N items purchased of another item (X).

## Code Structure
The code is designed for modularity and easy addition of new rules. Here's a breakdown of the key components:

### Rule Configuration
1. PromotionalRules: POJOs representing the JSON configuration for discount rules.
2. PromotionalRulesFactory: Reads the promotion_rules.json file and creates the configuration object.

### Discount Calculation
1. DiscountCalculator: Interface for calculating discounts based on rule type.
2. Specific implementations for each rule type (e.g., BuyXPayYDiscountCalculator).
3. DiscountCalculatorFactory: Creates the appropriate DiscountCalculator based on the rule type.

### Checkout
1. CheckoutItem: Represents an item in the cart with its details.
2. CheckoutController: Calculates the total price and applies discount rules.
3. CheckoutService: Calculates the final price after applying discounts.
4. Dependency injection is used for testability and flexibility.

## Building, Testing, and Running the System
### Prerequisites
1. Java 11 installed (https://www.oracle.com/java/technologies/downloads/)
2. Gradle installed (https://gradle.org/install/)

### Instructions
1. Navigate to the project directory in your terminal.

2. Build and Test: Run the following command to build the application and execute unit tests:
    ```bash
   ./gradlew build
    ```
3. Run the Application: Execute this command to run the application with sample data:
   ```bash
   ./gradlew run
   ```
   This will use the built application and sample data to simulate a cart with items and discount rules. The final receipt will be printed on the console.

### Notes
1. The provided commands assume Linux/macOS. For Windows, use ```gradlew.bat``` instead of ```./gradlew```.
2. These instructions assume the Gradle wrapper scripts (gradlew or gradlew.bat) are present in the project directory.

## Known Issues
1. Actual item prices are not displayed in the output.
2. Input data validation is not exhaustive.
3. The code is not optimized for performance or large datasets.
4. Items should not overlap in discount rules.
5. Ideally, classes should be grouped by functionality (rule configuration, calculation, checkout, etc.).

## Example Promotion Rules Configuration
```json
{
   "buyXPayYRules": [
      {
         "id": 1,
         "itemIds": ["A", "B"],
         "triggerQuantity": 3,
         "discountQuantity": 1,
         "comment": "Buy 3 of A or B and pay for 2. This belongs to Rule1: buy any 3 equal priced items and pay for 2"
      },
      {
         "id": 2,
         "itemIds": ["C", "D"],
         "triggerQuantity": 3,
         "discountQuantity": 1,
         "comment": "Buy 3 of C or D and pay for 2. This belongs to Rule1: buy any 3 equal priced items and pay for 2"
      }
   ],
   "specialPriceRules": [
      {
         "id": 21,
         "itemIds": ["E", "F"],
         "specialPrice": 10.00,
         "comment": "Special price for E and F is £10.00. This belongs to Rule2: Special price for item E and F."
      },
      {
         "id": 2,
         "itemIds": ["G", "H"],
         "specialPrice": 10.00,
         "comment": "Special price for G and H is £10.00. This belongs to Rule2: Special price for item G and H."
      }
   ],
   "cheapestFreeInGroupRules": [
      {
         "id": 3,
         "groupId": 5,
         "comment": "This belongs to Rule3: Buy 3 of 5 and get the cheapest free."
      }
   ],
   "buyNOfXGetKOfYFreeRules": [
      {
         "id": 4,
         "itemX": "I",
         "itemY": "J",
         "ntoTrigger": 3,
         "ktoTrigger": 1,
         "comment": "Example is Buy 3(N) bottles of water (X) and get 1(K) soda (Y) for free (N = 3, X=Water, K = 1, Y=Soda). This belongs to Rule4: for each N items X, you get K items Y for free"
      }
   ]
}
```

## Sample Input Files
### checkout_items.csv
```
item-id,group-id,quantity,unit-price
A,1,3,50.00
B,1,2,20.00
C,1,3,10.00
```
## Outstanding Challenge: Optimal Discount Selection
The current implementation doesn't find the absolute best discount when multiple rules apply. Here's why:

1. Exponential Number of Combinations: The number of possible discount combinations grows exponentially with the number of rules.
2. Overlapping Rules: Items can qualify for multiple discounts, leading to redundant calculations.

This makes finding the optimal solution intractable for large datasets (NP-hard problem).

### Possible Approach: Prioritized Discounts
One approach is to prioritize discounts based on some criteria (e.g., percentage discount) and apply them sequentially. This reduces complexity but might miss the absolute best combination.

A more sophisticated solution is beyond the scope of this release.

### Additional Notes
The provided reference Network optimizations in the Internet of Things: A review discusses this challenge in more detail.
[https://www.researchgate.net/publication/327820942_Network_optimizations_in_the_Internet_of_Things_A_review]
