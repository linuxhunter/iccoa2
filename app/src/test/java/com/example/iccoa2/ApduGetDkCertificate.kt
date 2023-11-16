package com.example.iccoa2

import com.example.iccoa2.apdu.CommandAPduGetDkCertificate
import com.example.iccoa2.apdu.DkCertificateType
import com.example.iccoa2.apdu.ResponseApduGetDkCertificate
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduGetDkCertificate {
    @Test
    fun create_get_dk_certificate_request() {
        val cla: UByte = 0x00u
        val dkType = DkCertificateType.VehicleCA
        val request = CommandAPduGetDkCertificate().apply {
            this.cla = cla
            this.dkType = dkType
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.dkType, DkCertificateType.VehicleCA)
    }
    @Test
    fun update_get_dk_certificate_request() {
        val cla: UByte = 0x00u
        val dkType = DkCertificateType.VehicleCA
        val request = CommandAPduGetDkCertificate().apply {
            this.cla = cla
            this.dkType = dkType
        }

        request.apply {
            this.cla = 0xFFu
            this.dkType = DkCertificateType.VehicleMasterKey
        }
        assertEquals(request.cla.toInt(), 0xFF)
        assertEquals(request.dkType, DkCertificateType.VehicleMasterKey)
    }
    @Test
    fun serialize_get_dk_certificate_request() {
        val cla: UByte = 0x00u
        val dkType = DkCertificateType.VehicleCA
        val request = CommandAPduGetDkCertificate().apply {
            this.cla = cla
            this.dkType = dkType
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x64u, 0x00u, 0x00u,
                0x01u,
                0x01u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_get_dk_certificate_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x64u, 0x00u, 0x00u,
            0x01u,
            0x01u,
            0x00u,
        )
        val request = CommandAPduGetDkCertificate().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertEquals(request.dkType, DkCertificateType.VehicleCA)
    }
    @Test
    fun create_get_dk_certificate_response() {
        val response = ResponseApduGetDkCertificate().apply {
            this.dkType = DkCertificateType.VehicleCA
            this.dkCert = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertEquals(response.dkType, DkCertificateType.VehicleCA)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_get_dk_certificate_response() {
        val response = ResponseApduGetDkCertificate().apply {
            this.dkType = DkCertificateType.VehicleCA
            this.dkCert = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }

        response.apply {
            this.dkType = DkCertificateType.VehicleMasterKey
            this.dkCert = ubyteArrayOf(0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertEquals(response.dkType, DkCertificateType.VehicleMasterKey)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)

        response.apply {
            this.dkType = DkCertificateType.TempSharedCert
            this.dkCert = ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertEquals(response.dkType, DkCertificateType.TempSharedCert)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)

        response.apply {
            this.dkType = DkCertificateType.FriendKey
            this.dkCert = ubyteArrayOf(0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertEquals(response.dkType, DkCertificateType.FriendKey)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
    @Test
    fun serialize_get_dk_certificate_response() {
        val response = ResponseApduGetDkCertificate().apply {
            this.dkType = DkCertificateType.VehicleCA
            this.dkCert = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        var serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x40u,
                0x0Au,
                0x01u, 0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
                0x90u, 0x00u
            ).toByteArray()
        )

        response.apply {
            this.dkType = DkCertificateType.VehicleMasterKey
            this.dkCert = ubyteArrayOf(0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x42u,
                0x0Au,
                0x01u, 0x08u,
                0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
                0x61u, 0x10u,
            ).toByteArray()
        )

        response.apply {
            this.dkType = DkCertificateType.TempSharedCert
            this.dkCert = ubyteArrayOf(0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x44u,
                0x0Au,
                0x01u, 0x08u,
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
                0x90u, 0x00u,
            ).toByteArray()
        )

        response.apply {
            this.dkType = DkCertificateType.FriendKey
            this.dkCert = ubyteArrayOf(0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu)
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x7Fu, 0x46u,
                0x0Au,
                0x01u, 0x08u,
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
                0x61u, 0x10u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_get_dk_certificate_response() {
        var buffer: UByteArray = ubyteArrayOf(
            0x7Fu, 0x40u,
            0x0Au,
            0x01u, 0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            0x90u, 0x00u
        )
        var response = ResponseApduGetDkCertificate().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.dkType, DkCertificateType.VehicleCA)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)

        buffer = ubyteArrayOf(
            0x7Fu, 0x42u,
            0x0Au,
            0x01u, 0x08u,
            0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            0x61u, 0x10u,
        )
        response = ResponseApduGetDkCertificate().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.dkType, DkCertificateType.VehicleMasterKey)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u ,0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)

        buffer = ubyteArrayOf(
            0x7Fu, 0x44u,
            0x0Au,
            0x01u, 0x08u,
            0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u,
            0x90u, 0x00u,
        )
        response = ResponseApduGetDkCertificate().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.dkType, DkCertificateType.TempSharedCert)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x11u, 0x12u, 0x13u, 0x14u, 0x15u, 0x16u, 0x17u
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)

        buffer = ubyteArrayOf(
            0x7Fu, 0x46u,
            0x0Au,
            0x01u, 0x08u,
            0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu,
            0x61u, 0x10u,
        )
        response = ResponseApduGetDkCertificate().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.dkType, DkCertificateType.FriendKey)
        assertArrayEquals(
            response.dkCert.toByteArray(),
            ubyteArrayOf(
                0x18u, 0x19u, 0x1Au, 0x1Bu, 0x1Cu, 0x1Du, 0x1Eu, 0x1Fu
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
}