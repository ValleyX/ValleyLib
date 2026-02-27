package com.vcs.valleylib.core.scheduler;

import com.vcs.valleylib.core.command.Command;

/**
 * Observer interface for command lifecycle events.
 */
public interface CommandSchedulerListener {

    default void onCommandScheduled(Command command) {}

    default void onCommandFinished(Command command) {}

    default void onCommandCanceled(Command command) {}
}
