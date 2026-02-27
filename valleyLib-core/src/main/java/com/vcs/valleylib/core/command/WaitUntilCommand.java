package com.vcs.valleylib.core.command;

import java.util.function.BooleanSupplier;

/**
 * Finishes once a condition evaluates to true.
 */
public class WaitUntilCommand implements Command {

    private final BooleanSupplier condition;

    public WaitUntilCommand(BooleanSupplier condition) {
        this.condition = condition;
    }

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean();
    }
}
