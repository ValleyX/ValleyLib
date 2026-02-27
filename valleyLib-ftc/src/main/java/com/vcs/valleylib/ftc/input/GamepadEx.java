package com.vcs.valleylib.ftc.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Backward-compatible alias for CommandGamepad.
 */
public class GamepadEx extends CommandGamepad {

    public GamepadEx(Gamepad gamepad) {
        super(gamepad);
    }
}
