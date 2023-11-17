package com.example.iccoa2

import com.example.iccoa2.ble.Message
import com.example.iccoa2.ble.MessageStatus
import com.example.iccoa2.ble.MessageType
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class BleMessageUnitTest {
    @Test
    fun create_ble_message() {
        val bleMessage = Message().apply {
            this.version = 0x01u
            this.messageType = MessageType.Measure
            this.messageStatus = MessageStatus.NoApplicable
            this.messageData = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        assertEquals(bleMessage.version.toInt(), 0x01)
        assertEquals(bleMessage.messageType, MessageType.Measure)
        assertEquals(bleMessage.messageStatus, MessageStatus.NoApplicable)
        assertArrayEquals(
            bleMessage.messageData.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun update_ble_message() {
        val bleMessage = Message().apply {
            this.version = 0x01u
            this.messageType = MessageType.Measure
            this.messageStatus = MessageStatus.NoApplicable
            this.messageData = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }

        bleMessage.apply {
            this.messageType = MessageType.Auth
            this.messageStatus = MessageStatus.Success
            this.messageData = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            )
        }
        assertEquals(bleMessage.version.toInt(), 0x01)
        assertEquals(bleMessage.messageType, MessageType.Auth)
        assertEquals(bleMessage.messageStatus, MessageStatus.Success)
        assertArrayEquals(
            bleMessage.messageData.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu,
            ).toByteArray()
        )
    }
    @Test
    fun serialize_ble_message() {
        val bleMessage = Message().apply {
            this.version = 0x01u
            this.messageType = MessageType.Measure
            this.messageStatus = MessageStatus.NoApplicable
            this.messageData = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
        }
        val serializedBleMessage = bleMessage.serialize()
        assertArrayEquals(
            serializedBleMessage.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x01u, 0x00u, 0x00u, 0x00u, 0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_ble_message() {
        val buffer: UByteArray = ubyteArrayOf(
            0x01u, 0x01u, 0x00u, 0x00u, 0x00u, 0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
        )
        val bleMessage = Message().apply {
            this.deserialize(buffer)
        }
        assertEquals(bleMessage.version.toInt(), 0x01)
        assertEquals(bleMessage.messageType, MessageType.Measure)
        assertEquals(bleMessage.messageStatus, MessageStatus.NoApplicable)
        assertArrayEquals(
            bleMessage.messageData.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
    }
}