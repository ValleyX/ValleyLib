package com.vcs.valleylib.core.scheduler;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandSchedulerTest {

    private CommandScheduler scheduler;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = CommandScheduler.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        scheduler = CommandScheduler.getInstance();
    }

    @Test
    void defaultCommandDoesNotInterruptRunningCommand() {
        TestSubsystem subsystem = new TestSubsystem();
        AtomicInteger driveExecutions = new AtomicInteger();
        AtomicInteger holdExecutions = new AtomicInteger();

        scheduler.registerSubsystem(subsystem);

        Command drivingCommand = new CountingCommand(driveExecutions, subsystem, false);
        subsystem.setDefaultCommand(new CountingCommand(holdExecutions, subsystem, false));

        scheduler.schedule(drivingCommand);
        scheduler.run();

        assertEquals(1, driveExecutions.get(), "Driving command should run once");
        assertEquals(0, holdExecutions.get(), "Default command should not interrupt active command");
    }

    @Test
    void schedulingSameCommandTwiceDoesNotReinitialize() {
        AtomicInteger initializeCalls = new AtomicInteger();
        AtomicInteger executeCalls = new AtomicInteger();

        Command command = new Command() {
            @Override
            public void initialize() {
                initializeCalls.incrementAndGet();
            }

            @Override
            public void execute() {
                executeCalls.incrementAndGet();
            }
        };

        scheduler.schedule(command);
        scheduler.schedule(command);
        scheduler.run();

        assertEquals(1, initializeCalls.get(), "Command should only initialize once");
        assertEquals(1, executeCalls.get(), "Command should still execute once during run");
    }

    private static final class TestSubsystem extends Subsystem {
    }

    private static final class CountingCommand implements Command {
        private final AtomicInteger counter;
        private final Subsystem requirement;
        private final boolean finishesImmediately;

        private CountingCommand(AtomicInteger counter, Subsystem requirement, boolean finishesImmediately) {
            this.counter = counter;
            this.requirement = requirement;
            this.finishesImmediately = finishesImmediately;
        }

        @Override
        public void execute() {
            counter.incrementAndGet();
        }

        @Override
        public boolean isFinished() {
            return finishesImmediately;
        }

        @Override
        public Set<Subsystem> getRequirements() {
            return Set.of(requirement);
        }
    }
}
