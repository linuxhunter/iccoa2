package com.example.iccoa2.ble

import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class MessageType(val value: UByte) {
    Apdu(0x00u),
    Measure(0x01u),
    Rke(0x02u),
    VehicleStatus(0x03u),
    VehicleAppCustomMessage(0x04u),
    VehicleServerCustomMessage(0x05u),
    Auth(0x06u),
    Custom(0x81u)
}

enum class MessageStatus(val value: UShort) {
    NoApplicable(0x0000u),
    Success(0x2000u),
    BeyondMessageLength(0x2001u),
    NoPermission(0x2002u),
    SeInaccessible(0x2003u),
    TlvParseError(0x2004u),
    VehicleNotSupported(0x2005u),
    InstructionVerificationFailed(0x2006u),
    UnknownError(0x5FFFu),
    Custom(0x6000u),
    Reserved(0x7FFFu)
}

@OptIn(ExperimentalUnsignedTypes::class)
class Message {
    companion object {
        const val MINI_LENGTH = 0x06
        const val VERSION_OFFSET = 0x00
        const val TYPE_OFFSET = 0x01
        const val STATUS_OFFSET = 0x02
        const val STATUS_LENGTH = 0x02
        const val LENGTH_OFFSET = 0x04
        const val LENGTH_LENGTH = 0x02
        const val DATA_OFFSET = 0x06
    }
    var version: UByte = 0x01u
    var messageType: MessageType = MessageType.Apdu
    var messageStatus: MessageStatus = MessageStatus.NoApplicable
    var messageData: UByteArray = ubyteArrayOf()

    fun serialize(): UByteArray {
        return arrayListOf<UByte>().run {
            this.add(version)
            this.add(messageType.value)
            this.addAll(ByteBuffer.allocate(Short.SIZE_BYTES).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.putShort(messageStatus.value.toShort())
                this.array().toUByteArray()
            })
            this.addAll(ByteBuffer.allocate(Short.SIZE_BYTES).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.putShort(messageData.size.toShort())
                this.array().toUByteArray()
            })
            this.addAll(messageData)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size < MINI_LENGTH) {
            return
        }
        val length = ByteBuffer.wrap(buffer.toByteArray(), LENGTH_OFFSET, LENGTH_LENGTH).run {
            this.order(ByteOrder.BIG_ENDIAN)
            this.short.toUShort()
        }
        if (buffer.size != length.toInt() + MINI_LENGTH) {
            return
        }
        version = buffer[VERSION_OFFSET]
        messageType = MessageType.values().first {
            it.value == buffer[TYPE_OFFSET]
        }
        messageStatus = MessageStatus.values().first {
            it.value == ByteBuffer.wrap(buffer.toByteArray(), STATUS_OFFSET, STATUS_LENGTH).run {
                this.order(ByteOrder.BIG_ENDIAN)
                this.short.toUShort()
            }
        }
        messageData = buffer.copyOfRange(DATA_OFFSET, DATA_OFFSET+length.toInt())
    }
}
