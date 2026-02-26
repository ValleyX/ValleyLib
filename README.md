# ValleyLib

ValleyLib is a modular command-based robotics library.

## Modules

- `valleyLib-core`: platform-agnostic command scheduler, command interfaces, and command groups.
- `valleyLib-ftc`: FTC-specific integration layer and command helpers.

## Quick start

```java
CommandScheduler scheduler = CommandScheduler.getInstance();

scheduler.registerSubsystem(drivetrain);
scheduler.registerSubsystem(intake);

scheduler.schedule(new SequentialCommandGroup(
        new InstantCommand(() -> intake.deploy()),
        new WaitCommand(0.5),
        new FollowPathCommand(follower, preloadPath)
));

// In OpMode loop:
scheduler.run();
```

## Documentation

- [Creating a custom command](docs/custom-command-guide.md)
- [Codebase review and suggested improvements](docs/codebase-review.md)
