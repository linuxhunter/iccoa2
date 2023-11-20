package com.example.iccoa2.ble

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlv
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class RkeFunctionAndAction(val function: UShort, val action: UByte) {
    DoorLock(0x0001u, 0x01u),
    DoorUnlock(0x0001u, 0x02u),
    CarWindowFullOpen(0x0002u, 0x01u),
    CarWindowFullClose(0x0002u, 0x02u),
    CarWindowPartialOpen(0x0002u, 0x03u),
    BackTrunkOpen(0x0003u, 0x01u),
    BackTrunkClose(0x0003u, 0x02u),
    EngineStart(0x0004u, 0x01u),
    EngineStop(0x0004u, 0x02u),
    FindVehicleFlashing(0x0005u, 0x01u),
    FindVehicleWhistling(0x0005u, 0x02u),
    FindVehicleFlashingAndWhistling(0x0005u, 0x03u),
    Custom(0x1001u, 0x00u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class RkeRequest {
    companion object {
        const val FIRST_RKE_REQUEST_TAG: Int = 0x7F
        const val SECOND_RKE_REQUEST_TAG: Int = 0x70
        const val FUNCTION_TAG: Int = 0x80
        const val ACTION_TAG: Int = 0x81
    }
    var rke: RkeFunctionAndAction = RkeFunctionAndAction.DoorLock

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG), BerTlvBuilder().run {
                this.addBytes(BerTag(FUNCTION_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.putShort(rke.function.toShort())
                    this.array()
                })
                this.addByte(BerTag(ACTION_TAG), rke.action.toByte())
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG)) == null) {
            return
        }
        deserializeFromTlv(tlv.find(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG)))
    }
    fun deserializeFromTlv(tlv: BerTlv) {
        if (tlv.find(BerTag(FUNCTION_TAG)) == null ||
            tlv.find(BerTag(ACTION_TAG)) == null) {
            return
        }
        rke = RkeFunctionAndAction.values().first {
            it.function == ByteBuffer.wrap(
                tlv.find(BerTag(FUNCTION_TAG)).run {
                    this.bytesValue
                }, 0, 2
            ).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            } && it.action == tlv.find(BerTag(ACTION_TAG)).run {
                this.bytesValue[0].toUByte()
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class RkeContinuedRequest {
    companion object {
        const val FIRST_RKE_CONTINUED_REQUEST_TAG: Int = 0x7F
        const val SECOND_RKE_CONTINUED_REQUEST_TAG: Int = 0x76
        const val FUNCTION_TAG: Int = 0x80
        const val ACTION_TAG: Int = 0x81
        const val CUSTOM_TAG: Int = 0x88
    }
    var rke: RkeFunctionAndAction = RkeFunctionAndAction.DoorLock
    var custom: UByteArray? = null

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_RKE_CONTINUED_REQUEST_TAG, SECOND_RKE_CONTINUED_REQUEST_TAG), BerTlvBuilder().run {
                this.addBytes(BerTag(FUNCTION_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.putShort(rke.function.toShort())
                    this.array()
                })
                this.addByte(BerTag(ACTION_TAG), rke.action.toByte())
                if (custom != null) {
                    this.addBytes(BerTag(CUSTOM_TAG), custom!!.toByteArray())
                }
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_RKE_CONTINUED_REQUEST_TAG, SECOND_RKE_CONTINUED_REQUEST_TAG)) == null ||
            tlv.find(BerTag(FUNCTION_TAG)) == null ||
            tlv.find(BerTag(ACTION_TAG)) == null) {
            return
        }
        rke = RkeFunctionAndAction.values().first {
            it.function == ByteBuffer.wrap(
                tlv.find(BerTag(RkeRequest.FUNCTION_TAG)).run {
                    this.bytesValue
                }, 0, 2
            ).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            } && it.action == tlv.find(BerTag(RkeRequest.ACTION_TAG)).run {
                this.bytesValue[0].toUByte()
            }
        }
        custom = if (tlv.find(BerTag(CUSTOM_TAG)) != null) {
            tlv.find(BerTag(CUSTOM_TAG)).run {
                this.bytesValue.toUByteArray()
            }
        } else {
            null
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class RkeVerificationResponse {
    companion object {
        const val RANDOM_TAG: Int = 0x8A
    }
    var random: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(RANDOM_TAG), random.toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(RANDOM_TAG)) == null) {
            return
        }
        random = tlv.find(BerTag(RANDOM_TAG)).run {
            this.bytesValue.toUByteArray()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class RkeResponse {
    companion object {
        const val FIRST_RKE_RESPONSE_TAG: Int = 0x7F
        const val SECOND_RKE_RESPONSE_TAG: Int = 0x72
        const val RKE_RESPONSE_MIDDLE_TAG: Int = 0xA0
        const val FUNCTION_TAG: Int = 0x80
        const val ACTION_TAG: Int = 0x83
        const val STATUS_TAG: Int = 0x89
    }
    var rke: RkeFunctionAndAction = RkeFunctionAndAction.DoorLock
    var status: UShort = 0x0000u

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_RKE_RESPONSE_TAG, SECOND_RKE_RESPONSE_TAG), BerTlvBuilder().run {
                this.addBytes(BerTag(RKE_RESPONSE_MIDDLE_TAG), BerTlvBuilder().run {
                    this.addBytes(BerTag(FUNCTION_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                        this.order(ByteOrder.BIG_ENDIAN)
                        this.putShort(rke.function.toShort())
                        this.array()
                    })
                    this.addBytes(BerTag(ACTION_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                        this.order(ByteOrder.BIG_ENDIAN)
                        this.putShort(rke.action.toShort())
                        this.array()
                    })
                    this.addBytes(BerTag(STATUS_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                        this.order(ByteOrder.BIG_ENDIAN)
                        this.putShort(status.toShort())
                        this.array()
                    })
                    this.buildArray()
                })
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_RKE_RESPONSE_TAG, SECOND_RKE_RESPONSE_TAG)) == null ||
            tlv.find(BerTag(RKE_RESPONSE_MIDDLE_TAG)) == null ||
            tlv.find(BerTag(FUNCTION_TAG)) == null ||
            tlv.find(BerTag(ACTION_TAG)) == null ||
            tlv.find(BerTag(STATUS_TAG)) == null) {
            return
        }
        rke = RkeFunctionAndAction.values().first {
            it.function == ByteBuffer.wrap(
                tlv.find(BerTag(FUNCTION_TAG)).run {
                    this.bytesValue
                }, 0, 2
            ).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            } && it.action == tlv.find(BerTag(ACTION_TAG)).run {
                this.bytesValue[1].toUByte()
            }
        }
        status = ByteBuffer.wrap(
            tlv.find(BerTag(STATUS_TAG)).run {
                this.bytesValue
            }, 0, 2
        ).run {
            this.order(ByteOrder.BIG_ENDIAN)
            this.short.toUShort()
        }
    }
}
