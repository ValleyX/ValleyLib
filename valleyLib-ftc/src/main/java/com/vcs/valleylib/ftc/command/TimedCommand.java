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

    /**
     * Called once when the command starts.
     * Use this to reset actuators/state before loop updates begin.
     */
    protected abstract void onStart();

    /**
     * Called every scheduler loop while the command is active.
     *
     * @param elapsedSeconds elapsed time since initialize()
     */
    protected abstract void onLoop(double elapsedSeconds);

    /**
     * Called once when the command ends naturally or is interrupted.
     *
     * @param interrupted true if canceled before timeout
     */
    protected abstract void onEnd(boolean interrupted);
}