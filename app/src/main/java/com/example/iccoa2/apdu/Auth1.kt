package com.example.iccoa2.apdu

import com.example.iccoa2.VehicleId
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

@OptIn(ExperimentalUnsignedTypes::class)
class Auth1Data {
    companion object {
        const val VEHICLE_ID_TAG: Int = 0x83
        const val DEVICE_TEMP_PUB_KEY_TAG: Int = 0x84
        const val VEHICLE_TEMP_PUB_KEY_TAG: Int = 0x81
        const val RANDOM_TAG: Int = 0x55
    }
    var vehicleId: VehicleId = VehicleId()
    var deviceTempPubKey: UByteArray = ubyteArrayOf()
    var vehicleTempPubKey: UByteArray = ubyteArrayOf()
    var random: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return arrayListOf<UByte>().run {
            this.addAll(
                BerTlvBuilder().run {
                    this.addBytes(BerTag(VEHICLE_ID_TAG), vehicleId.serialize().toByteArray())
                    this.buildArray().toUByteArray()
                }
            )
            this.addAll(
                BerTlvBuilder().run {
                    this.addBytes(BerTag(DEVICE_TEMP_PUB_KEY_TAG), deviceTempPubKey.toByteArray())
                    this.buildArray().toUByteArray()
                }
            )
            this.addAll(
                BerTlvBuilder().run {
                    this.addBytes(BerTag(VEHICLE_TEMP_PUB_KEY_TAG), vehicleTempPubKey.toByteArray())
                    this.buildArray().toUByteArray()
                }
            )
            this.addAll(
                BerTlvBuilder().run {
                    this.addBytes(BerTag(RANDOM_TAG), random.toByteArray())
                    this.buildArray().toUByteArray()
                }
            )
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val berTlvParse = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        if (berTlvParse.find(BerTag(VEHICLE_ID_TAG)) == null ||
            berTlvParse.find(BerTag(DEVICE_TEMP_PUB_KEY_TAG)) == null ||
            berTlvParse.find(BerTag(VEHICLE_TEMP_PUB_KEY_TAG)) == null ||
            berTlvParse.find(BerTag(RANDOM_TAG)) == null) {
            return
        }
        vehicleId = VehicleId().apply {
            this.deserialize(
                berTlvParse.run {
                    this.find(BerTag(VEHICLE_ID_TAG)).run {
                        this!!.bytesValue.toUByteArray()
                    }
                }
            )
        }
        deviceTempPubKey = berTlvParse.find(BerTag(DEVICE_TEMP_PUB_KEY_TAG)).run {
            this!!.bytesValue.toUByteArray()
        }
        vehicleTempPubKey = berTlvParse.find(BerTag(VEHICLE_TEMP_PUB_KEY_TAG)).run {
            this!!.bytesValue.toUByteArray()
        }
        random = berTlvParse.find(BerTag(RANDOM_TAG)).run {
            this!!.bytesValue.toUByteArray()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduAuth1 {
    companion object {
        const val SIGNATURE_TAG: Int = 0x8F //0x9F
    }
    var data: Auth1Data = Auth1Data()

    fun signature(): UByteArray {
        return ubyteArrayOf(
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
        )
    }
    fun verify(sign: UByteArray): Boolean {
        return true
    }
    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(SIGNATURE_TAG), signature().toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray): UByteArray {
        val berTlvParse = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        return berTlvParse.run {
            this.find(BerTag(SIGNATURE_TAG)).run {
                this.bytesValue.toUByteArray()
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduAuth1 {
    var data: Auth1Data = Auth1Data()

    fun signature(): UByteArray {
        return ubyteArrayOf(
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
        )
    }
    fun verify(sign: UByteArray): Boolean {
        return true
    }
    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(CommandApduAuth1.SIGNATURE_TAG), signature().toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray): UByteArray {
        val berTlvParse = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        return berTlvParse.run {
            this.find(BerTag(CommandApduAuth1.SIGNATURE_TAG)).run {
                this.bytesValue.toUByteArray()
            }
        }
    }
}
