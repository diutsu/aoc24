# aoc24

Welcome to the Advent of Code[^aoc] Kotlin project created by [diutsu][github] using the [Advent of Code Kotlin Template][template] delivered by JetBrains.

## Structure:
- src/main/kotlin : contains the solutions for each day
- there are also some test utils under the `com.diutsu.aoc.library` package
- input files are expected by the library utils under `./inputs/aoc24/day0X/` and should always be `.txt` files

## Library utils

The library provides:
- multiple ways to read the input `readInput` `readInputLines`, `scanInput`
- Methods to validate against examples `validateInput` or `checkInput` if you prefer that aproach
- Methods to run and print the result for the day `runDay`, with also the execution time
- A neat `stressTest` util in case you are aiming for performance, example result: 
```
    😰 Stress Testing day01-part2
    📊 Cold:   2,282 µs Avg:      680 µs Warm:     762 µs
```

## How to:

### Generate Day files
- Each day run the `./gen-day.py` script, this will setup you up:
  - Open the AOC page with the problem for the current day
  - Fetch the input file for the current day
  - Create an empty example file where you put the current example 
  - Generate a kotlin file from the template
  - Wishes you good luck

The `gen-day.py` also supports optional `day` and `year` parameters in case you are trying the challenge on other dates.

### Run solution

- Run the main function in each dayX.kt file, see if it passes the validations and prints the results back.

Each day will have the following structure
```kotlin
    fun main() {
        fun part1(input: List<String>): Int {
            input.size
        }
    
        fun part2(input: List<String>): Int {
            input.size
        }
    
        // validations and runs
    }
```
### Examples


Validate the input using an inlined example:
```kotlin
    import com.diutsu.aoc.library.readInput
    import com.diutsu.aoc.library.validateInput
...
    val example = """
        a
        b
        c
        d
        e
    """.trimIndent()

    validateInput( "$day-part1-inline" , 5 ) {
        part1(example.lines())
    }
```

Assert the input using the example from a file:
```kotlin
    import com.diutsu.aoc.library.readInput
    import com.diutsu.aoc.library.checkInput
...
    checkInput( "$day-part1-inline" , 5 ) {
        part1(readInput("$day/example"))
    }
```

Run your solution for an input file:
```kotlin
    import com.diutsu.aoc.library.readInput
    import com.diutsu.aoc.library.runDay
...
    runDay( "$day-part2" ) {
        part2(readInput("$day/input"))
    }
```


```kotlin
    import com.diutsu.aoc.library.readInput
    import com.diutsu.aoc.library.stressTest
    
...

    stressTest( "$day-part2" , 100, 100) {
        part2(readInput("$day/input"))
    }
```
## More

If you're stuck with Kotlin-specific questions or anything related to this template, check out the following resources:

- [Kotlin docs][docs]
- [Kotlin Slack][slack]
- Template [issue tracker][issues]


[^aoc]:
    [Advent of Code][aoc] – An annual event of Christmas-oriented programming challenges started December 2015.
    Every year since then, beginning on the first day of December, a programming puzzle is published every day for twenty-five days.
    You can solve the puzzle and provide an answer using the language of your choice.

[aoc]: https://adventofcode.com
[docs]: https://kotlinlang.org/docs/home.html
[github]: https://github.com/diutsu
[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues
[kotlin]: https://kotlinlang.org
[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up
[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
