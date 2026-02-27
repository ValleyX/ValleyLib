package com.vcs.valleylib.core.command.decorators;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.command.CommandWrapper;

public class TimeoutCommand extends CommandWrapper {

    private final long timeoutMillis;
    private long startTime;

    public TimeoutCommand(Command inner, long timeoutMillis) {
        super(inner);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    protected void onInitialize() {
        startTime = System.currentTimeMillis();
        super.onInitialize();
    }

    @Override
    protected boolean onIsFinished() {
        return inner.isFinished() ||
                System.currentTimeMillis() - startTime >= timeoutMillis;
    }
}