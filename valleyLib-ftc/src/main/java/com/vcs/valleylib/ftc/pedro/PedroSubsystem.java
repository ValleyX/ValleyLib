package com.vcs.valleylib.ftc.pedro;

import com.pedropathing.follower.Follower;
import com.vcs.valleylib.ftc.hardware.FtcSubsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Base subsystem for robots using Pedro Pathing.
 *
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
}