package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.BaseCommand;
import com.vcs.valleylib.core.command.Command;

public class RaceCommand extends BaseCommand {

    private final Command[] commands;

    public RaceCommand(Command... commands) {
        this.commands = commands;
    }

    @Override
    protected void onInitialize() {
        for (Command c : commands) {
            c.initialize();
        }
    }

    @Override
    protected void onExecute() {
        for (Command c : commands) {
            c.execute();
        }
    }

    @Override
    protected boolean onIsFinished() {
        for (Command c : commands) {
            if (c.isFinished()) return true;
        }
        return false;
    }

    @Override
    protected void onEnd(boolean interrupted) {
        for (Command c : commands) {
            c.end(true);
        }
    }
}