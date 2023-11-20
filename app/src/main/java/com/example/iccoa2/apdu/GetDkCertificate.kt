package com.example.iccoa2.apdu

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

enum class DkCertificateType(val value: UByte) {
    VehicleCA(0x01u),
    VehicleMasterKey(0x02u),
    TempSharedCert(0x03u),
    FriendKey(0x04u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandAPduGetDkCertificate {
    companion object {
        const val INS: UByte = 0x64u
        const val P1: UByte = 0x00u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
    }
    var cla: UByte = 0x00u
    var dkType: DkCertificateType = DkCertificateType.VehicleCA

    fun serialize(): UByteArray {
        return CommandApdu().run {
            this.header = CommandApduHeader().apply {
                this.ins = INS
                this.p1 = P1
                this.p2 = P2
            }
            this.header.cla = cla
            this.trailer = CommandApduTrailer().apply {
                this.data = ubyteArrayOf(dkType.value)
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
        cla = header.cla
        dkType = DkCertificateType.values().first {
            it.value == trailer.data!![0]
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduGetDkCertificate {
    companion object {
        const val FIRST_TAG: Int = 0x7F
        const val VEHICLE_CA_TAG: Int = 0x40
        const val VEHICLE_MASTER_KEY_TAG: Int = 0x42
        const val TEMP_SHARED_CERT_TAG: Int = 0x44
        const val FRIEND_KEY_TAG: Int = 0x46
        const val CERT_TAG: Int = 0x01
    }
    var dkType: DkCertificateType = DkCertificateType.VehicleCA
    var dkCert: UByteArray = ubyteArrayOf()
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = BerTlvBuilder().run {
                val secondTag = when (dkType) {
                    DkCertificateType.VehicleCA -> VEHICLE_CA_TAG
                    DkCertificateType.VehicleMasterKey -> VEHICLE_MASTER_KEY_TAG
                    DkCertificateType.TempSharedCert -> TEMP_SHARED_CERT_TAG
                    DkCertificateType.FriendKey -> FRIEND_KEY_TAG
                }

                this.addBytes(BerTag(FIRST_TAG, secondTag), BerTlvBuilder().run {
                    this.addBytes(BerTag(CERT_TAG), dkCert.toByteArray())
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
        } ?: return
        if (tlv.find(BerTag(CERT_TAG)) == null) {
            return
        }
        dkType = if (tlv.find(BerTag(FIRST_TAG, VEHICLE_CA_TAG)) != null) {
            DkCertificateType.VehicleCA
        } else if (tlv.find(BerTag(FIRST_TAG, VEHICLE_MASTER_KEY_TAG)) != null) {
            DkCertificateType.VehicleMasterKey
        } else if (tlv.find(BerTag(FIRST_TAG, TEMP_SHARED_CERT_TAG)) != null) {
            DkCertificateType.TempSharedCert
        } else if (tlv.find(BerTag(FIRST_TAG, FRIEND_KEY_TAG)) != null) {
            DkCertificateType.FriendKey
        } else {
            return
        }
        dkCert = tlv.find(BerTag(CERT_TAG)).run {
            this.bytesValue.toUByteArray()
        }
        status = response.trailer
    }
}