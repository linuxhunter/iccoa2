package com.example.iccoa2.apdu

import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduSelect {
    companion object {
        const val CLA: UByte = 0x00u
        const val INS: UByte = 0xA4u
        const val P1: UByte = 0x04u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
    }
    var aid: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return CommandApdu().run {
            this.header = CommandApduHeader().apply {
                this.cla = CLA
                this.ins = INS
                this.p1 = P1
                this.p2 = P2
            }
            this.trailer = CommandApduTrailer().apply {
                this.data = aid
                this.le = LE
            }
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val request = CommandApdu().apply {
            this.deserialize(buffer)
        }
        val header = request.header
        val trailer = request.trailer
        if (header.cla != CLA ||
            header.ins != INS ||
            header.p1 != P1 ||
            header.p2 != P2) {
            return
        }
        if (trailer == null ||
            trailer.le != LE ||
            trailer.data == null ||
            trailer.data?.isEmpty() == true) {
            return
        }
        aid = trailer.data!!
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduSelect {
    var version: UShort = 0x0000u
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = ByteBuffer.allocate(Short.SIZE_BYTES).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.putShort(version.toShort())
                this.array().toUByteArray()
            }
            this.trailer = status
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data?.isEmpty() == true) {
            return
        }
        val data = response.data!!
        version = ByteBuffer.wrap(data.toByteArray(), 0, 2).run {
            this.order(ByteOrder.BIG_ENDIAN)
            this.short.toUShort()
        }
        status = response.trailer
    }
}
