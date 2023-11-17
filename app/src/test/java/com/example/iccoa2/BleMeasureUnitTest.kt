package com.example.iccoa2

import com.example.iccoa2.ble.MeasureAction
import com.example.iccoa2.ble.MeasureRequest
import com.example.iccoa2.ble.MeasureResponse
import com.example.iccoa2.ble.MeasureResult
import com.example.iccoa2.ble.MeasureType
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleMeasureUnitTest {
    @Test
    fun create_measure_request() {
        val request = MeasureRequest().apply {
            this.type = MeasureType.BleRssi
            this.action = MeasureAction.Start
            this.duration = 0xAAu
        }
        assertEquals(request.type, MeasureType.BleRssi)
        assertEquals(request.action, MeasureAction.Start)
        assertEquals(request.duration.toInt(), 0xAA)
    }
    @Test
    fun update_measure_request() {
        val request = MeasureRequest().apply {
            this.type = MeasureType.BleRssi
            this.action = MeasureAction.Start
            this.duration = 0xAAu
        }

        request.apply {
            this.type = MeasureType.BleCs
            this.action = MeasureAction.Stop
            this.duration = 0xBBu
        }
        assertEquals(request.type, MeasureType.BleCs)
        assertEquals(request.action, MeasureAction.Stop)
        assertEquals(request.duration.toInt(), 0xBB)
    }
    @Test
    fun serialize_measure_request() {
        val request = MeasureRequest().apply {
            this.type = MeasureType.BleRssi
            this.action = MeasureAction.Start
            this.duration = 0xAAu
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x2Eu,
                0x09u,
                0x50u, 0x01u, 0x00u,
                0x51u, 0x01u, 0x01u,
                0x52u, 0x01u, 0xAAu,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_measure_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x2Eu,
            0x09u,
            0x50u, 0x01u, 0x00u,
            0x51u, 0x01u, 0x01u,
            0x52u, 0x01u, 0xAAu,
        )
        val request = MeasureRequest().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.type, MeasureType.BleRssi)
        assertEquals(request.action, MeasureAction.Start)
        assertEquals(request.duration.toInt(), 0xAA)
    }
    @Test
    fun create_measure_response() {
        val response = MeasureResponse().apply {
            this.result = MeasureResult.StartSuccess
            this.duration = 0xAAu
        }
        assertEquals(response.result, MeasureResult.StartSuccess)
        assertEquals(response.duration.toInt(), 0xAA)
    }
    @Test
    fun update_measure_response() {
        val response = MeasureResponse().apply {
            this.result = MeasureResult.StartSuccess
            this.duration = 0xAAu
        }

        response.apply {
            this.result = MeasureResult.StopSuccess
            this.duration = 0xBBu
        }
        assertEquals(response.result, MeasureResult.StopSuccess)
        assertEquals(response.duration.toInt(), 0xBB)
    }
    @Test
    fun serialize_measure_response() {
        val response = MeasureResponse().apply {
            this.result = MeasureResult.StartSuccess
            this.duration = 0xAAu
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x30u,
                0x06u,
                0x51u, 0x01u, 0x00u,
                0x52u, 0x01u, 0xAAu,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_measure_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x30u,
            0x06u,
            0x51u, 0x01u, 0x00u,
            0x52u, 0x01u, 0xAAu,
        )
        val response = MeasureResponse().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.result, MeasureResult.StartSuccess)
        assertEquals(response.duration.toInt(), 0xAA)
    }
}