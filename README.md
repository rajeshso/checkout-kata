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

## Known Issues

- Logging is not implemented. I would use slf4j for logging in the next release.
- The code is not thread-safe. I would use the singleton pattern to make the code thread-safe.
- The actual price for every CheckoutItem is not displayed in the output. I would add this feature in the next release.
- The input data validation is not exhaustive. I would add more data validation checks and extensive error handling in the next release.
- The code is not optimized for performance. I would optimize the code for performance based on the deployment and traffic in the next release.
- The code is not tested for large data sets. I would test the code for large data sets in the next release.
- The code is tested for most edge cases but not tested for all. I would test the code for the remaining edge cases in the next release.
- The items should not overlap in the rules. I would add a check to ensure that the items do not overlap in the rules in the next release.
