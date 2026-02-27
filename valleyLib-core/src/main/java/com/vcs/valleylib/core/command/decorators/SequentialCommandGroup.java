package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.BaseCommand;
import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SequentialCommandGroup extends BaseCommand {

    private final Command[] commands;
    private int currentIndex;

    public SequentialCommandGroup(Command... commands) {
        this.commands = commands;
    }

    @Override
    protected void onInitialize() {
        currentIndex = 0;
        if (commands.length > 0) {
            commands[0].initialize();
        }
    }

    @Override
    protected void onExecute() {
        if (currentIndex >= commands.length) {
            return;
        }

        Command current = commands[currentIndex];
        current.execute();

        if (current.isFinished()) {
            current.end(false);
            currentIndex++;
            if (currentIndex < commands.length) {
                commands[currentIndex].initialize();
            }
        }
    }

    @Override
    protected boolean onIsFinished() {
        return currentIndex >= commands.length;
    }

    @Override
    protected void onEnd(boolean interrupted) {
        if (interrupted && currentIndex < commands.length) {
            commands[currentIndex].end(true);
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        Set<Subsystem> requirements = new LinkedHashSet<>();
        Arrays.stream(commands).forEach(command -> requirements.addAll(command.getRequirements()));
        return requirements;
    }
}
