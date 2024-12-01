import os
import sys
from datetime import datetime
from aocd import get_data
import webbrowser

aoc = "aoc24"
main_folder = "src/main/kotlin/com/diutsu/" + aoc

# Kotlin template
main_template = """package com.diutsu.aoc24

import com.diutsu.aoc24.library.readInput
import com.diutsu.aoc24.library.runDay
import com.diutsu.aoc24.library.validateInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val day = "{{ day }}"

    validateInput( "$day-part1" , 11 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1" ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 31 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2" ) {
        part2(readInput("$day/input"))
    }
}
"""

def create_day_main_kotlin_file(day):
    # Create main folder if it doesn't exist
    os.makedirs(main_folder, exist_ok=True)
    kotlin_code = main_template.replace("{{ day }}", "day" + day)

    # Create main Kotlin file
    file_name = os.path.abspath(main_folder + "/Day" + day + ".kt")

    # Check if the file already exists
    if os.path.exists(file_name):
        print(f"  File '{file_name}' already exists. Skipping creation.")
        return  # Skip file creation if it already exists

    # Create the Kotlin file
    with open(file_name, "w") as file:
        file.write(kotlin_code)

    print(f"  Kotlin file '{file_name}' created successfully!")

def get_input_files(year, day) :
    try:
        data_str = get_data(day=day, year=year)

    except Exception as e:
        # Handle any errors (e.g., if get_data fails or file operations fail)
        print(f"  Error retrieving or saving input files for day {day} of {year}: {e}")
        return

    # Extract the last two digits of the year
    year_last_two_digits = str(year)[-2:]
    day_str = str(day).zfill(2)
    directory = f'inputs/aoc{year_last_two_digits}/day{day_str}/'
    os.makedirs(directory, exist_ok=True)

    # Save input data to input.txt
    input_file_path = os.path.join(directory, 'input.txt')
    with open(input_file_path, 'w') as input_file:
        input_file.write(data_str)

    # Save example data to example.txt
    example_file_path = os.path.join(directory, 'example.txt')
    example_data = ""

    # Check if the file already exists
    if not os.path.exists(example_file_path) and example_data:
        with open(example_file_path, 'w') as example_file:
            example_file.write(example_data)
    else:
        print(f"  Example data file '{example_file_path}' already exists. Skipping creation.")



def open_aoc_url(year, day):
    # Format the URL using the given year and day
    url = f"https://adventofcode.com/{year}/day/{day}"

    # Print the URL
    print(f"Opening: {url}")
    print()

    # Open the URL in the default web browser
    webbrowser.open(url)

if __name__ == "__main__":
    if len(sys.argv) > 3:
        print("Usage: python create_kotlin_files.py <day> <year>")
        sys.exit(1)

    # Get current year and day if not provided
    current_date = datetime.now()
    day_number = int(sys.argv[1]) if len(sys.argv) > 1 else current_date.timetuple().tm_mday
    year = int(sys.argv[2]) if len(sys.argv) > 2 else current_date.year

    day_str = str(day_number).zfill(2)
    open_aoc_url(year, day_number)

    print(f'📖 Fetching day {day_str} input ....')
    get_input_files(year, day_number)

    print(f'📖 Creating file for day {day_str} from template ....')
    create_day_main_kotlin_file(day_str)

    print("Good luck!! 🍀")
