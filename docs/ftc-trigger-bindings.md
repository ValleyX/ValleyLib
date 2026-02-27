# FTC Trigger Bindings Guide

This guide covers the trigger/binding model in `valleyLib-ftc`.

## Core classes

- `CommandGamepad`: FTC gamepad wrapper (recommended)
- `CommandXboxLike`: shaping + trigger helper base class
- `GamepadEx`: backward-compatible alias
- `Trigger`: condition pipeline + event bindings
- `TriggerManager`: polling registry used by `CommandOpMode`

## Logitech F310 quick start

```java
private CommandGamepad driver;

@Override
protected void initialize() {
    driver = CommandGamepad.forLogitechF310(gamepad1);
}

@Override
protected void configureBindings() {
    triggers.bindAll(
        driver.a().onTrue(claw.closeCommand()),
        driver.b().onTrue(claw.openCommand()),
        driver.rightBumper().whileTrue(intake.runIn()),
        driver.leftBumper().whileTrue(intake.runOut())
    );
}
```

## Event bindings

- `onTrue(command)`
- `onFalse(command)`
- `onChange(command)`
- `whileTrue(command)`
- `whileFalse(command)`
- `toggleOnTrue(command)`

## Composition helpers

Build richer conditions:

- `trigger.and(other)`
- `trigger.or(other)`
- `trigger.negate()`
- `trigger.debounce(seconds)`

Example:

```java
Trigger shootReady = driver.rightTriggerButton(0.35)
    .and(driver.leftBumper())
    .debounce(0.05);

triggers.bind(shootReady.whileTrue(shooter.feedAndShoot()));
```

## Axis shaping

Use these axis methods for drive/control input:

- `leftX()`, `leftY()`, `rightX()`, `rightY()`
- `leftTrigger()`, `rightTrigger()`
- `leftTriggerButton(threshold)`, `rightTriggerButton(threshold)`

These values are deadbanded and curve-shaped for smoother control.

## Common pitfalls

- Reuse command instances intentionally for `toggleOnTrue` (identity-based).
- Ensure commands reset internal state in `initialize()`.
- Always declare subsystem requirements.
