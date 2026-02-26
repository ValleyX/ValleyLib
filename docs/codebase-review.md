# Codebase Review and Improvement Suggestions

This document summarizes practical improvements identified while reviewing ValleyLib.

## High-impact improvement completed

### Fix default command scheduling behavior in `CommandScheduler`

Problem:
- A subsystem default command was scheduled every loop even when another command already owned that subsystem.
- Because scheduling enforces mutual exclusion, this could unintentionally cancel active commands.

Fix applied:
- Default commands now schedule only when their subsystem has no active requirement owner.
- Scheduling now ignores commands that are already active, preventing repeated `initialize()` calls.

Why it matters:
- Prevents default command thrashing.
- Makes active command behavior deterministic.
- Aligns with typical command-based framework expectations.

## Additional recommended improvements

### 1) Add timeout/deadline utilities in core

Current state:
- There is `WaitCommand` and FTC-specific `TimedCommand`.

Recommendation:
- Add a core `TimedCommand` decorator so all integrations can use timeouts consistently.

### 2) Provide scheduler observability hooks

Current state:
- No direct way to inspect currently scheduled commands.

Recommendation:
- Add read-only diagnostics methods such as:
  - `Set<Command> getScheduledCommands()`
  - `Map<Subsystem, Command> getRequirementsSnapshot()`

### 3) Add richer command group semantics

Current state:
- Core includes only all-finish parallel behavior.

Recommendation:
- Add:
  - race groups (finish when first finishes)
  - deadline groups (one command defines end condition)

### 4) Improve test coverage for lifecycle edge cases

Suggested tests:
- interruption ordering (`end(true)` timing)
- nested command groups with shared requirements
- default command recovery after interrupted command finishes

### 5) Add package-level docs/Javadocs

Recommendation:
- Add package docs in `core.command`, `core.scheduler`, and `core.subsystem` to describe architecture and lifecycle responsibilities.

## Suggested documentation additions

- Add a top-level README with:
  - module overview (`valleyLib-core`, `valleyLib-ftc`)
  - quick-start snippet for scheduler + subsystem registration
  - link to `docs/custom-command-guide.md`
- Add migration notes when scheduler behavior changes.
