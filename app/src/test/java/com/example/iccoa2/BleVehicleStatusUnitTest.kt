package com.example.iccoa2

import com.example.iccoa2.ble.QueryVehicleStatusRequest
import com.example.iccoa2.ble.SubscribeVehicleStatusRequest
import com.example.iccoa2.ble.SubscribeVerificationResponse
import com.example.iccoa2.ble.UnsubscribeVehicleStatusRequest
import com.example.iccoa2.ble.VehicleEntity
import com.example.iccoa2.ble.VehicleStatusResponse
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvParser
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleVehicleStatusUnitTest {
    @Test
    fun create_subscribe_request() {
        val request = SubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.DoorLock
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0001)
    }
    @Test
    fun update_subscribe_request() {
        val request = SubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.DoorLock
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0001)

        request.apply {
            this.entity = VehicleEntity.CarWindow
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0002)

        request.apply {
            this.entity = VehicleEntity.BackTrunk
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0003)

        request.apply {
            this.entity = VehicleEntity.Engine
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0004)

        request.apply {
            this.entity = VehicleEntity.AirController
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0005)

        request.apply {
            this.entity = VehicleEntity.CarLight
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0006)

        request.apply {
            this.entity = VehicleEntity.Custom
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x1001)
    }
    @Test
    fun serialize_subscribe_request() {
        val request = SubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.DoorLock
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x73u,
                0x06u,
                0x30u, 0x04u,
                0x84u, 0x02u,
                0x00u, 0x01u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_subscribe_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x73u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x01u,
        )
        val request = SubscribeVehicleStatusRequest().apply {
            this.deserialize(buffer)
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0001)
    }
    @Test
    fun deserialize_from_tlv_subscribe_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x73u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x01u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        val request = SubscribeVehicleStatusRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(SubscribeVehicleStatusRequest.FIRST_SUBSCRIBE_TAG, SubscribeVehicleStatusRequest.SECOND_SUBSCRIBE_TAG)))
        }
        assertNotNull(request.entity)
        assertEquals(request.entity!!.value.toInt(), 0x0001)
    }
    @Test
    fun create_subscribe_all_request() {
        val request = SubscribeVehicleStatusRequest().apply {
            this.entity = null
        }
        assertNull(request.entity)
    }
    @Test
    fun serialize_subscribe_all_request() {
        val request = SubscribeVehicleStatusRequest().apply {
            this.entity = null
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x73u,
                0x02u,
                0x86u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_subscribe_all_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x73u,
            0x02u,
            0x86u, 0x00u,
        )
        val request = SubscribeVehicleStatusRequest().apply {
            this.deserialize(buffer)
        }
        assertNull(request.entity)
    }
    @Test
    fun deserialize_from_tlv_subscribe_all_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x73u,
            0x02u,
            0x86u, 0x00u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        val request = SubscribeVehicleStatusRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(SubscribeVehicleStatusRequest.FIRST_SUBSCRIBE_TAG, SubscribeVehicleStatusRequest.SECOND_SUBSCRIBE_TAG)))
        }
        assertNull(request.entity)
    }
    @Test
    fun create_query_request() {
        val request = QueryVehicleStatusRequest().apply {
            this.entity = VehicleEntity.CarLight
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.CarLight)
    }
    @Test
    fun update_query_request() {
        val request = QueryVehicleStatusRequest().apply {
            this.entity = VehicleEntity.CarLight
        }

        request.apply {
            this.entity = VehicleEntity.AirController
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.AirController)
    }
    @Test
    fun serialize_query_request() {
        val request = QueryVehicleStatusRequest().apply {
            this.entity = VehicleEntity.CarLight
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x74u,
                0x06u,
                0x30u, 0x04u,
                0x84u, 0x02u,
                0x00u, 0x06u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_query_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x74u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x06u,
        )
        val request = QueryVehicleStatusRequest().apply {
            this.deserialize(buffer)
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.CarLight)
    }
    @Test
    fun deserialize_from_tlv_query_reqeust() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x74u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x06u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        val request = QueryVehicleStatusRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(QueryVehicleStatusRequest.FIRST_QUERY_TAG, QueryVehicleStatusRequest.SECOND_QUERY_TAG)))
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.CarLight)
    }
    @Test
    fun create_query_all_request() {
        val request = QueryVehicleStatusRequest().apply {
            this.entity = null
        }
        assertNull(request.entity)
    }
    @Test
    fun serialize_query_all_request() {
        val request = QueryVehicleStatusRequest().apply {
            this.entity = null
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x74u,
                0x02u,
                0x86u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_query_all_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x74u,
            0x02u,
            0x86u, 0x00u,
        )
        val request = QueryVehicleStatusRequest().apply {
            this.deserialize(buffer)
        }
        assertNull(request.entity)
    }
    @Test
    fun deserialize_from_tlv_query_all_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x74u,
            0x02u,
            0x86u, 0x00u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        val request = QueryVehicleStatusRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(QueryVehicleStatusRequest.FIRST_QUERY_TAG, QueryVehicleStatusRequest.SECOND_QUERY_TAG)))
        }
        assertNull(request.entity)
    }
    @Test
    fun create_unsubscribe_request() {
        val request = UnsubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.Engine
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.Engine)
    }
    @Test
    fun update_unsubscribe_request() {
        val request = UnsubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.Engine
        }

        request.apply {
            this.entity = VehicleEntity.DoorLock
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.DoorLock)
    }
    @Test
    fun serialize_unsubscribe_request() {
        val request = UnsubscribeVehicleStatusRequest().apply {
            this.entity = VehicleEntity.Engine
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x75u,
                0x06u,
                0x30u, 0x04u,
                0x84u, 0x02u,
                0x00u, 0x04u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_unsubscribe_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x75u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x04u,
        )
        val request = UnsubscribeVehicleStatusRequest().apply {
            this.deserialize(buffer)
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.Engine)
    }
    @Test
    fun deserialize_from_tlv_unsubscribe_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x75u,
            0x06u,
            0x30u, 0x04u,
            0x84u, 0x02u,
            0x00u, 0x04u,
        )
        val tlv = BerTlvParser().run {
            this.parse(buffer.toByteArray())
        }
        val request = UnsubscribeVehicleStatusRequest().apply {
            this.deserializeFromTlv(tlv.find(BerTag(UnsubscribeVehicleStatusRequest.FIRST_UNSUBSCRIBE_TAG, UnsubscribeVehicleStatusRequest.SECOND_UNSUBSCRIBE_TAG)))
        }
        assertNotNull(request.entity)
        assertEquals(request.entity, VehicleEntity.Engine)
    }
    @Test
    fun create_subscribe_verification_response() {
        val response = SubscribeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun update_subscribe_verification_response() {
        val response = SubscribeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }

        response.apply {
            this.random = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            )
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_subscribe_verification_response() {
        val response = SubscribeVerificationResponse().apply {
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x8Au,
                0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_subscribe_verification_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x8Au,
            0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
        )
        val response = SubscribeVerificationResponse().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.random.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun create_subscribe_response() {
        val response = VehicleStatusResponse().apply {
            this.entity = VehicleEntity.DoorLock
            this.status = 0xAABBu
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        assertEquals(response.entity, VehicleEntity.DoorLock)
        assertEquals(response.status.toInt(), 0xAABB)
        assertNotNull(response.random)
        assertArrayEquals(
            response.random!!.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun update_subscribe_response() {
        val response = VehicleStatusResponse().apply {
            this.entity = VehicleEntity.DoorLock
            this.status = 0xAABBu
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }

        response.apply {
            this.entity = VehicleEntity.CarLight
            this.status = 0xBBAAu
            this.random = null
        }
        assertEquals(response.entity, VehicleEntity.CarLight)
        assertEquals(response.status.toInt(), 0xBBAA)
        assertNull(response.random)
    }
    @Test
    fun serialize_subscribe_response() {
        val response = VehicleStatusResponse().apply {
            this.entity = VehicleEntity.DoorLock
            this.status = 0xAABBu
            this.random = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        var serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x77u,
                0x0Cu,
                0x30u, 0x0Au,
                0xA0u, 0x08u,
                0x80u, 0x02u,
                0x00u, 0x01u,
                0x89u, 0x02u,
                0xAAu, 0xBBu,
                0x8Au, 0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )

        response.apply {
            this.random = null
        }
        serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x77u,
                0x0Cu,
                0x30u, 0x0Au,
                0xA0u, 0x08u,
                0x80u, 0x02u,
                0x00u, 0x01u,
                0x89u, 0x02u,
                0xAAu, 0xBBu,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_subscribe_response() {
        var buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x77u,
            0x0Cu,
            0x30u, 0x0Au,
            0xA0u, 0x08u,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x89u, 0x02u,
            0xAAu, 0xBBu,
            0x8Au, 0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
        )
        var response = VehicleStatusResponse().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.entity, VehicleEntity.DoorLock)
        assertEquals(response.status.toInt(), 0xAABB)
        assertNotNull(response.random)
        assertArrayEquals(
            response.random!!.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )

        buffer = ubyteArrayOf(
            0x7Fu, 0x77u,
            0x0Cu,
            0x30u, 0x0Au,
            0xA0u, 0x08u,
            0x80u, 0x02u,
            0x00u, 0x01u,
            0x89u, 0x02u,
            0xAAu, 0xBBu,
        )
        response = VehicleStatusResponse().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.entity, VehicleEntity.DoorLock)
        assertEquals(response.status.toInt(), 0xAABB)
        assertNull(response.random)
    }
}