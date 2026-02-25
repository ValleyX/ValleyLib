package com.vcs.valleylib.ftc.command;

import com.pedropathing.util.Timer;
import com.vcs.valleylib.core.command.Command;

/**
 * Command that runs for a fixed duration.
 * Preferred over raw Timer usage in autos.
 */
public abstract class TimedCommand implements Command {

    private final double durationSeconds;
    private final Timer timer = new Timer();

    protected TimedCommand(double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    @Override
    public void initialize() {
        timer.resetTimer();
        onStart();
    }

    @Override
    public void execute() {
        onLoop(timer.getElapsedTimeSeconds());
    }

    @Override
    public boolean isFinished() {
        return timer.getElapsedTimeSeconds() >= durationSeconds;
    }

    @Override
    public void end(boolean interrupted) {
        onEnd(interrupted);
    }

    protected abstract void onStart();
    protected abstract void onLoop(double elapsedSeconds);
    protected abstract void onEnd(boolean interrupted);
}