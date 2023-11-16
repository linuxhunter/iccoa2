package com.example.iccoa2.apdu

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduGetChallenge {
    companion object {
        const val REQUEST_GET_CHALLENGE_LENGTH = 0x05
        const val CLA: UByte = 0x00u
        const val INS: UByte = 0x84u
        const val P1: UByte = 0x00u
        const val P2: UByte = 0x00u
        const val LE: UByte = 0x08u
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
        if (buffer.size != REQUEST_GET_CHALLENGE_LENGTH) {
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
class ResponseApduGetChallenge {
    companion object {
        const val RESPONSE_GET_CHALLENGE_LENGTH = 0x0A
    }
    var random: UByteArray = ubyteArrayOf()
    var status: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        return ResponseApdu().run {
            this.data = random
            this.trailer = status
            this.serialize()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size != RESPONSE_GET_CHALLENGE_LENGTH) {
            return
        }
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        if (response.data == null) {
            return
        }
        random = response.data!!
        status = response.trailer
    }
}
