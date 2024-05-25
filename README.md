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
- use this command to validate the files. The script does few data sanity checks, but is not exhaustive enough.
  ```shell
    ./validate_checkout_files.sh <checkout_items.csv> <config_rules.cfg>
    ```
- The input files are validated for the following conditions:
- The checkout_items.csv file should have the following columns:
  - item-id
  - group-id
  - quantity
  - unit-price
- The config_rules.cfg file should have the following properties:
  - Rule1: buy any 3 equal priced items and pay for 2
     rule.id=1
     rule.type=BUY_X_PAY_Y
     rule.quantity.to.trigger=3
     rule.discount.quantity=1
  - Please refer the format for other rules in the config_rules[n].cfg files.
- Please place the config_rules.cfg and config_rules[n].cfg files in the resources folder or the root of the project.
- The file sizes should not be too large to fit in memory.
- Comments are code smell because the code should be self-explanatory. Yet, I have added comments to communicate intent quicker the algorithm code to the interview panel. The comments will be removed in the production code though.[https://refactoring.guru/smells/comments#:~:text=Comments%20are%20usually%20created%20with,code%20that%20could%20be%20improved.]