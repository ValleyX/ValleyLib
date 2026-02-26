# Creating a Custom Command

This guide explains the recommended way to create custom commands in ValleyLib.

## Command lifecycle

All commands implement `com.vcs.valleylib.core.command.Command` and follow this lifecycle:

1. `initialize()` runs once when scheduled.
2. `execute()` runs every scheduler cycle.
3. `isFinished()` is checked every scheduler cycle.
4. `end(interrupted)` runs once when the command finishes or is canceled.

## Minimal template

```java
public final class OpenClawCommand implements Command {

    private final ClawSubsystem claw;

    public OpenClawCommand(ClawSubsystem claw) {
        this.claw = claw;
    }

    @Override
    public void initialize() {
        claw.open();
    }

    @Override
    public void execute() {
        // Optional: maintain output or run closed-loop logic
    }

    @Override
    public boolean isFinished() {
        return true; // one-shot action
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.of(claw);
    }
}
```

## Best practices

- Declare subsystem requirements in `getRequirements()` so the scheduler can prevent conflicts.
- Keep hardware details inside subsystem classes; commands should orchestrate behavior.
- Use `initialize()` for reset/setup work and `end()` for cleanup/safe-stop.
- Make command classes immutable where possible by using `final` fields.
- Prefer composing behavior with `SequentialCommandGroup` and `ParallelCommandGroup` over large monolithic commands.

## Composition examples

### Sequential behavior

```java
Command auto = new SequentialCommandGroup(
        new InstantCommand(() -> intakeSubsystem.deploy()),
        new WaitCommand(0.25),
        new FollowPathCommand(pathFollower, preloadPath),
        new InstantCommand(() -> intakeSubsystem.stow())
);
```

### Parallel behavior

```java
Command scoreAndDrive = new ParallelCommandGroup(
        new FollowPathCommand(pathFollower, toBackdropPath),
        new TimedCommand(new SpinShooterCommand(shooterSubsystem, 1.0), 2.0)
);
```

## Scheduling commands

Schedule commands through `CommandScheduler` from your OpMode loop or from command triggers:

```java
CommandScheduler scheduler = CommandScheduler.getInstance();
scheduler.schedule(new OpenClawCommand(clawSubsystem));
```

Make sure your OpMode repeatedly calls:

```java
scheduler.run();
```

## When to use built-in command types

- `InstantCommand`: one-shot actions
- `WaitCommand`: simple delays in command groups
- `SequentialCommandGroup`: strictly ordered tasks
- `ParallelCommandGroup`: concurrent tasks
