# Codebase Review and Improvement Suggestions

This review focuses on command scheduling behavior, API clarity, and maintainability.

## Changes already applied

1. **Command groups now publish combined requirements**
   - `SequentialCommandGroup` and `ParallelCommandGroup` now report the union of all child command requirements.
   - This allows the scheduler to correctly enforce subsystem mutual exclusion for grouped commands.

2. **Command groups now forward interruption cleanup**
   - When a group is interrupted, active child commands are ended with `interrupted=true`.
   - This avoids leaked actuator state when a grouped command is canceled.

3. **Expanded API documentation**
   - Added module-level docs and a complete custom command guide.
   - Added detailed Javadocs to `TimedCommand` hooks.

## Additional recommended improvements

1. **Default command scheduling policy**
   - Current scheduler can schedule a default command even while another command owns that subsystem if the default command does not declare requirements correctly.
   - Consider checking subsystem ownership in `CommandScheduler.run()` before scheduling defaults.

2. **Command identity + reusability contract**
   - Some commands are stateful (`InstantCommand` retains `hasRun`).
   - Consider documenting whether command instances are single-use or reusable after finishing.

3. **Deterministic timing abstraction**
   - `WaitCommand` depends on `System.currentTimeMillis()`.
   - For testability and simulation, consider injecting a clock source abstraction.

4. **Thread-safety expectations**
   - Scheduler collections are not synchronized (which is usually correct for FTC main-loop usage).
   - Document single-threaded usage assumptions to prevent misuse.

5. **Testing improvements**
   - Add unit tests for:
     - requirement conflict cancellation
     - group interruption semantics
     - default command scheduling behavior
     - timed command completion boundaries
