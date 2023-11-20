package com.example.iccoa2.ble

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser

enum class MeasureType(val value: UByte) {
    BleRssi(0x00u),
    BleCs(0x01u),
    Uwb(0x02u)
}

enum class MeasureAction(val value: UByte) {
    Start(0x01u),
    Stop(0x02u)
}

enum class MeasureResult(val value: UByte) {
    StartSuccess(0x00u),
    StopSuccess(0x01u),
    Failed(0xFFu)
}

@OptIn(ExperimentalUnsignedTypes::class)
class MeasureRequest {
    companion object {
        const val FIRST_REQUEST_TAG: Int = 0x7F
        const val SECOND_REQUEST_TAG: Int = 0x2E
        const val MEASURE_TYPE_TAG: Int = 0x50
        const val MEASURE_ACTION_TAG: Int = 0x51
        const val MEASURE_DURATION_TAG: Int = 0x52
    }
    var type: MeasureType = MeasureType.BleRssi
    var action: MeasureAction = MeasureAction.Start
    var duration: UByte = 0x00u

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_REQUEST_TAG, SECOND_REQUEST_TAG), BerTlvBuilder().run {
                this.addByte(BerTag(MEASURE_TYPE_TAG), type.value.toByte())
                this.addByte(BerTag(MEASURE_ACTION_TAG), action.value.toByte())
                this.addByte(BerTag(MEASURE_DURATION_TAG), duration.toByte())
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_REQUEST_TAG, SECOND_REQUEST_TAG)) == null ||
            tlv.find(BerTag(MEASURE_TYPE_TAG)) == null ||
            tlv.find(BerTag(MEASURE_ACTION_TAG)) == null ||
            tlv.find(BerTag(MEASURE_DURATION_TAG)) == null) {
            return
        }
        type = MeasureType.values().first {
            it.value == tlv.find(BerTag(MEASURE_TYPE_TAG)).run {
                this!!.bytesValue[0].toUByte()
            }
        }
        action = MeasureAction.values().first {
            it.value == tlv.find(BerTag(MEASURE_ACTION_TAG)).run {
                this!!.bytesValue[0].toUByte()
            }
        }
        duration = tlv.find(BerTag(MEASURE_DURATION_TAG)).run {
            this!!.bytesValue[0].toUByte()
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class MeasureResponse {
    companion object {
        const val FIRST_RESPONSE_TAG: Int = 0x7F
        const val SECOND_RESPONSE_TAG: Int = 0x30
        const val MEASURE_ACTION_RESULT_TAG: Int = 0x51
        const val MEASURE_DURATION_RESULT_TAG: Int = 0x52
    }
    var result: MeasureResult = MeasureResult.StartSuccess
    var duration: UByte = 0x00u

    fun serialize(): UByteArray {
        return BerTlvBuilder().run {
            this.addBytes(BerTag(FIRST_RESPONSE_TAG, SECOND_RESPONSE_TAG), BerTlvBuilder().run {
                this.addByte(BerTag(MEASURE_ACTION_RESULT_TAG), result.value.toByte())
                this.addByte(BerTag(MEASURE_DURATION_RESULT_TAG), duration.toByte())
                this.buildArray()
            })
            this.buildArray().toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        if (tlv.find(BerTag(FIRST_RESPONSE_TAG, SECOND_RESPONSE_TAG)) == null ||
            tlv.find(BerTag(MEASURE_ACTION_RESULT_TAG)) == null ||
            tlv.find(BerTag(MEASURE_DURATION_RESULT_TAG)) == null) {
            return
        }
        result = MeasureResult.values().first {
            it.value == tlv.find(BerTag(MEASURE_ACTION_RESULT_TAG)).run {
                this!!.bytesValue[0].toUByte()
            }
        }
        duration = tlv.find(BerTag(MEASURE_DURATION_RESULT_TAG)).run {
            this!!.bytesValue[0].toUByte()
        }
    }
}
