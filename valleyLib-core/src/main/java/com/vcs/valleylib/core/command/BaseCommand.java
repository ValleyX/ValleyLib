package com.vcs.valleylib.core.command;

import com.vcs.valleylib.core.subsystem.Subsystem;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseCommand implements Command {

    private boolean initialized = false;
    private boolean finished = false;
    private boolean interrupted = false;
    private boolean justFinished = false;

    @Override
    public final void initialize() {
        initialized = true;
        finished = false;
        interrupted = false;
        justFinished = false;
        onInitialize();
    }

    @Override
    public final void execute() {
        if (!finished) {
            onExecute();
            if (onIsFinished()) {
                finished = true;
            }
        }
    }

    @Override
    public final void end(boolean interrupted) {
        this.interrupted = interrupted;
        this.justFinished = true;
        onEnd(interrupted);
    }

    @Override
    public final boolean isFinished() {
        return finished;
    }

    protected void onInitialize() {}
    protected void onExecute() {}
    protected void onEnd(boolean interrupted) {}
    protected boolean onIsFinished() { return false; }

    public boolean justFinished() {
        boolean value = justFinished;
        justFinished = false;
        return value;
    }

    public boolean wasInterrupted() {
        return interrupted;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return new HashSet<>();
    }
}