package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.BaseCommand;
import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class DeadlineCommand extends BaseCommand {

    private final Command deadline;
    private final Command[] others;

    public DeadlineCommand(Command deadline, Command... others) {
        this.deadline = deadline;
        this.others = others;
    }

    @Override
    protected void onInitialize() {
        deadline.initialize();
        for (Command c : others) c.initialize();
    }

    @Override
    protected void onExecute() {
        deadline.execute();
        for (Command c : others) c.execute();
    }

    @Override
    protected boolean onIsFinished() {
        return deadline.isFinished();
    }

    @Override
    protected void onEnd(boolean interrupted) {
        deadline.end(interrupted);
        for (Command c : others) c.end(true);
    }

    @Override
    public Set<Subsystem> getRequirements() {
        Set<Subsystem> requirements = new LinkedHashSet<>(deadline.getRequirements());
        Arrays.stream(others).forEach(command -> requirements.addAll(command.getRequirements()));
        return requirements;
    }
}
