package org.firstinspires.ftc.teamcode.nextftc

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Lightweight wrapper modeled after the NextFTC examples that prefer MotorEx over raw DcMotor.
 * It keeps the API surface we need for the mecanum teleop while exposing underlying motors when
 * configuration is required.
 */
class MotorEx(private val motor: DcMotorEx) {
    var power: Double
        get() = motor.power
        set(value) {
            motor.power = value
        }

    var direction: DcMotorSimple.Direction
        get() = motor.direction
        set(value) {
            motor.direction = value
        }

    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        get() = motor.zeroPowerBehavior
        set(value) {
            motor.zeroPowerBehavior = value
        }

    /**
     * Access to the underlying DcMotorEx for any advanced configuration calls.
     */
    val rawMotor: DcMotorEx
        get() = motor

    object Direction {
        val FORWARD = DcMotorSimple.Direction.FORWARD
        val REVERSE = DcMotorSimple.Direction.REVERSE
    }

    object ZeroPowerBehavior {
        val BRAKE = DcMotor.ZeroPowerBehavior.BRAKE
        val FLOAT = DcMotor.ZeroPowerBehavior.FLOAT
    }

    companion object {
        fun fromHardware(hardwareMap: HardwareMap, name: String): MotorEx =
            MotorEx(hardwareMap.get(DcMotorEx::class.java, name))
    }
}
