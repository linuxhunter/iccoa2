package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduSharingRequest
import com.example.iccoa2.apdu.ResponseApduSharingRequest
import com.example.iccoa2.apdu.ResponseApduTrailer
import com.example.iccoa2.apdu.SharingRequestP1
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduSharingRequestUnitTest {
    @Test
    fun create_sharing_request_request() {
       val request = CommandApduSharingRequest().apply {
           this.cla = 0x00u
           this.p1 = SharingRequestP1.FromDeviceApp
           this.keyId = KeyId().apply {
               this.deviceOemId = 0x0102u
               this.vehicleOemId = 0x0304u
               this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
           }
           this.tempCsr = ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u)
       }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.p1, SharingRequestP1.FromDeviceApp)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray())
        assertArrayEquals(
            request.tempCsr.toByteArray(),
            ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u).toByteArray()
        )
    }
    @Test
    fun update_sharing_request_request() {
        val request = CommandApduSharingRequest().apply {
            this.cla = 0x00u
            this.p1 = SharingRequestP1.FromDeviceApp
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.tempCsr = ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u)
        }

        request.apply {
            this.cla = 0xFFu
            this.p1 = SharingRequestP1.FromVehicleApp
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0201u
                this.vehicleOemId = 0x0403u
                this.keySerialId = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
            }
            this.tempCsr = ubyteArrayOf(0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu)
        }
        assertEquals(request.cla.toInt(), 0xFF)
        assertEquals(request.p1, SharingRequestP1.FromVehicleApp)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0201)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u).toByteArray()
        )
        assertArrayEquals(
            request.tempCsr.toByteArray(),
            ubyteArrayOf(0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu).toByteArray()
        )
    }
    @Test
    fun serialize_sharing_request_request() {
        val request = CommandApduSharingRequest().apply {
            this.cla = 0x00u
            this.p1 = SharingRequestP1.FromDeviceApp
            this.keyId = KeyId().apply {
                this.deviceOemId = 0x0102u
                this.vehicleOemId = 0x0304u
                this.keySerialId = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
            }
            this.tempCsr = ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u)
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x65u, 0x00u, 0x00u,
                0x1Fu,
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x7Fu, 0x22u,
                0x0Au,
                0x02u, 0x08u,
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_sharing_request_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x65u, 0x00u, 0x00u,
            0x1Fu,
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x7Fu, 0x22u,
            0x0Au,
            0x02u, 0x08u,
            0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            0x00u,
        )
        val request = CommandApduSharingRequest().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.p1, SharingRequestP1.FromDeviceApp)
        assertEquals(request.keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(request.keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            request.keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray())
        assertArrayEquals(
            request.tempCsr.toByteArray(),
            ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u).toByteArray()
        )
    }
    @Test
    fun create_sharing_request_response() {
        val response = ResponseApduSharingRequest().apply {
            this.tempCert = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertArrayEquals(
            response.tempCert.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_sharing_request_response() {
        val response = ResponseApduSharingRequest().apply {
            this.tempCert = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }

        response.apply {
            this.tempCert = ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertArrayEquals(
            response.tempCert.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)

    }
    @Test
    fun serialize_sharing_request_response() {
        val response = ResponseApduSharingRequest().apply {
            this.tempCert = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
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
                0x7Fu, 0x48u,
                0x0Au,
                0x03u, 0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_sharing_request_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x48u,
            0x0Au,
            0x03u, 0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            0x90u, 0x00u,
        )
        val response = ResponseApduSharingRequest().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.tempCert.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
}