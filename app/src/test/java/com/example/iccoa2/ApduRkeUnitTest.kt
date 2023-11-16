package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduRke
import com.example.iccoa2.apdu.ResponseApduRke
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduRkeUnitTest {
    @Test
    fun create_rke_request() {
        val request = CommandApduRke().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.random = ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            )
            this.rkeInstruction = ubyteArrayOf(
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            )
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
        assertArrayEquals(
            request.random.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            ).toByteArray()
        )
        assertArrayEquals(
            request.rkeInstruction.toByteArray(),
            ubyteArrayOf(
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            ).toByteArray()
        )
    }
    @Test
    fun update_rke_request() {
        val request = CommandApduRke().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.random = ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            )
            this.rkeInstruction = ubyteArrayOf(
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            )
        }

        request.apply {
            this.cla = 0xFFu
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0201u
                this.vehicleOemId = 0x0403u
                this.keySerialId = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
            }
            this.random = ubyteArrayOf(
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            )
            this.rkeInstruction = ubyteArrayOf(
                0xFFu, 0xEEu, 0xDDu, 0xCCu, 0xBBu, 0xAAu,
            )
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
        assertArrayEquals(
            request.random.toByteArray(),
            ubyteArrayOf(
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            ).toByteArray()
        )
        assertArrayEquals(
            request.rkeInstruction.toByteArray(),
            ubyteArrayOf(
                0xFFu, 0xEEu, 0xDDu, 0xCCu, 0xBBu, 0xAAu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_rke_request() {
        val request = CommandApduRke().apply {
            this.cla = 0x00u
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.random = ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            )
            this.rkeInstruction = ubyteArrayOf(
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            )
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x66u, 0x00u, 0x00u,
                0x2Cu,
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x55u, 0x10u,
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
                0x57u, 0x06u,
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x66u, 0x00u, 0x00u,
            0x2Cu,
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x55u, 0x10u,
            0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            0x57u, 0x06u,
            0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            0x00u,
        )
        val request = CommandApduRke().apply {
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
        assertArrayEquals(
            request.random.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            ).toByteArray()
        )
        assertArrayEquals(
            request.rkeInstruction.toByteArray(),
            ubyteArrayOf(
                0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu,
            ).toByteArray()
        )
    }
    @Test
    fun create_rke_response() {
        val response = ResponseApduRke().apply {
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.signature = ubyteArrayOf(
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertEquals(response.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(response.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            response.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
        assertArrayEquals(
            response.signature.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_rke_response() {
        val response = ResponseApduRke().apply {
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.signature = ubyteArrayOf(
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }

        response.apply {
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0201u
                this.vehicleOemId = 0x0403u
                this.keySerialId = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
            }
            this.signature = ubyteArrayOf(
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertEquals(response.keyId.deviceOemId.toInt(), 0x0201)
        assertEquals(response.keyId.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            response.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u
            ).toByteArray()
        )
        assertArrayEquals(
            response.signature.toByteArray(),
            ubyteArrayOf(
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
    @Test
    fun serialize_rke_response() {
        val response = ResponseApduRke().apply {
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.signature = ubyteArrayOf(
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x8Fu, 0x40u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_rke_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x8Fu, 0x40u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            0x90u, 0x00u,
        )
        val response = ResponseApduRke().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(response.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            response.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(
                0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
        assertArrayEquals(
            response.signature.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u, 0x01u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
                0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u, 0x02u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
}