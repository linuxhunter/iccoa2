package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduListDk
import com.example.iccoa2.apdu.KeyIdStatus
import com.example.iccoa2.apdu.ResponseApduListDk
import com.example.iccoa2.apdu.ResponseApduTrailer
import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import org.junit.Test

import org.junit.Assert.*
@OptIn(ExperimentalUnsignedTypes::class)
class ApduListDkUnitTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun test_ber_tlv() {
        val bytes = BerTlvBuilder().run {
            this.addBytes(BerTag(0x89), byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05, 0x06))
            this.buildArray()
        }
        println("bytes = ${bytes.toHexString()}")
        val values = BerTlvParser().run {
            this.parse(bytes)
        }
        val yyy = values.find(BerTag(0x89)).run {
            this.bytesValue.toUByteArray()
        }
        println("values = $values")
        println("yyy = $yyy")
    }
    @Test
    fun create_list_dk_request() {
        val cla: UByte = 0x00u
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val listDkRequest = CommandApduListDk().apply {
            this.cla = cla
            this.keyId = keyId
        }
        assertEquals(listDkRequest.cla.toInt(), 0x00)
        assertNotNull(listDkRequest.keyId)
        val keyIdValue = listDkRequest.keyId!!
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0102)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )
    }
    @Test
    fun update_list_dk_request() {
        val cla: UByte = 0x00u
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val listDkRequest = CommandApduListDk().apply {
            this.cla = cla
            this.keyId = keyId
        }

        val newCla: UByte = 0xFFu
        val newDeviceId: UShort = 0x0201u
        val newVehicleId: UShort = 0x0403u
        val newKeySerialId: UByteArray = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
        val newKeyId = KeyId().apply {
            this.deviceOemId = newDeviceId
            this.vehicleOemId = newVehicleId
            this.keySerialId = newKeySerialId
        }
        listDkRequest.apply {
            this.cla = newCla
            this.keyId = newKeyId
        }
        assertEquals(listDkRequest.cla.toInt(), 0xFF)
        assertNotNull(listDkRequest.keyId)
        val keyIdValue = listDkRequest.keyId!!
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0201)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u).toByteArray()
        )

        listDkRequest.apply {
            this.keyId = null
        }
        assertEquals(listDkRequest.cla.toInt(), 0xFF)
        assertNull(listDkRequest.keyId)
    }
    @Test
    fun serialize_list_dk_request() {
        val cla: UByte = 0x00u
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val listDkRequest = CommandApduListDk().apply {
            this.cla = cla
            this.keyId = keyId
        }
        var serializedListDkRequest: UByteArray = listDkRequest.serialize()
        assertArrayEquals(
            serializedListDkRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x60u, 0x01u, 0x00u,
                0x12u,
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x00u,
            ).toByteArray()
        )

        listDkRequest.apply {
            this.keyId = null
        }
        serializedListDkRequest = listDkRequest.serialize()
        assertArrayEquals(
            serializedListDkRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x60u, 0x00u, 0x00u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_list_dk_request() {
        var buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x60u, 0x01u, 0x00u,
            0x12u,
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x00u,
        )
        var request = CommandApduListDk().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertNotNull(request.keyId)
        val keyIdValue = request.keyId!!
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0102)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )

        buffer = ubyteArrayOf(
            0x00u, 0x60u, 0x00u, 0x00u,
            0x00u,
        )
        request = CommandApduListDk().apply {
            this.deserialize(buffer)
        }
        assertEquals(request.cla.toInt(), 0x00)
        assertNull(request.keyId)
    }
    @Test
    fun create_list_dk_response() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val keyIdStatus = KeyIdStatus.UNDELIVERD
        val status = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApduListDk().apply {
            this.keyId = keyId
            this.keyIdStatus = keyIdStatus
            this.status = status
        }
        val keyIdValue = response.keyId
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0102)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )
        assertEquals(response.keyIdStatus, KeyIdStatus.UNDELIVERD)
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_list_dk_response() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val keyIdStatus = KeyIdStatus.UNDELIVERD
        val status = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApduListDk().apply {
            this.keyId = keyId
            this.keyIdStatus = keyIdStatus
            this.status = status
        }

        val newDeviceId: UShort = 0x0201u
        val newVehicleId: UShort = 0x0403u
        val newKeySerialId: UByteArray = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
        val newKeyId = KeyId().apply {
            this.deviceOemId = newDeviceId
            this.vehicleOemId = newVehicleId
            this.keySerialId = newKeySerialId
        }
        val newKeyIdStatus = KeyIdStatus.DELIVERD
        val newStatus = ResponseApduTrailer().apply {
            this.sw1 = 0x61u
            this.sw2 = 0x10u
        }
        response.apply {
            this.keyId = newKeyId
            this.keyIdStatus = newKeyIdStatus
            this.status = newStatus
        }
        val keyIdValue = response.keyId
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0201)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u).toByteArray()
        )
        assertEquals(response.keyIdStatus, KeyIdStatus.DELIVERD)
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
    @Test
    fun serialize_list_dk_response() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val keyIdStatus = KeyIdStatus.UNDELIVERD
        val status = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApduListDk().apply {
            this.keyId = keyId
            this.keyIdStatus = keyIdStatus
            this.status = status
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x89u, 0x10u,
                0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
                0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
                0x88u, 0x01u,
                0x01u,
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_list_dk_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x89u, 0x10u,
            0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u,
            0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            0x88u, 0x01u,
            0x01u,
            0x90u, 0x00u,
        )
        val response = ResponseApduListDk().apply {
            this.deserialize(buffer)
        }
        val keyIdValue = response.keyId
        assertEquals(keyIdValue.deviceOemId.toInt(), 0x0102)
        assertEquals(keyIdValue.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyIdValue.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )
        assertEquals(response.keyIdStatus, KeyIdStatus.UNDELIVERD)
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
}
