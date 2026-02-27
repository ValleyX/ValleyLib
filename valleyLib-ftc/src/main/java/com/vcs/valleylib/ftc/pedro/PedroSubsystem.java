package com.vcs.valleylib.ftc.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vcs.valleylib.core.command.Command;
import com.vcs.valleylib.core.command.InstantCommand;
import com.vcs.valleylib.core.command.WaitUntilCommand;
import com.vcs.valleylib.ftc.hardware.FtcSubsystem;

/**
 * Base subsystem for robots using Pedro Pathing.
 * <p>
 * Owns the Follower and ensures update() is called
 * consistently through the command scheduler.
 */
public abstract class PedroSubsystem extends FtcSubsystem {

    protected final Follower follower;

    protected PedroSubsystem(HardwareMap hardwareMap, Follower follower) {
        super(hardwareMap);
        this.follower = follower;
    }

    @Override
    public void periodic() {
        follower.update();
    }

    public Follower getFollower() {
        return follower;
    }

    /**
     * Command helper to follow a path with default max power (1.0).
     */
    public Command follow(PathChain path) {
        return new FollowPathCommand(this, path, 1.0);
    }

    /**
     * Command helper to follow a path with capped max power.
     */
    public Command follow(PathChain path, double maxPower) {
        return new FollowPathCommand(this, path, maxPower);
    }

    /**
     * Command helper that finishes when the follower is no longer busy.
     */
    public Command waitUntilIdle() {
        return new WaitUntilCommand(() -> !follower.isBusy());
    }

    /**
     * Sets follower max power as a one-shot command.
     */
    public Command setMaxPower(double maxPower) {
        return new InstantCommand(() -> follower.setMaxPower(maxPower));
    }
}
