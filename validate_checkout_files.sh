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

# Function to validate the config file
function validate_cfg() {
  local cfg_file="$1"

  # Check if file exists and is readable
  if ! [[ -f "$cfg_file" && -r "$cfg_file" ]]; then
    echo "Error: File '$cfg_file' does not exist or is not readable."
    return 1
  fi

  # Define a regular expression for key-value pairs
  local key_value_regex="^([a-zA-Z0-9_.]+)=(.+)$"

  # Track unique rule IDs using a regular Bash array
  declare -a rule_ids=()

  # Loop through each line in the config file
  while IFS= read -r line; do
    # Skip empty lines and comments
    if [[ -z "$line" || "$line" =~ ^# ]]; then
      continue
    fi

    # Validate key-value format
    if ! [[ "$line" =~ $key_value_regex ]]; then
      echo "Error: Invalid key-value format in line: '$line'."
      return 1
    fi

    # Extract key and value
    local key="${BASH_REMATCH[1]}"
    local value="${BASH_REMATCH[2]}"

    # Check for valid rule start (rule.id=...)
    if [[ "$key" =~ ^rule\.id= ]]; then
      local rule_id="${value}"

      # Check for duplicate rule ID
      if [[ "${rule_ids[@]}" =~ (^| )"$rule_id($| )" ]]; then
        echo "Error: Duplicate rule ID '$rule_id' found."
        return 1
      fi
      rule_ids+=("$rule_id")
    fi
  done < "$cfg_file"

  echo "Config file '$cfg_file' appears to be valid."
}

# Get the filenames from command line arguments
csv_file="$1"
cfg_file="$2"

# Validate the CSV file
validate_csv "$csv_file" || exit 1

# Validate the config file
validate_cfg "$cfg_file" || exit 1

echo "Both CSV and config files appear to be valid."
exit 0