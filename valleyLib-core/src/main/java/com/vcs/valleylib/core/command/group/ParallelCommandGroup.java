package com.vcs.valleylib.core.command.group;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.HashSet;
import java.util.Set;

/**
 * Runs multiple commands simultaneously.
 *
 * The group finishes when ALL commands have finished.
 */
public class ParallelCommandGroup implements Command {

    private final Set<Command> commands = new HashSet<>();
    private final Set<Subsystem> requirements = new HashSet<>();

    /**
     * Creates a parallel group that runs all provided commands together.
     *
     * @param commands commands to run simultaneously
     */
    public ParallelCommandGroup(Command... commands) {
        this.commands.addAll(java.util.List.of(commands));
        for (Command command : commands) {
            requirements.addAll(command.getRequirements());
        }
    }

    @Override
    public void initialize() {
        for (Command c : commands) {
            c.initialize();
        }
    }

    @Override
    public void execute() {
        commands.removeIf(command -> {
            command.execute();
            if (command.isFinished()) {
                command.end(false);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean isFinished() {
        return commands.isEmpty();
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            for (Command command : commands) {
                command.end(true);
            }
        }
        commands.clear();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.copyOf(requirements);
    }
}