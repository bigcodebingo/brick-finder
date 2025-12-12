package com.bigbingo.brickfinder.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

class SensorHelper(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private var lastGyroZ = 0f
    private var lastUpdateTime = 0L
    private var isInitialized = false

    private val GYRO_THRESHOLD = 0.8f
    private val TIME_THRESHOLD = 800L

    private val RETURN_THRESHOLD = 12.5f
    private var mustReturnToCenter = false

    var onTiltLeft: (() -> Unit)? = null
    var onTiltRight: (() -> Unit)? = null

    fun startListening() {
        when {
            gyroscope != null -> {
                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        isInitialized = false
        mustReturnToCenter = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val currentTime = System.currentTimeMillis()

            if (!isInitialized) {
                when (it.sensor.type) {
                    Sensor.TYPE_GYROSCOPE -> lastGyroZ = it.values[2]
                }
                lastUpdateTime = currentTime
                isInitialized = true
                return
            }

            if (mustReturnToCenter) {
                val accelY = it.values[1]
                if (abs(accelY) < RETURN_THRESHOLD) {
                    mustReturnToCenter = false
                } else {
                    return
                }
            }

            val timeDelta = currentTime - lastUpdateTime
            if (timeDelta < TIME_THRESHOLD) return

            when (it.sensor.type) {
                Sensor.TYPE_GYROSCOPE -> {
                    val gyroZ = it.values[2]
                    val deltaZ = abs(gyroZ - lastGyroZ)

                    if (deltaZ > GYRO_THRESHOLD) {

                        if (gyroZ > GYRO_THRESHOLD) {
                            onTiltRight?.invoke()
                            lastUpdateTime = currentTime
                            mustReturnToCenter = true
                        } else if (gyroZ < -GYRO_THRESHOLD) {
                            onTiltLeft?.invoke()
                            lastUpdateTime = currentTime
                            mustReturnToCenter = true
                        }
                    }
                    lastGyroZ = gyroZ
                }
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
