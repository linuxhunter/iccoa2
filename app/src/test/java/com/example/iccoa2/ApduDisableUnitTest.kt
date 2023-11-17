package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduDisable
import com.example.iccoa2.apdu.ResponseApduDisable
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduDisableUnitTest {
    @Test
    fun create_disable_request() {
        val request = CommandApduDisable().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
    }
    @Test
    fun update_disable_request() {
        val request = CommandApduDisable().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
        }

        request.apply {
            this.cla = 0xFFu
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0201u
                this.vehicleOemId = 0x0403u
                this.keySerialId = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
            }
        }
        assertEquals(request.cla.toInt(), 0xFF)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0201)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u
            ).toByteArray()
        )
    }
    @Test
    fun serialize_disable_request() {
        val request = CommandApduDisable().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x6Eu, 0x00u, 0x00u,
                0x12u,
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_disable_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x6Eu, 0x00u, 0x00u,
            0x12u,
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x00u,
        )
        val request = CommandApduDisable().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
    }
    @Test
    fun create_disable_response() {
        val response = ResponseApduDisable().apply {
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_disable_response() {
        val response = ResponseApduDisable().apply {
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }

        response.apply {
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
    @Test
    fun serialize_disable_response() {
        val response = ResponseApduDisable().apply {
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_disable_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x90u, 0x00u,
        )
        val response = ResponseApduDisable().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
}
