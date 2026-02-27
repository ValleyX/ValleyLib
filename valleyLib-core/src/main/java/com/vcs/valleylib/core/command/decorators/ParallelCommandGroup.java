package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.BaseCommand;
import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParallelCommandGroup extends BaseCommand {

    private final Command[] commands;
    private final Set<Command> running = new LinkedHashSet<>();

    public ParallelCommandGroup(Command... commands) {
        this.commands = commands;
    }

    @Override
    protected void onInitialize() {
        running.clear();
        running.addAll(Arrays.asList(commands));
        for (Command command : running) {
            command.initialize();
        }
    }

    @Override
    protected void onExecute() {
        running.removeIf(command -> {
            command.execute();
            if (command.isFinished()) {
                command.end(false);
                return true;
            }
            return false;
        });
    }

    @Override
    protected boolean onIsFinished() {
        return running.isEmpty();
    }

    @Override
    protected void onEnd(boolean interrupted) {
        if (interrupted) {
            for (Command command : running) {
                command.end(true);
            }
            running.clear();
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        Set<Subsystem> requirements = new LinkedHashSet<>();
        Arrays.stream(commands).forEach(command -> requirements.addAll(command.getRequirements()));
        return requirements;
    }
}
