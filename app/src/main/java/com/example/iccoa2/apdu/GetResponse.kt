package com.example.iccoa2.apdu

@OptIn(ExperimentalUnsignedTypes::class)
class CommandAPduGetResponse {
    companion object {
        const val GET_RESPONSE_LENGTH = 0x05
        const val CLA: UByte = 0x00u
        const val INS: UByte = 0xC0u
        const val P1: UByte = 0x00u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x00u
    }

    fun serialize(): UByteArray {
        return CommandApdu().run {
            this.header = CommandApduHeader().apply {
                this.cla = CLA
                this.ins = INS
                this.p1 = P1
                this.p2 = P2
            }
            this.trailer = CommandApduTrailer().apply {
                this.data = null
                this.le = LE
            }
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size != GET_RESPONSE_LENGTH) {
            return
        }
        val request = CommandApdu().apply {
            this.deserialize(buffer)
        }
        val header = request.header
        val trailer = request.trailer
        if (header.cla != CLA ||
            header.ins != INS ||
            header.p1 != P1 ||
            header.p2 != P2) {
            return
        }
        if (trailer?.data != null ||
            trailer?.le == null ||
            trailer.le != LE) {
            return
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduGetResponse {
    var info: UByteArray = ubyteArrayOf()
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = info
            this.trailer = status
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data == null) {
            return
        }
        info = response.data!!
        status = response.trailer
    }
}
