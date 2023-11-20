package com.example.iccoa2.apdu

enum class InstructionType(val value: UByte) {
    CommandSelect(0x01u),
    ResponseSelect(0x02u),
    CommandListDk(0x03u),
    ResponseListDk(0x04u),
    CommandAuth0(0x05u),
    ResponseAuth0(0x06u),
    CommandAuth1(0x07u),
    ResponseAuth1(0x08u),
    CommandGetDkCertificate(0x09u),
    ResponseGetDkCertificate(0x0Au),
    CommandSharingRequest(0x0Bu),
    ResponseSharingRequest(0x0Cu),
    CommandRke(0x0Du),
    ResponseRke(0x0Eu),
    CommandSign(0x0Fu),
    ResponseSign(0x10u),
    CommandDisableDk(0x11u),
    ResponseDisableDk(0x12u),
    CommandEnableDk(0x13u),
    ResponseEnableDk(0x14u),
    CommandGetChallenge(0x15u),
    ResponseGetChallenge(0x16u),
    CommandGetResponse(0x17u),
    ResponseGetResponse(0x18u),
}

@OptIn(ExperimentalUnsignedTypes::class)
class Instruction {
    companion object {
        const val INSTRUCTION_TYPE_OFFSET = 0x00
        const val INSTRUCTION_TYPE_LENGTH = 0x01
        const val INSTRUCTION_DATE_OFFSET = 0x01
    }
    var type: InstructionType = InstructionType.CommandSelect
    var data: UByteArray = ubyteArrayOf()

    fun size(): Int {
        return 1 + data.size
    }
    fun serialize(): UByteArray {
        return ArrayList<UByte>().run {
            this.add(type.value)
            this.addAll(data)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size <= INSTRUCTION_TYPE_LENGTH) {
            return
        }
        type = InstructionType.values().first {
            it.value == buffer[INSTRUCTION_TYPE_OFFSET]
        }
        data = buffer.copyOfRange(INSTRUCTION_DATE_OFFSET, buffer.size)
    }
}
