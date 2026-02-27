package com.vcs.valleylib.ftc.input;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.scheduler.CommandScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriggerTest {

    @AfterEach
    void tearDown() {
        CommandScheduler.getInstance().reset();
    }

    @Test
    void onChangeSchedulesOnBothEdges() {
        AtomicBoolean state = new AtomicBoolean(false);
        TriggerManager manager = new TriggerManager();
        CountingCommand command = new CountingCommand();

        manager.bind(new Trigger(state::get).onChange(command));

        manager.poll();
        state.set(true);
        manager.poll();
        CommandScheduler.getInstance().run();

        state.set(false);
        manager.poll();
        CommandScheduler.getInstance().run();

        assertEquals(2, command.initializeCalls);
    }

    @Test
    void andCompositionRequiresBothConditions() {
        AtomicBoolean a = new AtomicBoolean(false);
        AtomicBoolean b = new AtomicBoolean(false);
        TriggerManager manager = new TriggerManager();
        CountingCommand command = new CountingCommand();

        manager.bind(new Trigger(a::get).and(b::get).onTrue(command));

        manager.poll();
        a.set(true);
        manager.poll();
        b.set(true);
        manager.poll();

        assertEquals(1, command.initializeCalls);
    }

    private static class CountingCommand implements Command {
        int initializeCalls;

        @Override
        public void initialize() {
            initializeCalls++;
        }

        @Override
        public void execute() {}
    }
}
