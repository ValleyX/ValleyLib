# ValleyLib NextFTC-Style Depth Upgrade

This document tracks the major framework upgrades that make `valleyLib` much closer to a comprehensive NextFTC/WPILib-style experience.

## Completed capability set

## 1) Rich command composition

`Command` now supports fluent composition/decorators:

- `withTimeout`
- `until`
- `onlyWhile`
- `unless`
- `beforeStarting`
- `finallyDo`
- `repeatedly`
- `alongWith`
- `raceWith`
- `deadlineWith`
- `andThen`

Plus concrete command groups:

- `SequentialCommandGroup`
- `ParallelCommandGroup`
- `RaceCommand`
- `DeadlineCommand`

Requirement aggregation is preserved in composites, so conflict handling remains safe.

## 2) Command factories (`Commands`)

`Commands` static factories reduce boilerplate:

- `runOnce(...)`
- `run(...)`
- `startEnd(...)`
- `waitSeconds(...)`
- `waitUntil(...)`
- `sequence(...)`
- `parallel(...)`
- `race(...)`
- `deadline(...)`
- `none()`

This gives teams a fast path to express intent without creating one-off classes.

## 3) Autonomous DSL on top (`AutoDsl`)

`AutoDsl.auto(...)` now supports timeline + flow control:

- `command(...)`
- `run(...)`
- `marker(label, sink)`
- `waitSeconds(...)`
- `when(condition, command)`
- `either(condition, onTrue, onFalse)`
- `parallel(...)`
- `race(...)`
- `deadline(...)`

This creates readable autonomous scripts while still compiling into normal commands.

## 4) Trigger model depth

Trigger system now includes:

- `onTrue`, `onFalse`, `onChange`
- `whileTrue`, `whileFalse`, `toggleOnTrue`
- logical composition: `and`, `or`, `negate`
- temporal shaping: `debounce(seconds)`
- batch registration: `TriggerManager.bindAll(...)`

This supports richer control logic without branching spaghetti in OpMode loops.

## 5) Controller input abstraction (Logitech F310 friendly)

- `CommandXboxLike` for axis shaping + trigger/button helpers
- `CommandGamepad` FTC implementation
- `CommandGamepad.forLogitechF310(gamepad)` preset for XInput F310
- `GamepadEx` retained as compatibility alias

Axis deadband/exponent shaping provides finer low-speed control and predictable stick behavior.

## 6) Scheduler lifecycle observability

`CommandScheduler` now supports listeners and runtime introspection:

- `addListener(...)` / `removeListener(...)`
- `onCommandScheduled`, `onCommandFinished`, `onCommandCanceled`
- `isScheduled(...)`
- `reset()`

`CommandOpMode` also supports opt-in telemetry logging via `enableCommandLogging()`.

## 7) Simulation hooks for desktop validation

- `Subsystem.simulationPeriodic()`
- `CommandScheduler.setSimulationEnabled(...)`
- `CommandScheduler.runSimulationStep()`
- `CommandScheduler.isSimulationEnabled()`

These enable desktop state-machine and scheduler validation before field hardware testing.

---


## 8) Pedro pathing command-first compatibility

Pedro integration now has command-native helpers so pathing can stay fully command-based:

- `PedroSubsystem.follow(...)`, `waitUntilIdle()`, `setMaxPower(...)`
- `PedroCommands` static factories
- `PedroAutoDsl` path-first autonomous builder

See [`docs/pedro-command-based.md`](pedro-command-based.md) for usage patterns.

---

## Suggested next steps (future)

- Add path/event-marker adapters for Pedro path segments directly into `AutoDsl`.
- Add command naming/profiling metadata for dashboard timelines.
- Provide sample full robot package (subsystems + auto + teleop) as a starter template.
