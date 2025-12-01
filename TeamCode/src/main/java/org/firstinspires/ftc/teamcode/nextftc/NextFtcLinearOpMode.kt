package org.firstinspires.ftc.teamcode.nextftc

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

/**
 * Minimal helper base to mirror the structure used by NextFTC examples while
 * still running on the standard FTC SDK. It offers convenience utilities for
 * looping while the OpMode is active.
 */
abstract class NextFtcLinearOpMode : LinearOpMode() {
    /** Waits for start and returns whether the OpMode remained active. */
    protected fun waitForStartOrStop(): Boolean {
        waitForStart()
        return opModeIsActive()
    }

    /** Runs [block] repeatedly while the OpMode stays active. */
    protected inline fun runWhileActive(crossinline block: () -> Unit) {
        while (opModeIsActive()) {
            block()
        }
    }
}
