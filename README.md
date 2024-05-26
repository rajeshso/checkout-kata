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

# Solution 

## Notes
- The input files are in CSV format and CFG file in properties format.
- ```shell 
   chmod +x validate_checkout_files.sh 
  ```
- use this command to validate the checkout files. The script does few data sanity checks, but is not exhaustive enough.
  ```shell
    ./validate_checkout_files.sh <checkout_items.csv>
    ```
- The input files are validated for the following conditions:
- The checkout_items.csv file should have the following columns:
  - item-id
  - group-id
  - quantity
  - unit-price
- The config_rules.json file should have the following properties:
        ```json
        {
          "buyXPayYRules": [
                {
                "id": 1,
                "itemIds": ["E", "F"],
                "triggerQuantity": 3,
                "discountQuantity": 1,
                "comment": "Buy 3 of E or F and pay for 2. This belongs to Rule1: buy any 3 equal priced items and pay for 2"
                },
                {
                "id": 2,
                "itemIds": ["E", "F"],
                "triggerQuantity": 3,
                "discountQuantity": 1,
                "comment": "Buy 3 of E or F and pay for 2. This belongs to Rule1: buy any 3 equal priced items and pay for 2"
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
                "itemIds": ["E", "F"],
                "specialPrice": 10.00,
                "comment": "Special price for E and F is £10.00. This belongs to Rule2: Special price for item E and F."
                }
          ],
          "cheapestFreeInGroupRules": [
                {
          "id": 3,
          "groupId": 3,
          "comment": "This belongs to Rule3: Buy 3 of A, B, C, D and get the cheapest free."
          }
          ],
          "buyNOfXGetKOfYFreeRules": [
                {
          "id": 4,
          "itemX": "A",
          "itemY": "B",
          "ntoTrigger": 3,
          "ktoTrigger": 1,
          "comment": "Example is Buy 3(N) bottles of water (X) and get 1(K) soda (Y) for free (N = 3, X=Water, K = 1, Y=Soda). This belongs to Rule4: for each N items X, you get K items Y for free"
          }
          ]
        }
        ```
      - Please use the format for rules as in the promotion_rules.json files. The rules are categorized into 4 types:
        - buyXPayYRules
        - specialPriceRules
        - cheapestFreeInGroupRules
        - buyNOfXGetKOfYFreeRules
      - By default, the promotion_rules.json file in the root is used in the code. But you can create and configure any file path in the code.
      - The reason for individual rules is to make the code flexible and easy to add new rules that adhere to Type safety.
- Please place the config_rules.cfg and promotion_rules.json files in the resources folder or the root of the project.
- The file sizes should not be too large to fit in memory.
- Comments are code smell because the code should be self-explanatory. Yet, I have added comments to communicate intent quicker the algorithm code to the interview panel and for discussion during the interview. The comments will be removed in the production code though.[https://refactoring.guru/smells/comments#:~:text=Comments%20are%20usually%20created%20with,code%20that%20could%20be%20improved.]

### Design

- The code is designed to be modular and flexible to add new rules with minimal effort.
The code is classified as rule configuration, rule calculation, checkout and utility classes
- Rule Configuration: The code reads the promotion_rules.json file and creates a configuration object with the rules. The rules are categorized into 4 types: buyXPayYRules, specialPriceRules, cheapestFreeInGroupRules, and buyNOfXGetKOfYFreeRules. 
  - The Promotional Rules contains the pojos that map to the json configuration file.
  - The Promotional Rules Factory is the factory class that reads the json file and creates the configuration object.
  - If you want to add a new Rule, you can create a new class and add it to the factory.
- Discount Calculation: The code calculates the discount for each item based on the rules. The discount is calculated based on the type of rule and the item quantity.
  - There are four types of rules: buyXPayYRules, specialPriceRules, cheapestFreeInGroupRules, and buyNOfXGetKOfYFreeRules.
  - There are four types of Discount Calculators: BuyXPayYDiscountCalculator, SpecialPriceDiscountCalculator, CheapestFreeInGroupDiscountCalculator, and BuyNOfXGetKOfYFreeDiscountCalculator.
  - The Discount Calculator Factory creates the appropriate Discount Calculator based on the rule type.
  - If you want to add a new Rule and Discount Calculator, you can create a new class that implements the DiscountCalculator interface and add it to the factory.
- Checkout: The code reads the checkout_items.csv file and calculates the total price of the items in the cart. The code applies the discount rules to the items and calculates the final price.
    - The CheckoutItem class represents an item in the cart with the item-id, group-id, quantity, and unit price.
    - The CheckoutController class calculates the total price of the items in the cart and applies the discount rules.
    - The CheckoutService class calculates the final price of the items in the cart after applying the discount rules.
    - The Dependency injection are used in every class to make the code testable and flexible.
- Utility: The code contains utility classes for reading CSV files, JSON files, and calculating the total price.
    - The CheckoutParser class reads the checkout_items.csv file, creates CheckoutItem objects.
    - The Promotion Rules Factory class reads the promotion_rules.json file, creates PromotionalRules objects.
## Known Issues

- The actual price for every CheckoutItem is not displayed in the output. 
- The input data validation is not exhaustive.
- The code is not optimized for performance. 
- The code is not tested for large data sets. 
- The items should not overlap in the rules. 
- The classes should ideally be packaged into - rule configuration, rule calculation, checkout and utility classes. 

## Instructions to build/test/run the system

This guide assumes you're using Java 11 and have downloaded the application code package.

Prerequisites:

Java 11 installed (https://www.oracle.com/java/technologies/downloads/)
Gradle installed (https://gradle.org/install/)
Build Instructions:

Navigate to the project directory: Open a terminal window and navigate to the directory where you downloaded the code package.

Build and test the application: Run the following command to build the application and execute the unit tests:

Bash
./gradlew build
Use code with caution.
content_copy
This command will download any dependencies needed by the project, compile the source code, run the unit tests, and create a build output (usually in a build directory).

Running the Application:

Run the application:  Execute the following command to run the application with sample data:

Bash
./gradlew run
Use code with caution.
content_copy
This command will use the built application and sample data to simulate a cart with items and discount rules.  The final receipt for the cart will be printed on the console.

Additional Notes:

The provided commands assume you are using a Linux or macOS system. If you're on Windows, you might need to use gradlew.bat instead of ./gradlew.
This guide assumes the Gradle wrapper scripts (gradlew or gradlew.bat) are present in the project directory. If not, you might need to run gradle build and gradle run directly using the Gradle command.
By following these steps, you should be able to build, test, and run the Java application using Gradle. If you encounter any issues or need further assistance, feel free to ask for help.

## One key challenge is left out in this release.
- The challenge is to find the best discount for the user when more than one rule applies. I have not implemented this feature in the code. 
### Challenge
 Imagine you have n items in your cart and m discount rules. 
 Each rule might apply to a subset of these items. 
 The naive approach would be to try applying every combination of rules to the cart and see which one yields the cheapest total. 
 This is where things get tricky.

 Number of Combinations: The number of combinations of rules to try grows exponentially with the number of rules (m). For example, with just 5 rules, you have 2^5 (32) possible combinations to test.

 Overlapping Rules: The complexity arises because items can potentially qualify for multiple discounts. This means a single item might be included in many different combinations, leading to redundant calculations.

 Intractable for Large Numbers:

As the number of items and rules increases, the number of combinations explodes.  Trying all combinations becomes impractical and takes an extremely long time (exponential time complexity). This is why the problem is considered NP-hard - there's no known efficient algorithm guaranteed to find the optimal solution in all cases for large datasets.
https://www.researchgate.net/publication/220273718_Internet_shopping_optimization_problem 

### One possible approach - Prioritized Discounts
One possible approach is to prioritize the discounts based on some criteria. For example, you could sort the discounts based on the percentage discount they offer, and then apply them in that order. This way, the most beneficial discounts are applied first, potentially reducing the total cost. In order for this approach to be successful, the higher discounts rule should be at the top. So, there is no need to check for the best discount.

