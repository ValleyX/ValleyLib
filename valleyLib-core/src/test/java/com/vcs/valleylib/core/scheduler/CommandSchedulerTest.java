package com.vcs.valleylib.core.scheduler;

import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.subsystem.Subsystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandSchedulerTest {

    @AfterEach
    void tearDown() {
        CommandScheduler.getInstance().reset();
    }

    @Test
    void scheduleDoesNotReinitializeAlreadyScheduledCommand() {
        CommandScheduler scheduler = CommandScheduler.getInstance();
        CountingCommand command = new CountingCommand(Set.of());

        scheduler.schedule(command);
        scheduler.schedule(command);

        assertEquals(1, command.initializeCalls);
    }

    @Test
    void defaultCommandWaitsUntilSubsystemIsIdle() {
        CommandScheduler scheduler = CommandScheduler.getInstance();
        TestSubsystem subsystem = new TestSubsystem();
        scheduler.registerSubsystem(subsystem);

        CountingCommand defaultCommand = new CountingCommand(Set.of(subsystem));
        subsystem.setDefaultCommand(defaultCommand);

        CountingCommand activeCommand = new CountingCommand(Set.of(subsystem));
        scheduler.schedule(activeCommand);
        scheduler.run();

        assertEquals(0, defaultCommand.initializeCalls);

        scheduler.cancel(activeCommand);
        scheduler.run();

        assertEquals(1, defaultCommand.initializeCalls);
    }

    @Test
    void listenersReceiveScheduleFinishAndCancelCallbacks() {
        CommandScheduler scheduler = CommandScheduler.getInstance();
        List<String> events = new ArrayList<>();

        scheduler.addListener(new CommandSchedulerListener() {
            @Override
            public void onCommandScheduled(Command command) {
                events.add("scheduled");
            }

            @Override
            public void onCommandFinished(Command command) {
                events.add("finished");
            }

            @Override
            public void onCommandCanceled(Command command) {
                events.add("canceled");
            }
        });

        FinishesAfterOneExecuteCommand done = new FinishesAfterOneExecuteCommand();
        scheduler.schedule(done);
        scheduler.run();

        CountingCommand neverDone = new CountingCommand(Set.of());
        scheduler.schedule(neverDone);
        scheduler.cancel(neverDone);

        assertEquals(List.of("scheduled", "finished", "scheduled", "canceled"), events);
    }

    @Test
    void simulationPeriodicRunsWhenSimulationEnabled() {
        CommandScheduler scheduler = CommandScheduler.getInstance();
        TestSubsystem subsystem = new TestSubsystem();
        scheduler.registerSubsystem(subsystem);

        scheduler.setSimulationEnabled(true);
        scheduler.run();

        assertEquals(1, subsystem.simulationCalls);
        assertEquals(1, subsystem.periodicCalls);
    }

    private static class TestSubsystem extends Subsystem {

        private int periodicCalls;
        private int simulationCalls;

        @Override
        public void periodic() {
            periodicCalls++;
        }

        @Override
        public void simulationPeriodic() {
            simulationCalls++;
        }
    }

    private static class CountingCommand implements Command {

        private final Set<Subsystem> requirements;
        private int initializeCalls;

        private CountingCommand(Set<Subsystem> requirements) {
            this.requirements = requirements;
        }

        @Override
        public void initialize() {
            initializeCalls++;
        }

        @Override
        public void execute() {}

        @Override
        public Set<Subsystem> getRequirements() {
            return requirements;
        }
    }

    private static class FinishesAfterOneExecuteCommand implements Command {

        private boolean finished;

        @Override
        public void initialize() {
            finished = false;
        }

        @Override
        public void execute() {
            finished = true;
        }

        @Override
        public boolean isFinished() {
            return finished;
        }
    }
}
