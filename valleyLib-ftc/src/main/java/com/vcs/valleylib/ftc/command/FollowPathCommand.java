package com.vcs.valleylib.ftc.pedro;

import com.pedropathing.paths.PathChain;
import com.vcs.valleylib.core.command.Command;
import java.util.Set;

/**
 * A command that makes a Pedro Pathing follower drive along a PathChain.
 * <p>
 * The command finishes when the follower reports it is no longer busy.
 * While running, no other command with the same subsystem requirement
 * (typically your drive subsystem) can run.
 */
public class FollowPathCommand implements Command {

    private final PedroSubsystem drive;
    private final PathChain path;
    private final double maxPower;

    public FollowPathCommand(PedroSubsystem drive, PathChain path, double maxPower) {
        this.drive = drive;
        this.path = path;
        this.maxPower = maxPower;
    }

    @Override
    public void initialize() {
        // Start the path with a capped max power
        drive.getFollower().setMaxPower(maxPower);
        drive.getFollower().followPath(path);
    }

    @Override
    public void execute() {
        // Nothing else to run here; follower.update() is handled by the drive subsystem’s periodic()
        // This method *must* exist because Command.execute() is not defaulted.
    }

    @Override
    public boolean isFinished() {
        // The command ends when the follower is no longer busy
        return !drive.getFollower().isBusy();
    }

    @Override
    public Set<com.vcs.valleylib.core.subsystem.Subsystem> getRequirements() {
        // The follower needs exclusive access to the drive subsystem
        return Set.of(drive);
    }
}