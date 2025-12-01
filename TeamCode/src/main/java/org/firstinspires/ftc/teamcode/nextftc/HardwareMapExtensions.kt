package org.firstinspires.ftc.teamcode.nextftc

import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Kotlin-friendly helper to fetch hardware devices without explicitly passing the Java class.
 */
inline fun <reified T> HardwareMap.get(name: String): T = get(T::class.java, name)

fun HardwareMap.motor(name: String): MotorEx = MotorEx.fromHardware(this, name)
