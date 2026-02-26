package com.vcs.valleylib.core.command.group;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Runs commands one after another in sequence.
 *
 * The next command does not start until the current one finishes.
 */
public class SequentialCommandGroup implements Command {

    private final Set<Subsystem> requirements = new HashSet<>();

    private final Queue<Command> commands = new ArrayDeque<>();
    private Command current;

    /**
     * Creates a sequential group that runs the provided commands in order.
     *
     * @param commands commands to run one-by-one
     */
    public SequentialCommandGroup(Command... commands) {
        this.commands.addAll(java.util.List.of(commands));
        for (Command command : commands) {
            requirements.addAll(command.getRequirements());
        }
    }

    @Override
    public void initialize() {
        current = commands.poll();
        if (current != null) current.initialize();
    }

    @Override
    public void execute() {
        if (current == null) return;

        current.execute();
        if (current.isFinished()) {
            current.end(false);
            current = commands.poll();
            if (current != null) current.initialize();
        }
    }

    @Override
    public boolean isFinished() {
        return current == null;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted && current != null) {
            current.end(true);
        }
        current = null;
        commands.clear();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.copyOf(requirements);
    }
}