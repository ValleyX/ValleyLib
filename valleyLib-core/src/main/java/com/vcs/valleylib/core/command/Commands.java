package com.vcs.valleylib.core.command;

import com.vcs.valleylib.core.command.decorators.DeadlineCommand;
import com.vcs.valleylib.core.command.decorators.ParallelCommandGroup;
import com.vcs.valleylib.core.command.decorators.RaceCommand;
import com.vcs.valleylib.core.command.decorators.SequentialCommandGroup;

import java.util.function.BooleanSupplier;

/**
 * Static factories for common command construction patterns.
 */
public final class Commands {

    private Commands() {}

    public static Command none() {
        return new InstantCommand(() -> {});
    }

    public static Command runOnce(Runnable action) {
        return new InstantCommand(action);
    }

    public static Command run(Runnable action) {
        return new Command() {
            @Override
            public void execute() {
                action.run();
            }
        };
    }

    public static Command startEnd(Runnable onStart, Runnable onEnd) {
        return new Command() {
            @Override
            public void initialize() {
                onStart.run();
            }

            @Override
            public void execute() {}

            @Override
            public void end(boolean interrupted) {
                onEnd.run();
            }
        };
    }

    public static Command waitSeconds(double seconds) {
        return new WaitCommand(seconds);
    }

    public static Command waitUntil(BooleanSupplier condition) {
        return new WaitUntilCommand(condition);
    }

    public static Command sequence(Command... commands) {
        return new SequentialCommandGroup(commands);
    }

    public static Command parallel(Command... commands) {
        return new ParallelCommandGroup(commands);
    }

    public static Command race(Command... commands) {
        return new RaceCommand(commands);
    }

    public static Command deadline(Command deadline, Command... others) {
        return new DeadlineCommand(deadline, others);
    }
}
