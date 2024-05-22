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
