package com.example.iccoa2.ble

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
class VehicleAppCustomRequest {
    companion object {
        const val VEHICLE_APP_TAG: Int = 0x80
    }
    var data: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(VEHICLE_APP_TAG), data.toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        data = tlv.find(BerTag(VEHICLE_APP_TAG)).run {
            this?.bytesValue?.toUByteArray() ?: return
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class VehicleAppCustomResponse {
    companion object {
        const val VEHICLE_APP_TAG: Int = 0x81
    }
    var data: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(VEHICLE_APP_TAG), data.toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        data = tlv.find(BerTag(VEHICLE_APP_TAG)).run {
            this?.bytesValue?.toUByteArray() ?: return
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class VehicleServerCustomRequest {
    companion object {
        const val MINI_LENGTH = 0x03
        const val OFFSET_OFFSET = 0x00
        const val OFFSET_LENGTH = 0x02
        const val LENGTH_OFFSET = 0x02
        const val VEHICLE_SERVER_TAG: Int = 0x82
    }
    var offset: UShort = 0x0000u
    var length: UByte = 0x00u

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(VEHICLE_SERVER_TAG), ByteBuffer.allocate(Short.SIZE_BYTES+Byte.SIZE_BYTES).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.putShort(offset.toShort())
                this.put(length.toByte())
                this.array()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        val data = tlv.find(BerTag(VEHICLE_SERVER_TAG)).run {
            this?.bytesValue ?: return
        }
        if (data.size != MINI_LENGTH) {
            return
        }
        offset = ByteBuffer.wrap(data, OFFSET_OFFSET, OFFSET_LENGTH).run {
            this.order(ByteOrder.BIG_ENDIAN)
            this.short.toUShort()
        }
        length = data[LENGTH_OFFSET].toUByte()
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class VehicleServerCustomResponse {
    companion object {
        const val VEHICLE_SERVER_TAG: Int = 0x83
    }
    var data: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(VEHICLE_SERVER_TAG), data.toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        data = tlv.find(BerTag(VEHICLE_SERVER_TAG)).run {
            this?.bytesValue?.toUByteArray() ?: return
        }
    }
}
