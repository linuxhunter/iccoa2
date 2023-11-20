package com.example.iccoa2.ble

import com.example.iccoa2.apdu.Instruction

@OptIn(ExperimentalUnsignedTypes::class)
class Apdu {
    var data: ArrayList<Instruction> = arrayListOf()

    fun serialize(): UByteArray {
        return ArrayList<UByte>().run {
            for (item in data.iterator()) {
                this.add(item.size().toUByte())
                this.addAll(item.serialize())
            }
            this.toUByteArray()
        }
    }
    fun deserialize(buffer: UByteArray) {
        var index = 0x00
        while (index < buffer.size) {
            val length = buffer[index]
            val instruction = Instruction().apply {
                this.deserialize(buffer.copyOfRange(index+1, index+1+length.toInt()))
            }
            data.add(instruction)
            index += 1 + length.toInt()
        }
    }
}
