# Creating a Custom Command

This guide shows the recommended way to create your own command in `valleyLib`.

## 1) Implement `Command`

At minimum, implement `execute()`. You will usually also implement:

- `initialize()` for setup/reset work.
- `isFinished()` to define completion.
- `end(boolean interrupted)` for cleanup.
- `getRequirements()` to claim subsystem ownership.

```java
import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.Set;

public class MoveArmToPositionCommand implements Command {
    private final ArmSubsystem arm;
    private final double targetTicks;

    public MoveArmToPositionCommand(ArmSubsystem arm, double targetTicks) {
        this.arm = arm;
        this.targetTicks = targetTicks;
    }

    @Override
    public void initialize() {
        arm.setTargetPosition(targetTicks);
    }

    @Override
    public void execute() {
        arm.updateClosedLoop();
    }

    @Override
    public boolean isFinished() {
        return arm.isAtTarget();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.of(arm);
    }
}
```

## 2) Keep command responsibilities narrow

A command should express one behavior clearly, such as:

- move arm to target
- spin intake until sensor triggers
- follow path
- wait for a condition

Then compose bigger autonomous flows using `SequentialCommandGroup` and `ParallelCommandGroup`.

## 3) Always declare requirements

If a command controls a subsystem, include it in `getRequirements()`. This lets the scheduler prevent multiple commands from fighting over the same hardware.

## 4) Use the lifecycle correctly

- `initialize()`: one-time setup (reset timers, set state)
- `execute()`: repeated loop work
- `isFinished()`: completion condition
- `end(...)`: cleanup, stop motors if needed

A good default pattern:

- Start/arm in `initialize()`
- Closed-loop update in `execute()`
- Return true once your goal is reached
- Safe shutdown in `end(...)`

## 5) Prefer helper command types when useful

- `InstantCommand`: one-shot action
- `WaitCommand`: fixed delay
- `TimedCommand` (FTC module): time-bounded behavior with `onStart/onLoop/onEnd`

## 6) Register and schedule

- Register all subsystems with `CommandScheduler`.
- Set subsystem default commands for idle behavior.
- Schedule autonomous/triggered commands from your `OpMode` or bindings.

## 7) Common mistakes

- Forgetting `getRequirements()`
- Leaving motors running on interruption (missing cleanup in `end(true)`)
- Doing heavy blocking work inside `execute()`
- Building monolithic commands instead of small composable ones
