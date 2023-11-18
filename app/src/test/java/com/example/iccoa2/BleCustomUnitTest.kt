package com.example.iccoa2

import com.example.iccoa2.ble.VehicleAppCustomRequest
import com.example.iccoa2.ble.VehicleAppCustomResponse
import com.example.iccoa2.ble.VehicleServerCustomRequest
import com.example.iccoa2.ble.VehicleServerCustomResponse
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleCustomUnitTest {
    @Test
    fun create_vehicle_app_custom_request() {
        val request = VehicleAppCustomRequest().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        assertArrayEquals(
            request.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun update_vehicle_app_custom_request() {
        val request = VehicleAppCustomRequest().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }

        request.apply {
            this.data = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            )
        }
        assertArrayEquals(
            request.data.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_vehicle_app_custom_request() {
        val request = VehicleAppCustomRequest().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x80u,
                0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_vehicle_app_custom_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x80u,
            0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
        )
        val request = VehicleAppCustomRequest().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            request.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun create_vehicle_app_custom_response() {
        val response = VehicleAppCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun update_vehicle_app_custom_response() {
        val response = VehicleAppCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }

        response.apply {
            this.data = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            )
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_vehicle_app_custom_response() {
        val response = VehicleAppCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            )
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x81u,
                0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_vehicle_app_custom_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x81u,
            0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
        )
        val response = VehicleAppCustomResponse().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
    }
    @Test
    fun create_vehicle_server_custom_request() {
        val request = VehicleServerCustomRequest().apply {
            this.offset = 0x0102u
            this.length = 0x03u
        }
        assertEquals(request.offset.toInt(), 0x0102)
        assertEquals(request.length.toInt(), 0x03)
    }
    @Test
    fun update_vehicle_server_custom_request() {
        val request = VehicleServerCustomRequest().apply {
            this.offset = 0x0102u
            this.length = 0x03u
        }

        request.apply {
            this.offset = 0xAABBu
            this.length = 0xCCu
        }
        assertEquals(request.offset.toInt(), 0xAABB)
        assertEquals(request.length.toInt(), 0xCC)
    }
    @Test
    fun serialize_vehicle_server_custom_request() {
        val request = VehicleServerCustomRequest().apply {
            this.offset = 0x0102u
            this.length = 0x03u
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x82u,
                0x03u,
                0x01u, 0x02u, 0x03u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_vehicle_server_custom_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x82u,
            0x03u,
            0x01u, 0x02u, 0x03u,
        )
        val request = VehicleServerCustomRequest().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.offset.toInt(), 0x0102)
        assertEquals(request.length.toInt(), 0x03)
    }
    @Test
    fun create_vehicle_server_custom_response() {
        val response = VehicleServerCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun update_vehicle_server_custom_response() {
        val response = VehicleServerCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }

        response.apply {
            this.data = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            )
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_vehicle_server_custom_response() {
        val response = VehicleServerCustomResponse().apply {
            this.data = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x83u,
                0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_vehicle_server_custom_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x83u,
            0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
        )
        val response = VehicleServerCustomResponse().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.data.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
}
