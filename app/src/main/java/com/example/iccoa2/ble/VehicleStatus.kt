package com.example.iccoa2.ble

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class VehicleEntity(val value: UShort) {
    DoorLock(0x0001u),
    CarWindow(0x0002u),
    BackTrunk(0x0003u),
    Engine(0x0004u),
    AirController(0x0005u),
    CarLight(0x0006u),
    Custom(0x1001u)
}

@OptIn(ExperimentalUnsignedTypes::class)
class SubscribeVehicleStatusRequest {
    companion object {
        const val FIRST_SUBSCRIBE_TAG: Int = 0x7F
        const val SECOND_SUBSCRIBE_TAG: Int = 0x73
        const val SUBSCRIBE_MIDDLE_TAG: Int = 0x30
        const val ENTITY_TAG: Int = 0x84
        const val SUBSCRIBE_ALL_TAG: Int = 0x86
    }
    var entity: VehicleEntity? = null

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_SUBSCRIBE_TAG, SECOND_SUBSCRIBE_TAG), BerTlvBuilder().run {
                if (entity == null) {
                    this.addEmpty(BerTag(SUBSCRIBE_ALL_TAG))
                } else {
                    this.addBytes(BerTag(SUBSCRIBE_MIDDLE_TAG), BerTlvBuilder().run {
                        this.addBytes(BerTag(ENTITY_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                            this.order(ByteOrder.BIG_ENDIAN)
                            this.putShort(entity!!.value.toShort())
                            this.array()
                        })
                        this.buildArray()
                    })
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
        if (tlv.find(BerTag(FIRST_SUBSCRIBE_TAG, SECOND_SUBSCRIBE_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(SUBSCRIBE_MIDDLE_TAG)) == null &&
            tlv.find(BerTag(SUBSCRIBE_ALL_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(SUBSCRIBE_MIDDLE_TAG)) != null &&
            tlv.find(BerTag(ENTITY_TAG)) == null) {
            return
        }
        entity = if (tlv.find(BerTag(SUBSCRIBE_ALL_TAG)) != null) {
            null
        } else {
            VehicleEntity.values().first {
                it.value == ByteBuffer.wrap(
                    tlv.find(BerTag(ENTITY_TAG)).run {
                        this.bytesValue
                    }, 0, 2).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.short.toUShort()
                }
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class QueryVehicleStatusRequest {
    companion object {
        const val FIRST_QUERY_TAG: Int = 0x7F
        const val SECOND_QUERY_TAG: Int = 0x74
        const val QUERY_MIDDLE_TAG: Int = 0x30
        const val ENTITY_TAG: Int = 0x84
        const val QUERY_ALL_TAG: Int = 0x86
    }
    var entity: VehicleEntity? = null

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(
                FIRST_QUERY_TAG,
                SECOND_QUERY_TAG
            ), BerTlvBuilder().run {
                if (entity == null) {
                    this.addEmpty(BerTag(QUERY_ALL_TAG))
                } else {
                    this.addBytes(BerTag(QUERY_MIDDLE_TAG), BerTlvBuilder().run {
                        this.addBytes(BerTag(ENTITY_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                            this.order(ByteOrder.BIG_ENDIAN)
                            this.putShort(entity!!.value.toShort())
                            this.array()
                        })
                        this.buildArray()
                    })
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
        if (tlv.find(BerTag(FIRST_QUERY_TAG, SECOND_QUERY_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(QUERY_MIDDLE_TAG)) == null &&
            tlv.find(BerTag(QUERY_ALL_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(QUERY_MIDDLE_TAG)) != null &&
            tlv.find(BerTag(ENTITY_TAG)) == null) {
            return
        }
        entity = if (tlv.find(BerTag(QUERY_ALL_TAG)) != null) {
            null
        } else {
            VehicleEntity.values().first {
                it.value == ByteBuffer.wrap(
                    tlv.find(BerTag(ENTITY_TAG)).run {
                        this.bytesValue
                    }, 0, 2).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.short.toUShort()
                }
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class UnsubscribeVehicleStatusRequest {
    companion object {
        const val FIRST_UNSUBSCRIBE_TAG: Int = 0x7F
        const val SECOND_UNSUBSCRIBE_TAG: Int = 0x75
        const val UNSUBSCRIBE_MIDDLE_TAG: Int = 0x30
        const val ENTITY_TAG: Int = 0x84
        const val UNSUBSCRIBE_ALL_TAG: Int = 0x86
    }
    var entity: VehicleEntity? = null

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_UNSUBSCRIBE_TAG, SECOND_UNSUBSCRIBE_TAG), BerTlvBuilder().run {
                if (entity == null) {
                    this.addEmpty(BerTag(UNSUBSCRIBE_ALL_TAG))
                } else {
                    this.addBytes(BerTag(UNSUBSCRIBE_MIDDLE_TAG), BerTlvBuilder().run {
                        this.addBytes(BerTag(ENTITY_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                            this.order(ByteOrder.BIG_ENDIAN)
                            this.putShort(entity!!.value.toShort())
                            this.array()
                        })
                        this.buildArray()
                    })
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
        if (tlv.find(BerTag(FIRST_UNSUBSCRIBE_TAG, SECOND_UNSUBSCRIBE_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(UNSUBSCRIBE_MIDDLE_TAG)) == null &&
            tlv.find(BerTag(UNSUBSCRIBE_ALL_TAG)) == null) {
            return
        }
        if (tlv.find(BerTag(UNSUBSCRIBE_MIDDLE_TAG)) != null &&
            tlv.find(BerTag(ENTITY_TAG)) == null) {
            return
        }
        entity = if (tlv.find(BerTag(UNSUBSCRIBE_ALL_TAG)) != null) {
            null
        } else {
            VehicleEntity.values().first {
                it.value == ByteBuffer.wrap(
                    tlv.find(BerTag(ENTITY_TAG)).run {
                        this.bytesValue
                    }, 0, 2).run {
                    this.order(ByteOrder.BIG_ENDIAN)
                    this.short.toUShort()
                }
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class SubscribeVerificationResponse {
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
class VehicleStatusResponse {
    companion object {
        const val FIRST_RESPONSE_TAG: Int = 0x7F
        const val SECOND_RESPONSE_TAG: Int = 0x77
        const val FIRST_MIDDLE_RESPONSE_TAG: Int = 0x30
        const val SECOND_MIDDLE_RESPONSE_TAG: Int = 0xA0
        const val ENTITY_TAG: Int = 0x80
        const val STATUS_TAG: Int = 0x89
        const val RANDOM_TAG: Int = 0x8A
    }
    var entity: VehicleEntity = VehicleEntity.DoorLock
    var status: UShort = 0x0000u
    var random: UByteArray? = null

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_RESPONSE_TAG, SECOND_RESPONSE_TAG), BerTlvBuilder().run {
                this.addBytes(BerTag(FIRST_MIDDLE_RESPONSE_TAG), BerTlvBuilder().run {
                    this.addBytes(BerTag(SECOND_MIDDLE_RESPONSE_TAG), BerTlvBuilder().run {
                        this.addBytes(BerTag(ENTITY_TAG), ByteBuffer.allocate(Short.SIZE_BYTES).run {
                            this.order(ByteOrder.BIG_ENDIAN)
                            this.putShort(entity.value.toShort())
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
                this.buildArray()
            })
            if (random != null) {
                this.addBytes(BerTag(RANDOM_TAG), random!!.toByteArray())
            }
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_RESPONSE_TAG, SECOND_RESPONSE_TAG)) == null ||
            tlv.find(BerTag(FIRST_MIDDLE_RESPONSE_TAG)) == null ||
            tlv.find(BerTag(SECOND_MIDDLE_RESPONSE_TAG)) == null ||
            tlv.find(BerTag(ENTITY_TAG)) == null ||
            tlv.find(BerTag(STATUS_TAG)) == null) {
            return
        }
        entity = VehicleEntity.values().first {
            it.value == ByteBuffer.wrap(
                tlv.find(BerTag(ENTITY_TAG)).run {
                    this.bytesValue
                }, 0, 2).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            }
        }
        status = ByteBuffer.wrap(
            tlv.find(BerTag(STATUS_TAG)).run {
                this.bytesValue
            }, 0, 2).run {
            this.order(ByteOrder.BIG_ENDIAN)
            this.short.toUShort()
        }
        random = if (tlv.find(BerTag(RANDOM_TAG)) == null) {
            null
        } else {
            tlv.find(BerTag(RANDOM_TAG)).run {
                this.bytesValue.toUByteArray()
            }
        }
    }
}
