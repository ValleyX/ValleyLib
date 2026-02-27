# Advanced Controls, Logging, and Simulation

This guide covers the newest comprehensive features for a NextFTC-style workflow.

## Logitech F310 controller stack

Use `CommandGamepad` (or compatibility `GamepadEx`) with F310 preset:

```java
CommandGamepad driver = CommandGamepad.forLogitechF310(gamepad1);
```

Preset tuning (XInput mode):

- stick deadband `0.08`
- stick exponent `1.7`
- trigger deadband `0.05`
- trigger exponent `1.5`

Axis methods:

- `leftX()`, `leftY()`, `rightX()`, `rightY()`
- `leftTrigger()`, `rightTrigger()`
- `leftTriggerButton(threshold)`, `rightTriggerButton(threshold)`

## Advanced trigger composition

Trigger pipelines now support logic + temporal shaping:

```java
Trigger precisionAim = driver.leftTriggerButton(0.5)
    .and(driver.rightBumper())
    .debounce(0.05);

triggers.bindAll(
    precisionAim.whileTrue(aim.holdTarget()),
    driver.a().onChange(arm.toggleMode()),
    driver.b().negate().whileTrue(intake.safeIdle())
);
```

Supported helpers:

- `and(...)`, `or(...)`, `negate()`
- `debounce(seconds)`
- `onChange(...)`
- `bindAll(...)`

## Built-in command logging listener

Enable in `CommandOpMode`:

```java
@Override
protected boolean enableCommandLogging() {
    return true;
}
```

Or attach manually with custom listeners:

```java
scheduler.addListener(new CommandSchedulerListener() {
    @Override
    public void onCommandScheduled(Command command) {
        telemetryBus.put("scheduled", command.getClass().getSimpleName());
    }
});
```

`FtcCommandLogger` provides telemetry-backed defaults.

## AutoDsl + command factories

Use both together for concise autonomous code:

```java
Command auto = AutoDsl.auto(a -> a
    .marker("start", tag -> telemetryBus.put("auto", tag))
    .command(Commands.runOnce(intake::closeGate))
    .waitSeconds(0.15)
    .either(
        vision::seesPropLeft,
        drive.follow(leftPath),
        drive.follow(centerPath)
    )
    .deadline(
        drive.follow(backdropPath),
        Commands.run(shooter::spin).withTimeout(1.2)
    )
    .when(() -> sensor.isReady(), Commands.runOnce(outtake::drop))
);
```

## Desktop simulation workflow

Implement `simulationPeriodic()` in subsystems for desktop-only model updates.

Then choose either mode:

1. `scheduler.setSimulationEnabled(true)` and run normal loops.
2. `scheduler.runSimulationStep()` for explicit sim stepping tests.

This allows rapid tuning of command state machines and trajectories before hardware runs.
