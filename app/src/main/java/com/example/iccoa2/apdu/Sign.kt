package com.example.iccoa2.apdu

import com.example.iccoa2.KeyId
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduSign {
    companion object {
        const val INS: UByte = 0x67u
        const val P1: UByte = 0x00u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
        const val KEY_ID_TAG: Int = 0x89
        const val DATA_TAG: Int = 0x58
    }
    var cla: UByte = 0x00u
    var keyId: KeyId = KeyId()
    var originData: UByteArray = ubyteArrayOf()

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
                    this.addBytes(BerTag(DATA_TAG), originData.toByteArray())
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
        if (tlv.find(BerTag(KEY_ID_TAG)) == null ||
            tlv.find(BerTag(DATA_TAG)) == null) {
            return
        }
        cla = header.cla
        keyId = KeyId().apply {
            this.deserialize(
                tlv.find(BerTag(KEY_ID_TAG)).run {
                    this.bytesValue.toUByteArray()
                }
            )
        }
        originData = tlv.find(BerTag(DATA_TAG)).run {
            this.bytesValue.toUByteArray()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduSign {
    companion object {
        const val KEY_ID_TAG: Int = 0x89
        const val SIGNATURE_TAG: Int = 0x8F //0x9F
    }
    var keyId: KeyId = KeyId()
    var signature: UByteArray = ubyteArrayOf()
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = BerTlvBuilder().run {
                this.addBytes(BerTag(KEY_ID_TAG), keyId.serialize().toByteArray())
                this.addBytes(BerTag(SIGNATURE_TAG), signature.toByteArray())
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
        val tlv = BerTlvParser().run {
            this.parse(response.data!!.toByteArray())
        }
        if (tlv.find(BerTag(KEY_ID_TAG)) == null ||
            tlv.find(BerTag(SIGNATURE_TAG)) == null) {
            return
        }
        keyId = KeyId().apply {
            this.deserialize(
                tlv.find(BerTag(KEY_ID_TAG)).run {
                    this.bytesValue.toUByteArray()
                }
            )
        }
        signature = tlv.find(BerTag(SIGNATURE_TAG)).run {
            this.bytesValue.toUByteArray()
        }
        status = response.trailer
    }
}
