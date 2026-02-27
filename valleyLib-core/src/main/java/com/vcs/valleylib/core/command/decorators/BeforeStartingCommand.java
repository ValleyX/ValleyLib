package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.command.CommandWrapper;

public class BeforeStartingCommand extends CommandWrapper {

    private final Runnable action;

    public BeforeStartingCommand(Command inner, Runnable action) {
        super(inner);
        this.action = action;
    }

    @Override
    protected void onInitialize() {
        action.run();
        super.onInitialize();
    }
}