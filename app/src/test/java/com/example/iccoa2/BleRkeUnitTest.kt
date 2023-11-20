package com.example.iccoa2

import com.example.iccoa2.ble.RkeContinuedRequest
import com.example.iccoa2.ble.RkeFunctionAndAction
import com.example.iccoa2.ble.RkeRequest
import com.example.iccoa2.ble.RkeResponse
import com.example.iccoa2.ble.RkeVerificationResponse
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvParser
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleRkeUnitTest {
    @Test
    fun create_rke_request() {
        val request = RkeRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
    }
    @Test
    fun update_rke_request() {
        val request = RkeRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)

        request.apply {
            this.rke = RkeFunctionAndAction.DoorUnlock
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x02)

        request.apply {
            this.rke = RkeFunctionAndAction.CarWindowFullOpen
        }
        assertEquals(request.rke.function.toInt(), 0x0002)
        assertEquals(request.rke.action.toInt(), 0x01)

        request.apply {
            this.rke = RkeFunctionAndAction.CarWindowFullClose
        }
        assertEquals(request.rke.function.toInt(), 0x0002)
        assertEquals(request.rke.action.toInt(), 0x02)

        request.apply {
            this.rke = RkeFunctionAndAction.CarWindowPartialOpen
        }
        assertEquals(request.rke.function.toInt(), 0x0002)
        assertEquals(request.rke.action.toInt(), 0x03)

        request.apply {
            this.rke = RkeFunctionAndAction.BackTrunkOpen
        }
        assertEquals(request.rke.function.toInt(), 0x0003)
        assertEquals(request.rke.action.toInt(), 0x01)

        request.apply {
            this.rke = RkeFunctionAndAction.BackTrunkClose
        }
        assertEquals(request.rke.function.toInt(), 0x0003)
        assertEquals(request.rke.action.toInt(), 0x02)

        request.apply {
            this.rke = RkeFunctionAndAction.EngineStart
        }
        assertEquals(request.rke.function.toInt(), 0x0004)
        assertEquals(request.rke.action.toInt(), 0x01)

        request.apply {
            this.rke = RkeFunctionAndAction.EngineStop
        }
        assertEquals(request.rke.function.toInt(), 0x0004)
        assertEquals(request.rke.action.toInt(), 0x02)

        request.apply {
            this.rke = RkeFunctionAndAction.FindVehicleFlashing
        }
        assertEquals(request.rke.function.toInt(), 0x0005)
        assertEquals(request.rke.action.toInt(), 0x01)

        request.apply {
            this.rke = RkeFunctionAndAction.FindVehicleWhistling
        }
        assertEquals(request.rke.function.toInt(), 0x0005)
        assertEquals(request.rke.action.toInt(), 0x02)

        request.apply {
            this.rke = RkeFunctionAndAction.FindVehicleFlashingAndWhistling
        }
        assertEquals(request.rke.function.toInt(), 0x0005)
        assertEquals(request.rke.action.toInt(), 0x03)

        request.apply {
            this.rke = RkeFunctionAndAction.Custom
        }
        assertEquals(request.rke.function.toInt(), 0x1001)
        assertEquals(request.rke.action.toInt(), 0x00)
    }
    @Test
    fun serialize_rke_request() {
        val request = RkeRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x70u,
                0x07u,
                0x80u, 0x02u,
                0x00u, 0x01u,
                0x81u, 0x01u,
                0x01u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x70u,
            0x07u,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x81u, 0x01u,
            0x01u,
        )
        val request = RkeRequest().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
    }
    @Test
    fun deserialize_from_tlv_rke_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x70u,
            0x07u,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x81u, 0x01u,
            0x01u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        assertNotNull(tlv)
        val request = RkeRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(RkeRequest.FIRST_RKE_REQUEST_TAG, RkeRequest.SECOND_RKE_REQUEST_TAG)))
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
    }
    @Test
    fun create_rke_continued_request() {
        val request = RkeContinuedRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.custom = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
        assertNotNull(request.custom)
        assertArrayEquals(
            request.custom?.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun update_rke_continued_request() {
        val request = RkeContinuedRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.custom = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
        assertNotNull(request.custom)
        assertArrayEquals(
            request.custom?.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )

        request.apply {
            this.rke = RkeFunctionAndAction.EngineStart
            this.custom = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            )
        }
        assertEquals(request.rke.function.toInt(), 0x0004)
        assertEquals(request.rke.action.toInt(), 0x01)
        assertNotNull(request.custom)
        assertArrayEquals(
            request.custom?.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            ).toByteArray()
        )

        request.apply {
            this.custom = null
        }
        assertEquals(request.rke.function.toInt(), 0x0004)
        assertEquals(request.rke.action.toInt(), 0x01)
        assertNull(request.custom)
    }
    @Test
    fun serialize_rke_continued_request() {
        val request = RkeContinuedRequest().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.custom = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x76u,
                0x11u,
                0x80u, 0x02u,
                0x00u, 0x01u,
                0x81u, 0x01u,
                0x01u,
                0x88u, 0x08u,
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_continued_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x76u,
            0x11u,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x81u, 0x01u,
            0x01u,
            0x88u, 0x08u,
            0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
        )
        val request = RkeContinuedRequest().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.rke.function.toInt(), 0x0001)
        assertEquals(request.rke.action.toInt(), 0x01)
        assertNotNull(request.custom)
        assertArrayEquals(
            request.custom?.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun create_rke_verification_response() {
        val response = RkeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun update_rke_verification_response() {
        val response = RkeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }

        response.apply {
            this.random = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            )
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            ).toByteArray()
        )
    }
    @Test
    fun serialize_rke_verification_response() {
        val response = RkeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x8Au, 0x08u,
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_verification_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x8Au, 0x08u,
            0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
        )
        val response = RkeVerificationResponse().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x2u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun create_rke_response() {
        val response = RkeResponse().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.status = 0xAABBu
        }
        assertEquals(response.rke.function.toInt(), 0x0001)
        assertEquals(response.rke.action.toInt(), 0x01)
        assertEquals(response.status.toInt(), 0xAABB)
    }
    @Test
    fun update_rke_response() {
        val response = RkeResponse().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.status = 0xAABBu
        }

        response.apply {
            this.rke = RkeFunctionAndAction.DoorUnlock
            this.status = 0xBBAAu
        }
        assertEquals(response.rke.function.toInt(), 0x0001)
        assertEquals(response.rke.action.toInt(), 0x02)
        assertEquals(response.status.toInt(), 0xBBAA)
    }
    @Test
    fun serialize_rke_response() {
        val response = RkeResponse().apply {
            this.rke = RkeFunctionAndAction.DoorLock
            this.status = 0xAABBu
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x72u,
                0x0Eu,
                0xA0u, 0x0Cu,
                0x80u, 0x02u,
                0x00u, 0x01u,
                0x83u, 0x02u,
                0x00u, 0x01u,
                0x89u, 0x02u,
                0xAAu, 0xBBu,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x72u,
            0x0Eu,
            0xA0u, 0x0Cu,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x83u, 0x02u,
            0x00u, 0x01u,
            0x89u, 0x02u,
            0xAAu, 0xBBu,
        )
        val response = RkeResponse().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.rke.function.toInt(), 0x0001)
        assertEquals(response.rke.action.toInt(), 0x01)
        assertEquals(response.status.toInt(), 0xAABB)
    }
}
