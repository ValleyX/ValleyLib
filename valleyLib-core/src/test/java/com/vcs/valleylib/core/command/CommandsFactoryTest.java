package com.vcs.valleylib.core.command;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandsFactoryTest {

    @Test
    void startEndRunsBothCallbacks() {
        AtomicBoolean started = new AtomicBoolean(false);
        AtomicBoolean ended = new AtomicBoolean(false);

        Command command = Commands.startEnd(() -> started.set(true), () -> ended.set(true));
        command.initialize();
        command.end(false);

        assertTrue(started.get());
        assertTrue(ended.get());
    }

    @Test
    void waitUntilFinishesWhenConditionBecomesTrue() {
        AtomicBoolean condition = new AtomicBoolean(false);
        Command command = Commands.waitUntil(condition::get);

        command.initialize();
        command.execute();
        assertFalse(command.isFinished());

        condition.set(true);
        assertTrue(command.isFinished());
    }
}
