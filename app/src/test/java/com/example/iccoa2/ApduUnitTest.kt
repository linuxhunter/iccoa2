package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApdu
import com.example.iccoa2.apdu.CommandApduHeader
import com.example.iccoa2.apdu.CommandApduTrailer
import com.example.iccoa2.apdu.ResponseApdu
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduHeaderUnitTest {
    @Test
    fun create_command_apdu_header() {
        val header = CommandApduHeader()
        assertEquals(header.cla.toUInt(), 0x00u)
        assertEquals(header.ins.toUInt(), 0x00u)
        assertEquals(header.p1.toUInt(), 0x00u)
        assertEquals(header.p2.toUInt(), 0x00u)
    }

    @Test
    fun update_command_apdu_header() {
        val header = CommandApduHeader()
        header.cla = 0x01u
        header.ins = 0x02u
        header.p1 = 0x03u
        header.p2 = 0x04u
        assertEquals(header.cla.toUInt(), 0x01u)
        assertEquals(header.ins.toUInt(), 0x02u)
        assertEquals(header.p1.toUInt(), 0x03u)
        assertEquals(header.p2.toUInt(), 0x04u)
    }

    @Test
    fun serialize_command_apdu_header() {
        val header = CommandApduHeader()
        header.cla = 0x01u
        header.ins = 0x02u
        header.p1 = 0x03u
        header.p2 = 0x04u
        assertArrayEquals(
            header.serialize().toByteArray(),
            ubyteArrayOf(0x01u, 0x02u, 0x03u, 0x04u).toByteArray()
        )
    }

    @Test
    fun deserialize_command_apdu_header() {
        val buffer = ubyteArrayOf(0x01u, 0x02u, 0x03u, 0x04u)
        val header = CommandApduHeader()
        header.deserialize(buffer)
        assertEquals(header.cla.toUInt(), 0x01u)
        assertEquals(header.ins.toUInt(), 0x02u)
        assertEquals(header.p1.toUInt(), 0x03u)
        assertEquals(header.p2.toUInt(), 0x04u)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduTrailerUnitTest {
    @Test
    fun create_command_apdu_trailer() {
        val trailer = CommandApduTrailer()
        assertNull(trailer.data)
        assertNull(trailer.le)
    }

    @Test
    fun update_command_apdu_trailer() {
        val trailer = CommandApduTrailer()
        trailer.data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        trailer.le = 0x00u
        assertNotNull(trailer.data)
        assertNotNull(trailer.le)
        assertArrayEquals(
            trailer.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertEquals(trailer.le?.toInt(), 0x00)

        trailer.le = null
        assertArrayEquals(
            trailer.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertNull(trailer.le)

        trailer.data = null
        assertNull(trailer.data)
        assertNull(trailer.le)
    }

    @Test
    fun serialize_command_apdu_trailer() {
        val trailer = CommandApduTrailer()
        trailer.data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        trailer.le = 0x00u
        var serializedTrailer = trailer.serialize()
        assertNotNull(serializedTrailer)
        assertArrayEquals(
            serializedTrailer?.toByteArray(),
            ubyteArrayOf(
                0x08u,
                0x00u,
                0x01u,
                0x02u,
                0x03u,
                0x04u,
                0x05u,
                0x06u,
                0x07u,
                0x00u
            ).toByteArray()
        )

        trailer.data = null
        serializedTrailer = trailer.serialize()
        assertNotNull(serializedTrailer)
        assertArrayEquals(serializedTrailer?.toByteArray(), ubyteArrayOf(0x00u).toByteArray())

        trailer.data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        trailer.le = null
        serializedTrailer = trailer.serialize()
        assertNotNull(serializedTrailer)
        assertArrayEquals(
            serializedTrailer?.toByteArray(),
            ubyteArrayOf(
                0x08u,
                0x00u,
                0x01u,
                0x02u,
                0x03u,
                0x04u,
                0x05u,
                0x06u,
                0x07u
            ).toByteArray()
        )

        trailer.data = null
        trailer.le = null
        serializedTrailer = trailer.serialize()
        assertNull(serializedTrailer)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class CommandApduUnitTest {
    @Test
    fun create_command_apdu() {
        val header = CommandApduHeader().apply {
            this.cla = 0x01u
            this.ins = 0x02u
            this.p1 = 0x03u
            this.p2 = 0x04u
        }
        val trailer = CommandApduTrailer().apply {
            this.data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
            this.le = 0x00u
        }
        val commandApdu = CommandApdu().apply {
            this.header = header
            this.trailer = trailer
        }
        assertEquals(commandApdu.header.cla.toUInt(), 0x01u)
        assertEquals(commandApdu.header.ins.toUInt(), 0x02u)
        assertEquals(commandApdu.header.p1.toUInt(), 0x03u)
        assertEquals(commandApdu.header.p2.toUInt(), 0x04u)
        assertNotNull(commandApdu.trailer)
        assertArrayEquals(
            commandApdu.trailer?.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertEquals(commandApdu.trailer?.le?.toInt(), 0x00)
    }

    @Test
    fun update_command_apdu() {
        val header = CommandApduHeader()
        val trailer = CommandApduTrailer()
        val commandApdu = CommandApdu().apply {
            this.header = header
            this.trailer = trailer
        }
        val newHeader = CommandApduHeader().apply {
            this.cla = 0xAAu
            this.ins = 0xBBu
            this.p1 = 0xCCu
            this.p2 = 0xDDu
        }
        val newTrailer = CommandApduTrailer().apply {
            this.data = ubyteArrayOf(0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu)
            this.le = 0xFFu
        }
        commandApdu.apply {
            this.header = newHeader
            this.trailer = newTrailer
        }
        assertEquals(commandApdu.header.cla.toUInt(), 0xAAu)
        assertEquals(commandApdu.header.ins.toUInt(), 0xBBu)
        assertEquals(commandApdu.header.p1.toUInt(), 0xCCu)
        assertEquals(commandApdu.header.p2.toUInt(), 0xDDu)
        assertArrayEquals(
            commandApdu.trailer?.data?.toByteArray(),
            ubyteArrayOf(0xAAu, 0xBBu, 0xCCu, 0xDDu, 0xEEu, 0xFFu).toByteArray()
        )
        assertEquals(commandApdu.trailer?.le?.toInt(), 0xFF)
    }

    @Test
    fun serialize_command_apdu() {
        val header = CommandApduHeader().apply {
            this.cla = 0x01u
            this.ins = 0x02u
            this.p1 = 0x03u
            this.p2 = 0x04u
        }
        val trailer = CommandApduTrailer().apply {
            this.data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
            this.le = 0x00u
        }
        val commandApdu = CommandApdu().apply {
            this.header = header
            this.trailer = trailer
        }
        val serializedCommandApdu = commandApdu.serialize()
        assertArrayEquals(
            serializedCommandApdu.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x02u, 0x03u, 0x04u,
                0x08u,
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
                0x00u,
            ).toByteArray()
        )
    }

    @Test
    fun deserialize_command_apdu() {
        val buffer = ubyteArrayOf(
            0x01u, 0x02u, 0x03u, 0x04u,
            0x08u,
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            0x00u,
        )
        val commandApdu = CommandApdu().apply {
            this.deserialize(buffer)
        }
        assertEquals(commandApdu.header.cla.toUInt(), 0x01u)
        assertEquals(commandApdu.header.ins.toUInt(), 0x02u)
        assertEquals(commandApdu.header.p1.toUInt(), 0x03u)
        assertEquals(commandApdu.header.p2.toUInt(), 0x04u)
        assertArrayEquals(
            commandApdu.trailer?.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertEquals(commandApdu.trailer?.le?.toInt(), 0x00)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduTrailerUnitTest {
    @Test
    fun create_response_apdu_trailer() {
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        assertEquals(trailer.sw1.toUInt(), 0x090u)
        assertEquals(trailer.sw2.toUInt(), 0x00u)
    }

    @Test
    fun update_response_apdu_trailer() {
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        trailer.apply {
            this.sw1 = 0x61u
            this.sw2 = 0x10u
        }
        assertEquals(trailer.sw1.toUInt(), 0x061u)
        assertEquals(trailer.sw2.toUInt(), 0x10u)
    }

    @Test
    fun contents_response_apdu_trailer() {
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        assertEquals(trailer.isSuccess(), true)
        assertEquals(trailer.isRemain(), false)

        trailer.apply {
            this.sw1 = 0x61u
            this.sw2 = 0x10u
        }
        assertEquals(trailer.isSuccess(), false)
        assertEquals(trailer.isRemain(), true)
        assertEquals(trailer.remainBytes().toUInt(), 0x10u)

        trailer.apply {
            this.sw1 = 0x6Eu
            this.sw2 = 0x00u
        }
        assertEquals(trailer.isSuccess(), false)
        assertEquals(trailer.isRemain(), false)
        assertEquals(trailer.getErrorMessage(), "Invalid CLA")
    }

    @Test
    fun serialize_response_apdu_trailer() {
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val serializedTrailer = trailer.serialize()
        assertArrayEquals(
            serializedTrailer.toByteArray(),
            ubyteArrayOf(0x90u, 0x00u).toByteArray()
        )
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class ResponseApduUnitTest {
    @Test
    fun create_response_apdu() {
        val data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApdu().apply {
            this.data = data
            this.trailer = trailer
        }
        assertNotNull(response.data)
        assertArrayEquals(
            response.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertEquals(response.trailer.sw1.toUInt(), 0x90u)
        assertEquals(response.trailer.sw2.toUInt(), 0x00u)
    }

    @Test
    fun update_response_apdu() {
        val data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApdu().apply {
            this.data = data
            this.trailer = trailer
        }
        response.apply {
            this.data = ubyteArrayOf(0x07u, 0x06u, 0x05u, 0x04u, 0x03u, 0x02u, 0x01u, 0x00u)
            this.trailer = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertNotNull(response.data)
        assertArrayEquals(
            response.data?.toByteArray(),
            ubyteArrayOf(0x07u, 0x06u, 0x05u, 0x04u, 0x03u, 0x02u, 0x01u, 0x00u).toByteArray()
        )
        assertEquals(response.trailer.sw1.toUInt(), 0x61u)
        assertEquals(response.trailer.sw2.toUInt(), 0x10u)

        response.apply {
            this.data = null
        }
        assertNull(response.data)
        assertEquals(response.trailer.sw1.toUInt(), 0x61u)
        assertEquals(response.trailer.sw2.toUInt(), 0x10u)
    }

    @Test
    fun serialize_response_apdu() {
        val data = ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u)
        val trailer = ResponseApduTrailer().apply {
            this.sw1 = 0x90u
            this.sw2 = 0x00u
        }
        val response = ResponseApdu().apply {
            this.data = data
            this.trailer = trailer
        }
        var serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
                0x90u, 0x00u
            ).toByteArray()
        )

        response.apply {
            this.data = null
        }
        serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x90u, 0x00u
            ).toByteArray()
        )

    }

    @Test
    fun deserialize_response_apdu() {
        var buffer = ubyteArrayOf(
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            0x90u, 0x00u
        )
        val response = ResponseApdu().apply {
            this.deserialize(buffer)
        }
        assertNotNull(response.data)
        assertArrayEquals(
            response.data?.toByteArray(),
            ubyteArrayOf(0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u).toByteArray()
        )
        assertEquals(response.trailer.sw1.toUInt(), 0x90u)
        assertEquals(response.trailer.sw2.toUInt(), 0x00u)

        buffer = ubyteArrayOf(0x90u, 0x00u)
        response.apply {
            this.deserialize(buffer)
        }
        assertNull(response.data)
        assertEquals(response.trailer.sw1.toUInt(), 0x90u)
        assertEquals(response.trailer.sw2.toUInt(), 0x00u)
    }
}
