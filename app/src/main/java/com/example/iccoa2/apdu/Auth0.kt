package com.example.iccoa2.apdu

import com.example.iccoa2.VehicleId
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class P1(val value: UByte) {
    Standard(0x00u),
    Fast(0x01u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduAuth0 {
    companion object {
        const val INS: UByte = 0x67u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
        const val VERSION_TAG: Int = 0x5A
        const val VEHICLE_ID_TAG: Int = 0x83
        const val VEHICLE_TEMP_PUB_KEY_TAG: Int = 0x81
        const val RANDOM_TAG: Int = 0x55
    }
    var cla: UByte = 0x00u
    var p1: P1 = P1.Standard
    var version: UShort = 0x0000u
    var vehicleId: VehicleId = VehicleId()
    var vehicleTempPubKey: UByteArray = ubyteArrayOf()
    var random: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        val header = CommandApduHeader().apply {
            this.ins = INS
            this.p2 = P2
        }
        header.cla = cla
        header.p1 = p1.value
        val trailer = CommandApduTrailer().apply {
            this.data = BerTlvBuilder().run {
                this.addBytes(BerTag(VERSION_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.putShort(version.toShort())
                    this.array()
                })
                this.addBytes(BerTag(VEHICLE_ID_TAG), vehicleId.serialize().toByteArray())
                this.addBytes(BerTag(VEHICLE_TEMP_PUB_KEY_TAG), vehicleTempPubKey.toByteArray())
                this.addBytes(BerTag(RANDOM_TAG), random.toByteArray())
                this.buildArray().toUByteArray()
            }
            this.le = LE
        }
        return CommandApdu().run {
            this.header = header
            this.trailer = trailer
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val request = CommandApdu().apply {
            this.deserialize(buffer)
        }
        val header = request.header
        val trailer = request.trailer
        if (header.ins != INS ||
            header.p2 != P2) {
            return
        }
        if (trailer?.data == null ||
            trailer.le == null) {
            return
        }
        cla = header.cla
        p1 = P1.values().first { it.value == header.p1 }
        val berTlvParse = BerTlvParser().run {
            this.parse(trailer.data!!.toByteArray())
        }
        version = berTlvParse.find(BerTag(VERSION_TAG)).run {
            ByteBuffer.wrap(this.bytesValue).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            }
        }
        vehicleId = VehicleId().apply {
            this.deserialize(
                berTlvParse.find(BerTag(VEHICLE_ID_TAG)).run {
                    this.bytesValue.toUByteArray()
                }
            )
        }
        vehicleTempPubKey = berTlvParse.find(BerTag(VEHICLE_TEMP_PUB_KEY_TAG)).run {
            this.bytesValue.toUByteArray()
        }
        random = berTlvParse.find(BerTag(RANDOM_TAG)).run {
            this.bytesValue.toUByteArray()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduAuth0 {
    companion object {
        const val DEVICE_TEMP_PUB_KEY_TAG: Int = 0x84
        const val CRYPTO_GRAM_TAG: Int = 0x85
    }
    var deviceTempPubKey: UByteArray = ubyteArrayOf()
    var cryptoGram: UByteArray? = null
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = BerTlvBuilder().run {
                this.addBytes(BerTag(DEVICE_TEMP_PUB_KEY_TAG), deviceTempPubKey.toByteArray())
                if (cryptoGram != null) {
                    this.addBytes(BerTag(CRYPTO_GRAM_TAG), cryptoGram!!.toByteArray())
                }
                this.buildArray().toUByteArray()
            }
            this.trailer = status
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data == null) {
            return
        }
        val berTlvParse = BerTlvParser().run {
            this.parse(response.data!!.toByteArray())
        }
        deviceTempPubKey = berTlvParse.find(BerTag(DEVICE_TEMP_PUB_KEY_TAG)).run {
            this.bytesValue.toUByteArray()
        }
        cryptoGram = if (berTlvParse.find(BerTag(CRYPTO_GRAM_TAG)) != null) {
            berTlvParse.find(BerTag(CRYPTO_GRAM_TAG)).run {
                this.bytesValue.toUByteArray()
            }
        } else {
            null
        }
        status = response.trailer
    }
}
