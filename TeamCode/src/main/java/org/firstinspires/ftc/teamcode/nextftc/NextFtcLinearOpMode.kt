package org.firstinspires.ftc.teamcode.nextftc

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

/**
 * Minimal helper base to mirror the structure used by NextFTC examples while
 * still running on the standard FTC SDK. It offers convenience utilities for
 * looping while the OpMode is active.
 */
abstract class NextFtcLinearOpMode : LinearOpMode() {
    /** Configure hardware and any init-time telemetry. Called once when the OpMode begins init. */
    protected open fun onInit() {}

    /**
     * Optional hook for an init loop. Called repeatedly while the driver station displays the
     * init state (before start is pressed) to mirror the behavior of NextFTC base classes.
     */
    protected open fun onInitLoop() {}

    /** Called once after start is pressed, before the active loop begins. */
    protected open fun onStart() {}

    /** Called repeatedly while the OpMode is active. Must be implemented by subclasses. */
    protected abstract fun onUpdate()

    /** Called once after the active loop ends, for cleanup. */
    protected open fun onStop() {}

    final override fun runOpMode() {
        onInit()

        while (opModeInInit()) {
            onInitLoop()
            idle()
        }

        waitForStart()
        if (isStopRequested) return

        onStart()
        while (opModeIsActive()) {
            onUpdate()
            idle()
        }

        onStop()
    }
}
