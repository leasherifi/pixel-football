# Pixel Football

A 30-second arcade football mini-game built with **Java and JavaFX**. Aim the swinging arrow, press SPACE to kick, and score as many goals as you can before time runs out.

## How it plays

- An arrow sweeps back and forth above the ball. Press **SPACE** to shoot in that direction.
- The ball follows a gravity-based arc. Land it in the goal for **+10 points**.
- You have **30 seconds**. When time is up, your score and session high score appear. Press **SPACE** to retry.

## What it demonstrates

- Game loop with `AnimationTimer` (physics update every frame) and a `Timeline` countdown
- Projectile motion: velocity from angle and power, constant gravity
- Collision/goal detection with simple bounding checks
- JavaFX scene graph: `ImageView` sprites, `Group` + `Rotate` for the aim arrow, styled `Label`/`VBox` HUD and game-over overlay
- Keyboard input handling and game state reset

## Run it

Requires JDK 17+ and [JavaFX](https://openjfx.io/).

1. Put the sprite images (`background1.jpg`, `minigame.png`, `goal.png`, `ballgame.png`) in `C:/a/` (paths are currently hard-coded in `minigame.java`).
2. Compile and run `src/MyfirstJavaFX/minigame.java` from NetBeans, or from the command line with `--module-path <javafx-lib> --add-modules javafx.controls`.

If the background image is missing, the game falls back to a light-blue background.

## Roadmap

- Load assets from a relative `resources/` folder instead of `C:/a/`
- Add sound effects and a goalkeeper
- Persist the high score between sessions

## Author

Lea Sherifi — [@leasherifi](https://github.com/leasherifi)
