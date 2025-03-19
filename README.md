# Compiler Construction

## Overview
This project consists of two main components:
1. **MyLang Tokenizer:** A lexical analyzer that tokenizes a custom programming language called "MyLang."
2. **NFA to DFA Converter:** A program that converts an NFA (Non-Deterministic Finite Automaton) for identifying identifiers in MyLang into a DFA (Deterministic Finite Automaton).

Additionally, the project includes a sample MyLang program (`program.mlang`) to demonstrate the tokenizer's functionality.

---

## 1. MyLang Tokenizer
### **Purpose:**
The `MyLangTokenizer` class reads a source code file (`program.mlang`), tokenizes its contents, and classifies the tokens based on predefined rules.

### **How it Works:**
1. Reads the source code from `program.mlang`.
2. Identifies different types of tokens:
   - **Keywords:** `var`, `print`, `if`, `else`, `while`
   - **Boolean Values:** `true`, `false`
   - **Operators:** `+`, `-`, `*`, `/`, `%`, `^`, `=`, `<`, `>`, `!`, `&`, `|`
   - **Punctuation:** `(`, `)`, `{`, `}`, `;`, `,`
   - **Identifiers:** Variable names following specific rules
   - **Numbers:** Integers and decimals
   - **Strings:** Text enclosed in double quotes (`"..."`)
   - **Comments:** Single-line (`//`) and multi-line (`/* ... */`)
3. Outputs the list of tokens along with their types.

### **Key Methods:**
- `tokenize(String code)`: Splits the code into tokens while handling comments and strings correctly.
- `getTokenType(String token)`: Determines the type of a given token based on regex matching and predefined sets.

### **Example Output:**
For the following MyLang code:
```mlang
var x = 10;
var y = 5.12345;
print("The result is: ", x + y);
```
The tokenizer would produce:
```
TOKEN: var (Type: Keyword)
TOKEN: x (Type: Identifier)
TOKEN: = (Type: Operator)
TOKEN: 10 (Type: Integer)
TOKEN: ; (Type: Punctuation)
TOKEN: print (Type: Keyword)
TOKEN: "The result is: " (Type: String)
TOKEN: , (Type: Punctuation)
TOKEN: x (Type: Identifier)
TOKEN: + (Type: Operator)
TOKEN: y (Type: Identifier)
TOKEN: ; (Type: Punctuation)
```
---

## 2. NFA to DFA Converter
### **Purpose:**
The `NFAtoDFA.java` file implements an NFA (Non-Deterministic Finite Automaton) to identify valid identifiers in MyLang and converts it into a DFA (Deterministic Finite Automaton) for more efficient processing.

### **How it Works:**
1. Constructs an **NFA** for identifiers (i.e., variable names starting with a letter and followed by letters or digits).
2. Converts the NFA to a **DFA** by computing the epsilon closure and merging states.
3. Uses the DFA to check whether given inputs are valid identifiers.

### **Key Methods:**
- `addTransition(int fromState, char input, int toState)`: Defines NFA transitions.
- `convertToDFA()`: Converts the NFA into a DFA using state mapping and epsilon closure.
- `accepts(String input)`: Checks whether a given input is a valid identifier according to the DFA.

### **Example DFA Conversion:**
#### **NFA Transitions (Before Conversion):**
```
0 -- a --> 1
0 -- b --> 1
...
1 -- a --> 1
1 -- b --> 1
1 -- 0 --> 1
1 -- 1 --> 1
```
#### **DFA Transitions (After Conversion):**
```
0 -- [a-z] --> 1
1 -- [a-z0-9] --> 1
```
This means that an identifier must start with a lowercase letter and can be followed by letters or digits.

### **Example Usage:**
```java
DFA identifierDFA = identifierNFA.convertToDFA();
System.out.println(identifierDFA.accepts("abc")); // true
System.out.println(identifierDFA.accepts("x1")); // true
System.out.println(identifierDFA.accepts("123")); // false (invalid identifier)
```

---

## 3. Sample MyLang Program (`program.mlang`)
A test program written in MyLang:
```mlang
var x = 10;
var y = 5.12345;
var z = x + y;
print("The result is: ", z);

// Single-line comment
/* Multi-line comment */

if x > 5 {
    print("x is greater than 5");
} else {
    print("x is not greater than 5");
}
```
### **How it Works:**
1. Declares three variables (`x`, `y`, `z`).
2. Uses `print` to output a string and a variable.
3. Demonstrates single-line (`// ...`) and multi-line (`/* ... */`) comments.
4. Includes an `if-else` statement to check whether `x > 5` and prints the corresponding message.

---

## Conclusion
This project provides a simple tokenizer for the MyLang language and a finite automaton-based approach to recognizing identifiers efficiently. The tokenizer processes the source code, breaking it down into meaningful tokens, while the NFA-to-DFA conversion optimizes identifier recognition.

This implementation can be extended by:
- Adding more keywords and operators.
- Enhancing the DFA to recognize more complex tokens.
- Implementing a parser to analyze the syntax of MyLang programs.

