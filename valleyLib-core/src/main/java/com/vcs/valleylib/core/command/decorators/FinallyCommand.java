package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.command.CommandWrapper;

public class FinallyCommand extends CommandWrapper {

    private final Runnable action;

    public FinallyCommand(Command inner, Runnable action) {
        super(inner);
        this.action = action;
    }

    @Override
    protected void onEnd(boolean interrupted) {
        super.onEnd(interrupted);
        action.run();
    }
}