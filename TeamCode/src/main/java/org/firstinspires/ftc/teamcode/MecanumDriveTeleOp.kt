package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.nextftc.NextFtcLinearOpMode
import org.firstinspires.ftc.teamcode.nextftc.get
import org.firstinspires.ftc.teamcode.nextftc.motor
import org.firstinspires.ftc.teamcode.nextftc.MotorEx
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Field-centric mecanum teleop implemented in Kotlin using a lightweight
 * NextFTC-style base class for loop structure.
 */
@TeleOp(name = "Mecanum Drive", group = "Drive")
class MecanumDriveTeleOp : NextFtcLinearOpMode() {
    private lateinit var frontLeft: MotorEx
    private lateinit var frontRight: MotorEx
    private lateinit var backLeft: MotorEx
    private lateinit var backRight: MotorEx
    private lateinit var imu: IMU

    override fun onInit() {
        frontLeft = hardwareMap.motor("frontLeft")
        frontRight = hardwareMap.motor("frontRight")
        backLeft = hardwareMap.motor("backLeft")
        backRight = hardwareMap.motor("backRight")
        imu = hardwareMap.get("imu")

        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
            )
        )

        frontLeft.direction = MotorEx.Direction.FORWARD
        backLeft.direction = MotorEx.Direction.FORWARD
        frontRight.direction = MotorEx.Direction.REVERSE
        backRight.direction = MotorEx.Direction.REVERSE

        listOf(frontLeft, frontRight, backLeft, backRight).forEach {
            it.zeroPowerBehavior = MotorEx.ZeroPowerBehavior.BRAKE
            it.power = 0.0
        }

        telemetry.addLine("Initialized mecanum drive with NextFTC helpers")
        telemetry.update()
    }

    override fun onStart() {
        telemetry.addLine("Starting field-centric control")
        telemetry.update()
    }

    override fun onUpdate() {
        val y = -gamepad1.left_stick_y.toDouble()
        val x = gamepad1.left_stick_x.toDouble()
        val rx = gamepad1.right_stick_x.toDouble()

        val botHeading = imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)

        val rotX = x * cos(-botHeading) - y * sin(-botHeading)
        val rotY = x * sin(-botHeading) + y * cos(-botHeading)

        val denominator = max(abs(rotY) + abs(rotX) + abs(rx), 1.0)
        val frontLeftPower = (rotY + rotX + rx) / denominator
        val backLeftPower = (rotY - rotX + rx) / denominator
        val frontRightPower = (rotY - rotX - rx) / denominator
        val backRightPower = (rotY + rotX - rx) / denominator

        frontLeft.power = frontLeftPower
        backLeft.power = backLeftPower
        frontRight.power = frontRightPower
        backRight.power = backRightPower

        telemetry.addData("Front Left", frontLeftPower)
        telemetry.addData("Front Right", frontRightPower)
        telemetry.addData("Back Left", backLeftPower)
        telemetry.addData("Back Right", backRightPower)
        telemetry.addData("Heading (deg)", imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES))
        telemetry.update()
    }

    override fun onStop() {
        listOf(frontLeft, frontRight, backLeft, backRight).forEach { it.power = 0.0 }
    }
}
