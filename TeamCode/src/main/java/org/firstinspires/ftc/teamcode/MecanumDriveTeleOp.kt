package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.nextftc.NextFtcLinearOpMode
import org.firstinspires.ftc.teamcode.nextftc.get
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
    private lateinit var frontLeft: DcMotor
    private lateinit var frontRight: DcMotor
    private lateinit var backLeft: DcMotor
    private lateinit var backRight: DcMotor
    private lateinit var imu: IMU

    override fun onInit() {
        frontLeft = hardwareMap.get("frontLeft")
        frontRight = hardwareMap.get("frontRight")
        backLeft = hardwareMap.get("backLeft")
        backRight = hardwareMap.get("backRight")
        imu = hardwareMap.get("imu")

        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
            )
        )

        frontLeft.direction = DcMotorSimple.Direction.FORWARD
        backLeft.direction = DcMotorSimple.Direction.FORWARD
        frontRight.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.REVERSE

        listOf(frontLeft, frontRight, backLeft, backRight).forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
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
