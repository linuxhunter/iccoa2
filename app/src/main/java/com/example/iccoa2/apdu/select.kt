package com.example.iccoa2.apdu

import java.nio.ByteBuffer
import java.nio.ByteOrder

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
        val header = CommandApduHeader().apply {
            this.cla = CLA
            this.ins = INS
            this.p1 = P1
            this.p2 = P2
        }
        val trailer = CommandApduTrailer().apply {
            this.data = aid
            this.le = LE
        }
        val request = CommandApdu().apply {
            this.header = header
            this.trailer = trailer
        }
        return request.serialize()
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
        if (trailer?.le != LE) {
            return
        }
        if (trailer?.data?.isEmpty() == true) {
            return
        }
        aid = trailer?.data!!
    }
}

class ResponseApduSelect {
    var version: UShort = 0x0000u
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        val buffer = ByteBuffer.allocate(Short.SIZE_BYTES)
        buffer.order(ByteOrder.BIG_ENDIAN)
        buffer.putShort(version.toShort())
        val response = ResponseApdu().apply {
            this.data = buffer.array().toUByteArray()
            this.trailer = status
        }
        return response.serialize()
    }
    @OptIn(ExperimentalStdlibApi::class)
    fun deserialize(buffer: UByteArray) {
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data?.isEmpty() == true) {
            return
        }
        val data = response.data!!
        val buffer = ByteBuffer.wrap(data.toByteArray(), 0, 2).apply {
            this.order(ByteOrder.BIG_ENDIAN)
        }
        version = buffer.short.toUShort()
        status = response.trailer
    }
}
