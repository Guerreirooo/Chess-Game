## Chess Game — JavaFX Chess Application

## Overview

Chess Game is a desktop chess application developed in **Java** with **JavaFX**, created as an object-oriented programming project to practice software architecture, game logic, graphical user interfaces, persistence, and automated testing.

The application implements a playable chess board with support for piece movement, captures, castling, pawn promotion, turn management, and game-state handling. The project follows the **Model-View-ViewModel (MVVM)** architectural pattern, keeping the chess domain logic separated from the JavaFX presentation layer.

Game state can also be persisted through Java serialization, allowing an unfinished game to be saved and restored.

The project is structured around three main concerns:

* **Model** — chess pieces, board state, rules, and game logic
* **View** — JavaFX graphical interface
* **ViewModel** — communication between the game model and the graphical interface

The repository also includes a dedicated test suite for validating the application's behaviour.

---

## Features

*  Two-player local gameplay
*  Piece movement validation
*  Piece captures
*  Rook movement
*  Castling support
*  Pawn promotion
*  Game persistence using Java serialization
*  Save and restore game state
*  Object-oriented domain model
*  MVVM architecture
*  JavaFX graphical interface

> **Note:** En Passant is currently not implemented.

---

## System Architecture

The application follows the **Model-View-ViewModel (MVVM)** pattern to separate the graphical interface from the underlying chess logic.

```text
                    ┌──────────────────────┐
                    │      JavaFX View     │
                    │                      │
                    │   Chess Board / UI   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      ViewModel       │
                    │                      │
                    │ UI ↔ Game mediation  │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │        Model         │
                    │                      │
                    │ Board / Pieces /     │
                    │ Rules / Game State   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Persistence       │
                    │                      │
                    │ Java Serialization   │
                    └──────────────────────┘
```

### Model

The Model represents the core chess domain.

It is responsible for:

* Representing the chess board
* Representing individual pieces
* Managing piece positions
* Validating movements
* Handling captures
* Managing turns
* Applying special chess rules
* Maintaining the current game state
* Supporting game persistence

Keeping these responsibilities inside the Model prevents the JavaFX interface from becoming responsible for chess rules.

### View

The View is implemented using **JavaFX**.

Its responsibility is to:

* Display the chess board
* Display the pieces
* Allow players to interact with the board
* Present the current game state
* Trigger player actions

The View does not directly implement the chess rules. Instead, user interactions are forwarded through the ViewModel.

## Chess Rules

The game implements the standard movement behaviour for the main chess pieces.

The current implementation supports:

* **Castling**
* **Pawn promotion**

The following rule is currently not implemented:

* **En Passant**

---

## Persistence

The application includes game persistence through **Java serialization**.

This allows the state of an unfinished game to be stored and subsequently restored.

The persisted state can represent information such as:

* Current board configuration
* Piece positions
* Piece state
* Current turn
* Game progress

This functionality demonstrates the use of object serialization to preserve application state between executions.

---

## How to Play

1. Launch the application.
2. The chess board is initialized in the standard starting position.
3. White plays first.
4. Select a piece and choose a valid destination.
5. Captures are performed by moving onto a square occupied by an opposing piece.
6. Players alternate turns.
7. Castling and pawn promotion are available when their respective conditions are met.
8. Continue until the game reaches its final state.

The application is designed for **local two-player gameplay**, with both players using the same desktop application.

---

### Game Persistence

Saving a complete chess game requires more than storing individual moves.

The application needs to preserve the relevant state of the game so that it can be reconstructed when loaded.

Java serialization was used to address this requirement.

### Testing Game Logic

Chess contains many edge cases, making automated testing particularly useful.

A movement rule that works for one position may fail when pieces are blocking the path or when a capture occurs. Testing therefore helps ensure that changes to the game logic do not introduce regressions.

---

## Future Improvements

Possible improvements for future versions include:

*  Implement En Passant
*  Improve check and checkmate detection
*  Add move history and undo functionality
*  Add chess clocks
*  Add a computer opponent
*  Implement a chess AI using Minimax and Alpha-Beta pruning
*  Expand automated test coverage
