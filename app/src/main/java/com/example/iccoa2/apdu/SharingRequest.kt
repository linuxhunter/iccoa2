package com.example.iccoa2.apdu

import com.example.iccoa2.KeyId
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

enum class SharingRequestP1(var value: UByte) {
    FromDeviceApp(0x00u),
    FromVehicleApp(0x01u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduSharingRequest {
    companion object {
        const val INS: UByte = 0x65u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
        const val KEY_ID_TAG: Int = 0x89
        const val INNER_CSR_TAG: Int = 0x02
        const val FIRST_TEMP_CSR_TAG: Int = 0x7F
        const val SECOND_TEMP_CSR_TAG: Int = 0x22
    }
    var cla: UByte = 0x00u
    var p1: SharingRequestP1 = SharingRequestP1.FromDeviceApp
    var keyId: KeyId = KeyId()
    var tempCsr: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return CommandApdu().run {
            this.header = CommandApduHeader().apply {
                this.ins = INS
                this.p2 = P2
            }
            this.header.cla = cla
            this.header.p1 = p1.value
            this.trailer = CommandApduTrailer().apply {
                this.data = BerTlvBuilder().run {
                    this.addBytes(BerTag(KEY_ID_TAG), keyId.serialize().toByteArray())
                    this.addBytes(BerTag(FIRST_TEMP_CSR_TAG, SECOND_TEMP_CSR_TAG), BerTlvBuilder().run {
                        this.addBytes(BerTag(INNER_CSR_TAG), tempCsr.toByteArray())
                        this.buildArray()
                    })
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
            header.p2 != P2) {
            return
        }
        if (trailer?.data == null ||
            trailer.le != LE) {
            return
        }
        val tlv = BerTlvParser().run {
            this.parse(trailer.data!!.toByteArray())
        }
        if (tlv.find(BerTag(KEY_ID_TAG)) == null ||
            tlv.find(BerTag(FIRST_TEMP_CSR_TAG, SECOND_TEMP_CSR_TAG)) == null ||
            tlv.find(BerTag(INNER_CSR_TAG)) == null) {
            return
        }
        cla = header.cla
        p1 = SharingRequestP1.values().first {
            it.value == header.p1
        }
        keyId = KeyId().apply {
            this.deserialize(
                tlv.find(BerTag(KEY_ID_TAG)).run {
                    this.bytesValue.toUByteArray()
                }
            )
        }
        tempCsr = tlv.find(BerTag(INNER_CSR_TAG)).run {
            this.bytesValue.toUByteArray()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduSharingRequest {
    companion object {
        const val INNER_CERT_TAG: Int = 0x03
        const val FIRST_TEMP_CERT_TAG: Int = 0x7F
        const val SECOND_TEMP_CERT_TAG: Int = 0x48
    }
    var tempCert: UByteArray = ubyteArrayOf()
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = BerTlvBuilder().run {
                this.addBytes(BerTag(FIRST_TEMP_CERT_TAG, SECOND_TEMP_CERT_TAG), BerTlvBuilder().run {
                    this.addBytes(BerTag(INNER_CERT_TAG), tempCert.toByteArray())
                    this.buildArray()
                })
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
        if (tlv.find(BerTag(FIRST_TEMP_CERT_TAG, SECOND_TEMP_CERT_TAG)) == null ||
            tlv.find(BerTag(INNER_CERT_TAG)) == null) {
            return
        }
        tempCert = tlv.find(BerTag(INNER_CERT_TAG)).run {
            this.bytesValue.toUByteArray()
        }
        status = response.trailer
    }
}