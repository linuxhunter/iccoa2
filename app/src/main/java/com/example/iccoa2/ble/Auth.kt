package com.example.iccoa2.ble

import com.example.iccoa2.KeyId
import com.example.iccoa2.ble.QueryVehicleStatusRequest.Companion.FIRST_QUERY_TAG
import com.example.iccoa2.ble.QueryVehicleStatusRequest.Companion.SECOND_QUERY_TAG
import com.example.iccoa2.ble.RkeRequest.Companion.FIRST_RKE_REQUEST_TAG
import com.example.iccoa2.ble.RkeRequest.Companion.SECOND_RKE_REQUEST_TAG
import com.example.iccoa2.ble.SubscribeVehicleStatusRequest.Companion.FIRST_SUBSCRIBE_TAG
import com.example.iccoa2.ble.SubscribeVehicleStatusRequest.Companion.SECOND_SUBSCRIBE_TAG
import com.example.iccoa2.ble.UnsubscribeVehicleStatusRequest.Companion.FIRST_UNSUBSCRIBE_TAG
import com.example.iccoa2.ble.UnsubscribeVehicleStatusRequest.Companion.SECOND_UNSUBSCRIBE_TAG
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

@OptIn(ExperimentalUnsignedTypes::class)
class AuthRandomRequest {
    companion object {
        const val AUTH_RANDOM_TAG: Int = 0x8B
    }

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addEmpty(BerTag(AUTH_RANDOM_TAG))
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        if (tlv.find(BerTag(AUTH_RANDOM_TAG)) == null) {
            return
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class AuthRequest {
    companion object {
        const val AUTH_REQUEST_TAG: Int = 0x8A
    }
    var random: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(AUTH_REQUEST_TAG), random.toByteArray())
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        } ?: return
        random = tlv.find(BerTag(AUTH_REQUEST_TAG)).run {
            this?.bytesValue?.toUByteArray() ?: return
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class AuthResponse {
    companion object {
        const val FIRST_AUTH_RESPONSE_TAG: Int = 0x7F
        const val SECOND_AUTH_RESPONSE_TAG: Int = 0x2D
        const val KEY_ID_TAG: Int = 0x5D
        const val SIGNATURE_TAG: Int = 0x9E
    }
    var keyId: KeyId = KeyId()
    var rke: RkeRequest? = null
    var subscribe: SubscribeVehicleStatusRequest? = null
    var query: QueryVehicleStatusRequest? = null
    var unsubscribe: UnsubscribeVehicleStatusRequest? = null
    var signature: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_AUTH_RESPONSE_TAG, SECOND_AUTH_RESPONSE_TAG), BerTlvBuilder().run {
                this.addBytes(BerTag(KEY_ID_TAG), keyId.serialize().toByteArray())
                rke?.let {
                    val tlv = BerTlvParser().run {
                        this.parse(it.serialize().toByteArray())
                    }
                    this.addBerTlv(tlv.find(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG)))
                }
                subscribe?.let {
                    val tlv = BerTlvParser().run {
                        this.parse(it.serialize().toByteArray())
                    }
                    this.addBerTlv(tlv.find(BerTag(FIRST_SUBSCRIBE_TAG, SECOND_SUBSCRIBE_TAG)))
                }
                query?.let {
                    val tlv = BerTlvParser().run {
                        this.parse(it.serialize().toByteArray())
                    }
                    this.addBerTlv(tlv.find(BerTag(FIRST_QUERY_TAG, SECOND_QUERY_TAG)))
                }
                unsubscribe?.let {
                    val tlv = BerTlvParser().run {
                        this.parse(it.serialize().toByteArray())
                    }
                    this.addBerTlv(tlv.find(BerTag(FIRST_UNSUBSCRIBE_TAG, SECOND_UNSUBSCRIBE_TAG)))
                }
                this.addBytes(BerTag(SIGNATURE_TAG), signature.toByteArray())
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray()) ?: return
        }
        keyId = KeyId().apply {
            this.deserialize(tlv.find(BerTag(KEY_ID_TAG)).run {
                this?.bytesValue?.toUByteArray() ?: return
            })
        }

        if (tlv.find(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG)) != null) {
            rke = RkeRequest().apply {
                this.deserializeFromTlv(tlv.find(BerTag(FIRST_RKE_REQUEST_TAG, SECOND_RKE_REQUEST_TAG)))
            }
        }
        if (tlv.find(BerTag(FIRST_SUBSCRIBE_TAG, SECOND_SUBSCRIBE_TAG)) != null) {
            subscribe = SubscribeVehicleStatusRequest().apply {
                this.deserializeFromTlv(tlv.find(BerTag(FIRST_SUBSCRIBE_TAG, SECOND_SUBSCRIBE_TAG)))
            }
        }
        if (tlv.find(BerTag(FIRST_QUERY_TAG, SECOND_QUERY_TAG)) != null) {
            query = QueryVehicleStatusRequest().apply {
                this.deserializeFromTlv(tlv.find(BerTag(FIRST_QUERY_TAG, SECOND_QUERY_TAG)))
            }
        }
        if (tlv.find(BerTag(FIRST_UNSUBSCRIBE_TAG, SECOND_UNSUBSCRIBE_TAG)) != null) {
            unsubscribe = UnsubscribeVehicleStatusRequest().apply {
                this.deserializeFromTlv(tlv.find(BerTag(FIRST_UNSUBSCRIBE_TAG, SECOND_UNSUBSCRIBE_TAG)))
            }
        }
        signature = tlv.find(BerTag(SIGNATURE_TAG)).run {
            this?.bytesValue?.toUByteArray() ?: return
        }
    }
}
