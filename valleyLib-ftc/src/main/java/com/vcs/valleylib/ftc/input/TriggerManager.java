package com.vcs.valleylib.ftc.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for command trigger bindings.
 */
public class TriggerManager {

    private final List<Runnable> bindings = new ArrayList<>();

    public Trigger bind(Trigger trigger) {
        bindings.addAll(trigger.getBindings());
        return trigger;
    }

    public void bindAll(Trigger... triggers) {
        for (Trigger trigger : triggers) {
            bind(trigger);
        }
    }

    public void poll() {
        for (Runnable binding : bindings) {
            binding.run();
        }
    }

    public void clear() {
        bindings.clear();
    }
}
