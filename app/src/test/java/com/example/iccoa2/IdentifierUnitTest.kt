package com.example.iccoa2

import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class IdentifierUnitTest {
    @Test
    fun create_key_id() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        assertEquals(keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )
    }
    @Test
    fun update_key_id() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val newDeviceId: UShort = 0x0201u
        val newVehicleId: UShort = 0x0403u
        val newKeySerialId: UByteArray = ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u)
        keyId.run {
            this.deviceOemId = newDeviceId
            this.vehicleOemId = newVehicleId
            this.keySerialId = newKeySerialId
        }
        assertEquals(keyId.deviceOemId.toInt(), 0x0201)
        assertEquals(keyId.vehicleOemId.toInt(), 0x0403)
        assertArrayEquals(
            keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u).toByteArray()
        )
    }
    @Test
    fun serialize_key_id() {
        val deviceOemId: UShort = 0x0102u
        val vehicleOemId: UShort = 0x0304u
        val keySerialId: UByteArray = ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u)
        val keyId = KeyId().apply {
            this.deviceOemId = deviceOemId
            this.vehicleOemId = vehicleOemId
            this.keySerialId = keySerialId
        }
        val serializedKeyId = keyId.serialize()
        assertArrayEquals(
            serializedKeyId.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x02u,
                0x03u, 0x04u,
                0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_key_id() {
        val buffer: UByteArray = ubyteArrayOf(
            0x01u, 0x02u,
            0x03u, 0x04u,
            0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
        )
        val keyId = KeyId().apply {
            this.deserialize(buffer)
        }
        assertEquals(keyId.deviceOemId.toInt(), 0x0102)
        assertEquals(keyId.vehicleOemId.toInt(), 0x0304)
        assertArrayEquals(
            keyId.keySerialId.toByteArray(),
            ubyteArrayOf(0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u).toByteArray()
        )
    }
    @Test
    fun create_vehicle_id() {
        val vehicleOemId: UShort = 0x0102u
        val vehicleSerialId: UByteArray = ubyteArrayOf(
            0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
        )
        val vehicleId = VehicleId().apply {
            this.vehicleOemId = vehicleOemId
            this.vehicleSerialId = vehicleSerialId
        }
        assertEquals(vehicleId.vehicleOemId.toInt(), 0x0102)
        assertArrayEquals(
            vehicleId.vehicleSerialId.toByteArray(),
            ubyteArrayOf(
                0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
    }
    @Test
    fun update_vehicle_id() {
        val vehicleOemId: UShort = 0x0102u
        val vehicleSerialId: UByteArray = ubyteArrayOf(
            0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
        )
        val vehicleId = VehicleId().apply {
            this.vehicleOemId = vehicleOemId
            this.vehicleSerialId = vehicleSerialId
        }
        val newVehicleOemId: UShort = 0x0201u
        val newVehicleSerialId: UByteArray = ubyteArrayOf(
            0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u, 0x04u, 0x03u
        )
        vehicleId.apply {
            this.vehicleOemId = newVehicleOemId
            this.vehicleSerialId = newVehicleSerialId
        }
        assertEquals(vehicleId.vehicleOemId.toInt(), 0x0201)
        assertArrayEquals(
            vehicleId.vehicleSerialId.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x0Fu, 0x0Eu, 0x0Du, 0x0Cu, 0x0Bu, 0x0Au, 0x09u, 0x08u, 0x07u, 0x06u, 0x05u, 0x04u, 0x03u
            ).toByteArray()
        )
    }
    @Test
    fun serialize_vehicle_id() {
        val vehicleOemId: UShort = 0x0102u
        val vehicleSerialId: UByteArray = ubyteArrayOf(
            0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
        )
        val vehicleId = VehicleId().apply {
            this.vehicleOemId = vehicleOemId
            this.vehicleSerialId = vehicleSerialId
        }
        val serializedVehicleId = vehicleId.serialize()
        assertArrayEquals(
            serializedVehicleId.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x02u,
                0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_vehicle_id() {
        val buffer: UByteArray = ubyteArrayOf(
            0x01u, 0x02u,
            0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
        )
        val vehicleId = VehicleId().apply {
            this.deserialize(buffer)
        }
        assertEquals(vehicleId.vehicleOemId.toInt(), 0x0102)
        assertArrayEquals(
            vehicleId.vehicleSerialId.toByteArray(),
            ubyteArrayOf(
                0x03u, 0x04u, 0x05u, 0x06u, 0x07u, 0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu, 0x10u
            ).toByteArray()
        )
    }
}