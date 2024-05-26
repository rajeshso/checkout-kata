#!/bin/bash

# Function to validate a CSV file
function validate_csv() {
  local csv_file="$1"
  local expected_header="item-id,group-id,quantity,unit-price"

  # Check if file exists and is readable
  if ! [[ -f "$csv_file" && -r "$csv_file" ]]; then
    echo "Error: File '$csv_file' does not exist or is not readable."
    return 1
  fi

  # Check header row using head and awk (fixed typo)
  local header=$(head -n 1 "$csv_file" | awk -F ',' '{print $0}')
  if [[ "$header" != "$expected_header" ]]; then
    echo "Error: Invalid header row. Expected '$expected_header', got '$header'."
    return 1
  fi

  # Use awk to validate data rows
  awk -F ',' '
    NR > 1 {
      # Check for 4 comma-separated fields
      if (NF != 4) {
        print "Error: Invalid data row (wrong number of fields)."
        exit 1
      }

      # Validate item-id (non-empty string)
      if ($1 == "") {
        print "Error: Invalid item-id (empty string)."
        exit 1
      }

      # Validate group-id (integer)
      if ($2 !~ /^[0-9]+$/) {
        print "Error: Invalid group-id (not an integer)."
        exit 1
      }

      # Validate quantity (positive integer)
      if ($3 !~ /^[0-9]+$/ || $3 <= 0) {
        print "Error: Invalid quantity (not a positive integer)."
        exit 1
      }

      # Validate unit-price (decimal number)
      if ($4 !~ /^\$?[0-9]+(\.[0-9]+)?$/) {
        print "Error: Invalid unit-price (not a decimal number)."
        exit 1
      }
    }
  ' "$csv_file" || return 1

  echo "CSV file '$csv_file' appears to be valid."
}

# Get the filenames from command line arguments
csv_file="$1"

# Validate the CSV file
validate_csv "$csv_file" || exit 1

exit 0