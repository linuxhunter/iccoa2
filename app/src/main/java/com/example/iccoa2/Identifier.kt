package com.example.iccoa2

import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
class KeyId {
    companion object {
        const val KEY_ID_LENGTH = 0x10
        const val DEVICE_OEM_ID_OFFSET = 0x00
        const val DEVICE_OEM_ID_LENGTH = 0x02
        const val VEHICLE_OEM_ID_OFFSET = 0x02
        const val VEHICLE_OEM_ID_LENGTH = 0x02
        const val KEY_SERIAL_ID_OFFSET = 0x04
        const val KEY_SERIAL_ID_LENGTH = 0x0B
    }
    var deviceOemId: UShort = 0x0000u
    var vehicleOemId: UShort = 0x0000u
    var keySerialId: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        val deviceId = ByteBuffer.allocate(Short.SIZE_BYTES).apply {
            this.order(ByteOrder.BIG_ENDIAN)
            this.putShort(deviceOemId.toShort())
        }
        val vehicleId = ByteBuffer.allocate(Short.SIZE_BYTES).apply {
            this.order(ByteOrder.BIG_ENDIAN)
            this.putShort(vehicleOemId.toShort())
        }
        return arrayListOf<UByte>().run {
            this.addAll(deviceId.array().toUByteArray())
            this.addAll(vehicleId.array().toUByteArray())
            this.addAll(keySerialId)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size != KEY_ID_LENGTH) {
            return
        }
        deviceOemId = ByteBuffer.wrap(buffer.toByteArray(), DEVICE_OEM_ID_OFFSET, DEVICE_OEM_ID_LENGTH).let {
            it.order(ByteOrder.BIG_ENDIAN)
            it.short.toUShort()
        }
        vehicleOemId = ByteBuffer.wrap(buffer.toByteArray(), VEHICLE_OEM_ID_OFFSET, VEHICLE_OEM_ID_LENGTH).let {
            it.order(ByteOrder.BIG_ENDIAN)
            it.short.toUShort()
        }
        keySerialId = buffer.copyOfRange(KEY_SERIAL_ID_OFFSET, KEY_SERIAL_ID_OFFSET+ KEY_SERIAL_ID_LENGTH + 1)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class VehicleId {
    companion object {
        const val VEHICLE_ID_LENGTH = 0x10
        const val VEHICLE_OEM_ID_OFFSET = 0x00
        const val VEHICLE_OEM_ID_LENGTH = 0x02
        const val VEHICLE_SERIAL_ID_OFFSET = 0x02
        const val VEHICLE_SERIAL_ID_LENGTH = 0x0D
    }
    var vehicleOemId: UShort = 0x0000u
    var vehicleSerialId: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        val vehicleId = ByteBuffer.allocate(Short.SIZE_BYTES).apply {
            this.order(ByteOrder.BIG_ENDIAN)
            this.putShort(vehicleOemId.toShort())
        }
        return arrayListOf<UByte>().run {
            this.addAll(vehicleId.array().toUByteArray())
            this.addAll(vehicleSerialId)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size != VEHICLE_ID_LENGTH) {
            return
        }
        vehicleOemId = ByteBuffer.wrap(buffer.toByteArray(), VEHICLE_OEM_ID_OFFSET, VEHICLE_OEM_ID_LENGTH).let {
            it.order(ByteOrder.BIG_ENDIAN)
            it.short.toUShort()
        }
        vehicleSerialId = buffer.copyOfRange(VEHICLE_SERIAL_ID_OFFSET, VEHICLE_SERIAL_ID_OFFSET + VEHICLE_SERIAL_ID_LENGTH + 1)
    }
}
