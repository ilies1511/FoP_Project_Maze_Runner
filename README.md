# Maze Runner

![Project Logo or Screenshot](assets/mainMenu_background.png)

"Vampire in Berghain" is a top-down maze-runner game where you play as a recently awakened vampire. Trapped in Berghain, a club in Berlin, you must navigate through dangerous mazes, spill the blood of mortals, and find the portal that leads back to your home in Transylvania.

## Quick Start (Getting Started)

Follow these instructions to run the project locally on your machine.

### Windows
Open a terminal (Command Prompt or PowerShell) in the project root and run:
```bash
gradlew.bat desktop:run
```

### Mac
Open a terminal in the project root and run:
```bash
./gradlew desktop:run
```
*Note: The project is configured to use the `-XstartOnFirstThread` JVM argument, which is required for LWJGL3 applications on macOS.*

### Linux
Open a terminal in the project root and run:
```bash
./gradlew desktop:run
```

## Prerequisites

To build and run this project, you need:
- **Java Development Kit (JDK) 17 or higher**: Ensure your `JAVA_HOME` environment variable is set correctly.
- **Gradle**: The project uses the Gradle wrapper, so no manual installation is required.

## Project Structure

This project is built using **Java 17** and the **libGDX** framework, organized into a multi-module Gradle setup:

- **`core`**: Contains the main game logic, entity management, and shared code. It utilizes **gdx-vfx** for post-processing and **FreeType** for font rendering.
- **`desktop`**: The desktop launcher module using **LWJGL3**. It handles window creation and uses **gdx-nativefilechooser** for system file dialogs.
- **`assets`**: Stores all game resources, including textures, sound effects, music, and map files.

## How to Build/Export

To create a standalone, runnable JAR file for the desktop version, execute the following command:

```bash
./gradlew desktop:dist
```

The generated JAR file will be located in the `desktop/build/libs/` directory. You can run it using:
```bash
java -jar desktop/build/libs/desktop-1.0.jar
```

## Controls

The following table lists the default key bindings for Maze Runner:

| Action | Key |
| :--- | :--- |
| **Move Up** | `W` |
| **Move Left** | `A` |
| **Move Down** | `S` |
| **Move Right** | `D` |
| **Attack** | `SPACE` |
| **Dash** | `Left Shift` |
| **Interact** | `E` |
| **Special Ability** | `Q` |
| **Progression Tree** | `I` |
| **Developer Console** | `/` |
| **Zoom In** | `+` / `=` |
| **Zoom Out** | `-` |
| **Pause / Menu** | `ESC` |

## Game Mechanics

"Vampire in Berghain" features different mechanics, such as: 

1.  

## Contributing

Contributions are welcome! If you'd like to improve the game, please follow these steps:

1. **Fork** the repository.
2. Create a new **branch** (`git checkout -b feature/amazing-feature`).
3. **Commit** your changes (`git commit -m 'Add some amazing feature'`).
4. **Push** to the branch (`git push origin feature/amazing-feature`).
5. Open a **Pull Request**.

Please ensure your code follows the existing style and all tests pass before submitting.
