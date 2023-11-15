package com.example.iccoa2.apdu

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduHeader {
    companion object {
        const val HEADER_LENGTH = 4
        const val CLA_OFFSET = 0
        const val INS_OFFSET = 1
        const val P1_OFFSET = 2
        const val P2_OFFSET = 3
    }
    var cla: UByte = 0x00u
    var ins: UByte = 0x00u
    var p1: UByte = 0x00u
    var p2: UByte = 0x00u

    fun serialize(): UByteArray {
        return arrayListOf<UByte>().run {
            this.add(cla)
            this.add(ins)
            this.add(p1)
            this.add(p2)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size < HEADER_LENGTH) {
            return
        }
        cla = buffer[CLA_OFFSET]
        ins = buffer[INS_OFFSET]
        p1 = buffer[P1_OFFSET]
        p2 = buffer[P2_OFFSET]
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduTrailer {
    companion object {
        const val MINI_LENGTH = 0x01
    }
    var data: UByteArray? = null
    var le: UByte? = null

    fun serialize(): UByteArray? {
        val bufferList = ArrayList<UByte>()
        data?.let {
            bufferList.add(it.size.toUByte())       //Lc
            bufferList.addAll(it)              //Data
        }
        le?.let { bufferList.add(it) }              //Le
        return if (bufferList.isEmpty()) {
            null
        } else {
            bufferList.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray?) {
        buffer?.let {
            if (it.size == MINI_LENGTH) {
                data = null
                le = it[0]
            } else {
                val lc = it[0]
                data = it.copyOfRange(1, 1+lc.toInt())
                le = if (1 + lc.toInt() < it.size) {
                    it[1+lc.toInt()]           //Case 4
                } else {
                    null                       //Case 3
                }
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApdu {
    companion object {
        const val MINI_LENGTH = 0x04
    }
    var header: CommandApduHeader = CommandApduHeader()
    var trailer: CommandApduTrailer? = null

    fun serialize(): UByteArray {
        val bufferList = ArrayList<UByte>()
        bufferList.addAll(header.serialize())
        trailer?.serialize()?.let { bufferList.addAll(it) }
        return bufferList.toUByteArray()
    }
    fun deserialize(buffer: UByteArray?) {
        buffer?.let {
            if (it.size < MINI_LENGTH) {
                return
            } else {
                header.deserialize(it.sliceArray(IntRange(0, MINI_LENGTH-1)))
                if (it.size > MINI_LENGTH) {
                    if (trailer == null) {
                        trailer = CommandApduTrailer()
                    }
                    trailer?.deserialize(it.sliceArray(IntRange(MINI_LENGTH, it.size-1)))
                }
            }
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduTrailer {
    companion object {
        const val TRAILER_LENGTH = 0x02
    }
    var sw1: UByte = 0x00u
    var sw2: UByte = 0x00u

    fun isSuccess(): Boolean {
        return sw1.toUInt() == 0x90u && sw2.toUInt() == 0x00u
    }
    fun isRemain(): Boolean {
        return sw1.toUInt() == 0x61u
    }
    fun remainBytes(): UByte {
        return if (sw1.toUInt() == 0x61u) {
            sw2
        } else {
            0x00u
        }
    }
    fun getErrorMessage(): String {
        val sw1 = sw1.toUInt()
        val sw2 = sw2.toUInt()
        val errorMessage = if (sw1 == 0x6Eu && sw2 == 0x00u) {
            "Invalid CLA"
        } else if (sw1 == 0x6Du && sw2 == 0x00u) {
            "Invalid INS"
        } else if (sw1 == 0x67u && sw2 == 0x00u) {
            "Data Length Error"
        } else if (sw1 == 0x6Au) {
            when (sw2) {
                0x88u -> {
                    "Reference Data is not exist"
                }
                0x86u -> {
                    "P1/P2 parameter error"
                }
                0x82u -> {
                    "Application or File is not exist"
                }
                0x80u -> {
                    "Data Format Error"
                }
                else -> {
                    "Unsupported Status Error ${sw1}/${sw2}"
                }
            }
        } else {
            "Unsupported Status Error ${sw1}/${sw2}"
        }
        return errorMessage
    }
    fun serialize(): UByteArray {
        return arrayListOf<UByte>().run {
            this.add(sw1)
            this.add(sw2)
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        if (buffer.size != TRAILER_LENGTH) {
            return
        }
        sw1 = buffer[0]
        sw2 = buffer[1]
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApdu {
    companion object {
        const val MINI_LENGTH = 0x02
    }
    var data: UByteArray? = null
    var trailer: ResponseApduTrailer = ResponseApduTrailer()

    fun serialize(): UByteArray {
        val bufferList = ArrayList<UByte>()
        data?.apply {
            bufferList.addAll(this)
        }
        bufferList.addAll(trailer.serialize())
        return bufferList.toUByteArray()
    }
    fun deserialize(buffer: UByteArray) {
        data = if (buffer.size > MINI_LENGTH) {
            buffer.copyOfRange(0, buffer.size-2)
        } else {
            null
        }
        trailer.deserialize(buffer.copyOfRange(buffer.size-2, buffer.size))
    }
}
