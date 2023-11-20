package com.example.iccoa2.apdu

import com.example.iccoa2.KeyId
import com.example.iccoa2.apdu.CommandApduListDk.Companion.KEY_ID_STATUS_TAG
import com.example.iccoa2.apdu.CommandApduListDk.Companion.KEY_ID_TAG
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduListDk {
    companion object {
        const val LIST_DK_INS: UByte = 0x60u
        const val LIST_ALL_DK_P1: UByte = 0x00u
        const val LIST_SPEC_DK_P1: UByte = 0x01u
        const val LIST_DK_P2: UByte = 0x00u
        const val LIST_DK_LE: UByte = 0x00u
        const val KEY_ID_STATUS_TAG: Int = 0x88
        const val KEY_ID_TAG: Int = 0x89
    }
    var cla: UByte = 0x00u
    var keyId: KeyId? = null

    fun serialize(): UByteArray {
        val header = CommandApduHeader().apply {
            this.ins = LIST_DK_INS
            if (keyId != null) {
                this.p1 = LIST_SPEC_DK_P1
            } else {
                this.p1 = LIST_ALL_DK_P1
            }
            this.p2 = LIST_DK_P2
        }
        header.cla = cla
        val trailer = CommandApduTrailer().apply {
            this.data = if (keyId != null) {
                BerTlvBuilder().run {
                    this.addBytes(BerTag(KEY_ID_TAG), keyId!!.serialize().toByteArray())
                    this.buildArray().toUByteArray()
                }
            } else {
                null
            }
            this.le = LIST_DK_LE
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
        if (header.ins != LIST_DK_INS ||
            header.p2 != LIST_DK_P2) {
            return
        }
        if ((trailer == null) ||
            (trailer.le != LIST_DK_LE)
        ) {
            return
        }
        if ((header.p1 == LIST_SPEC_DK_P1) &&
            (trailer.data == null)
        ) {
            return
        }
        cla = header.cla
        keyId = if (header.p1 == LIST_SPEC_DK_P1) {
            KeyId().apply {
                this.deserialize(
                    BerTlvParser().run {
                        this.parse(trailer.data!!.toByteArray()).run {
                            this.find(BerTag(KEY_ID_TAG)).run {
                                this.bytesValue.toUByteArray()
                            }
                        }
                    }
                )
            }
        } else {
            null
        }
    }
}

enum class KeyIdStatus(val value: UByte) {
    UNDELIVERD(0x01u),
    DELIVERD(0x02u),
    ACTIVATED(0x03u),
    SUSPENDED(0x04u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduListDk {
    var keyId: KeyId = KeyId()
    var keyIdStatus: KeyIdStatus = KeyIdStatus.UNDELIVERD
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return arrayListOf<UByte>().run {
            this.addAll(
                BerTlvBuilder().run {
                    this.addBytes(BerTag(KEY_ID_TAG), keyId.serialize().toByteArray())
                    this.buildArray().toUByteArray()
                }
            )
            this.addAll(
                BerTlvBuilder().run {
                    this.addByte(BerTag(KEY_ID_STATUS_TAG), keyIdStatus.value.toByte())
                    this.buildArray().toUByteArray()
                }
            )
            this.addAll(status.serialize())
            this.toUByteArray()
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
            this.parse(buffer.copyOfRange(0, buffer.size-2).toByteArray())
        }
        if (berTlvParse.find(BerTag(KEY_ID_TAG)) == null ||
            berTlvParse.find(BerTag(KEY_ID_STATUS_TAG)) == null) {
            return
        }
        keyId = KeyId().apply {
            this.deserialize(
                berTlvParse.find(BerTag(KEY_ID_TAG)).run {
                    this!!.bytesValue.toUByteArray()
                }
            )
        }
        keyIdStatus = KeyIdStatus.values().first {
            it.value == berTlvParse.find(BerTag(KEY_ID_STATUS_TAG)).run {
                this!!.bytesValue.toUByteArray()[0]
            }
        }
        status = response.trailer
    }
}
