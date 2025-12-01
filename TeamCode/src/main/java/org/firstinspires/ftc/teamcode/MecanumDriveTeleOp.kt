package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.nextftc.NextFtcLinearOpMode
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
    override fun runOpMode() {
        val frontLeft = hardwareMap.get(DcMotor::class.java, "frontLeft")
        val frontRight = hardwareMap.get(DcMotor::class.java, "frontRight")
        val backLeft = hardwareMap.get(DcMotor::class.java, "backLeft")
        val backRight = hardwareMap.get(DcMotor::class.java, "backRight")
        val imu = hardwareMap.get(IMU::class.java, "imu")

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

        frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        telemetry.addLine("Initialized mecanum drive with NextFTC helpers")
        telemetry.update()

        if (!waitForStartOrStop()) return

        runWhileActive {
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
    }
}
