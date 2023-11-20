package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduRke
import com.example.iccoa2.apdu.CommandApduSelect
import com.example.iccoa2.apdu.InstructionType
import com.example.iccoa2.apdu.Instruction
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduInstructions {
    @Test
    fun create_apdu_instructions() {
        val instructions = Instruction().apply {
            this.type = InstructionType.CommandSelect
            this.data = CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize()
            }
        }
        assertEquals(instructions.type, InstructionType.CommandSelect)
        assertArrayEquals(
            instructions.data.toByteArray(),
            CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize().toByteArray()
            }
        )
    }
    @Test
    fun update_apdu_instructions() {
        val instructions = Instruction().apply {
            this.type = InstructionType.CommandSelect
            this.data = CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize()
            }
        }

        instructions.apply {
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
        assertEquals(instructions.type, InstructionType.CommandRke)
        assertArrayEquals(
            instructions.data.toByteArray(),
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
    @Test
    fun serialize_apdu_instructions() {
        val instructions = Instruction().apply {
            this.type = InstructionType.CommandSelect
            this.data = CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize()
            }
        }
        val serializedInstructions = instructions.serialize()
        assertArrayEquals(
            serializedInstructions.toByteArray(),
            ubyteArrayOf(
                0x01u,
                0x00u, 0xA4u, 0x04u, 0x00u,
                0x04u,
                0x10u, 0x20u, 0x30u, 0x40u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_apdu_instructions() {
        val buffer: UByteArray = ubyteArrayOf(
            0x01u,
            0x00u, 0xA4u, 0x04u, 0x00u,
            0x04u,
            0x10u, 0x20u, 0x30u, 0x40u,
            0x00u,
        )
        val instruction = Instruction().apply {
            this.deserialize(buffer)
        }
        assertEquals(instruction.type, InstructionType.CommandSelect)
        assertArrayEquals(
            instruction.data.toByteArray(),
            CommandApduSelect().run {
                this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
                this.serialize().toByteArray()
            }
        )
    }
}