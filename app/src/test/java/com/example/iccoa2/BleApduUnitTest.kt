package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduRke
import com.example.iccoa2.apdu.CommandApduSelect
import com.example.iccoa2.apdu.Instruction
import com.example.iccoa2.apdu.InstructionType
import com.example.iccoa2.ble.Apdu
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleApduUnitTest {
    @Test
    fun create_apdu() {
        val select = Instruction().apply {
            this.type = InstructionType.CommandSelect
            this.data = CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize()
            }
        }
        val rke = Instruction().apply {
            this.type = InstructionType.CommandRke
            this.data = CommandApduRke().run {
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
                this.serialize()
            }
        }
        val apdu = Apdu().apply {
            this.data = arrayListOf(select, rke)
        }
        for (item in apdu.data.iterator()) {
            when (item.type) {
                InstructionType.CommandSelect -> {
                    assertArrayEquals(
                        item.data.toByteArray(),
                        CommandApduSelect().run {
                            this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                            this.serialize().toByteArray()
                        }
                    )
                }
                InstructionType.CommandRke -> {
                    assertArrayEquals(
                        item.data.toByteArray(),
                        CommandApduRke().run {
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
                            this.serialize().toByteArray()
                        }
                    )
                }
                else -> {
                    println("Could not be reach here!")
                }
            }
        }
    }
    @Test
    fun serialize_apdu() {
        val select = Instruction().apply {
            this.type = InstructionType.CommandSelect
            this.data = CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize()
            }
        }
        val rke = Instruction().apply {
            this.type = InstructionType.CommandRke
            this.data = CommandApduRke().run {
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
                this.serialize()
            }
        }
        val apdu = Apdu().apply {
            this.data = arrayListOf(select, rke)
        }
        val serializedApdu = apdu.serialize()
        assertArrayEquals(
            serializedApdu.toByteArray(),
            ubyteArrayOf(
                0x0Bu,
                0x01u,
                0x00u, 0xA4u, 0x04u, 0x00u,
                0x04u,
                0x10u, 0x20u, 0x30u, 0x40u,
                0x00u,
                0x33u,
                0x0Du,
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
    fun deserialize_apdu() {
        val buffer: UByteArray = ubyteArrayOf(
            0x0Bu,
            0x01u,
            0x00u, 0xA4u, 0x04u, 0x00u,
            0x04u,
            0x10u, 0x20u, 0x30u, 0x40u,
            0x00u,
            0x33u,
            0x0Du,
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
        val apdu = Apdu().apply {
            this.deserialize(buffer)
        }
        for (item in apdu.data.iterator()) {
            when (item.type) {
                InstructionType.CommandSelect -> {
                    assertArrayEquals(
                        item.data.toByteArray(),
                        CommandApduSelect().run {
                            this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                            this.serialize().toByteArray()
                        }
                    )
                }
                InstructionType.CommandRke -> {
                    assertArrayEquals(
                        item.data.toByteArray(),
                        CommandApduRke().run {
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
                            this.serialize().toByteArray()
                        }
                    )
                }
                else -> {
                    println("Could not be reach here!")
                }
            }
        }
    }
}
