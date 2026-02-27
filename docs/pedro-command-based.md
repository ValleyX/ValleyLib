# Pedro Pathing: Everything Command-Based

This guide shows how to run Pedro Pathing entirely through command-based workflows in ValleyLib.

## New Pedro command helpers

`PedroSubsystem` now exposes command factories directly:

- `follow(path)`
- `follow(path, maxPower)`
- `waitUntilIdle()`
- `setMaxPower(maxPower)`

Static helpers are also available via `PedroCommands`:

- `PedroCommands.follow(...)`
- `PedroCommands.waitUntilIdle(...)`
- `PedroCommands.followSequence(...)`

## Pedro auto DSL

Use `PedroAutoDsl.auto(drive, builder -> ...)` for path-first autos.

Builder methods:

- `follow(path)`
- `follow(path, maxPower)`
- `action(runnable)`
- `waitSeconds(seconds)`
- `waitUntilDriveIdle()`
- `command(command)`
- `parallel(commands...)`

## Example: full command-based Pedro autonomous

```java
Command auto = PedroAutoDsl.auto(drive, a -> a
    .action(() -> intake.close())
    .follow(pathToSpike, 0.85)
    .action(() -> intake.dropPreload())
    .parallel(
        drive.follow(pathToBackdrop, 0.9),
        lift.toHeight(LiftHeight.HIGH)
    )
    .waitUntilDriveIdle()
    .action(() -> outtake.score())
);
```

You can also compose with core APIs:

```java
Command auto = PedroCommands.followSequence(drive, p1, p2, p3)
    .andThen(() -> claw.open())
    .andThen(drive.waitUntilIdle())
    .alongWith(vision.trackTag());
```

## Recommended structure

1. Keep all pathing in commands (no direct follower calls in OpMode loop).
2. Register the `PedroSubsystem` once and let scheduler call `periodic()`.
3. Combine Pedro commands with manipulator commands via `parallel`, `deadline`, and `race`.
4. Use trigger bindings to schedule prebuilt Pedro command chains during teleop.

This keeps autonomous and teleop behavior deterministic and testable.
