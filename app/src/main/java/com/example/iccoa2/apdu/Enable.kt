package com.example.iccoa2.apdu

import com.example.iccoa2.KeyId
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduEnable {
    companion object {
        const val INS: UByte = 0x6Fu
        const val P1: UByte = 0x00u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
        const val KEY_ID_TAG: Int = 0x89
    }
    var cla: UByte = 0x00u
    var keyId: KeyId = KeyId()

    fun serialize(): UByteArray {
        return CommandApdu().run {
            this.header = CommandApduHeader().apply {
                this.ins = INS
                this.p1 = P1
                this.p2 = P2
            }
            this.header.cla = cla
            this.trailer = CommandApduTrailer().apply {
                this.data = BerTlvBuilder().run {
                    this.addBytes(BerTag(KEY_ID_TAG), keyId.serialize().toByteArray())
                    this.buildArray().toUByteArray()
                }
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
        if (header.ins != INS ||
            header.p1 != P1 ||
            header.p2 != P2) {
            return
        }
        if (trailer?.data == null ||
            trailer.le == null ||
            trailer.le != LE) {
            return
        }
        val tlv = BerTlvParser().run {
            this.parse(trailer.data!!.toByteArray())
        }
        cla = header.cla
        keyId = KeyId().apply {
            this.deserialize(
                tlv.find(BerTag(KEY_ID_TAG)).run {
                    this.bytesValue.toUByteArray()
                }
            )
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduEnable {
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = null
            this.trailer = status
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data != null) {
            return
        }
        status = response.trailer
    }
}
