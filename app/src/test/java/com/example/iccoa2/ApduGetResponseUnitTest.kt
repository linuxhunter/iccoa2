package com.example.iccoa2

import com.example.iccoa2.apdu.CommandAPduGetResponse
import com.example.iccoa2.apdu.ResponseApduGetResponse
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduGetResponseUnitTest {
    @Test
    fun serialize_get_response_request() {
        val request = CommandAPduGetResponse()
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0xC0u, 0x00u, 0x00u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_get_response_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0xC0u, 0x00u, 0x00u,
            0x00u,
        )
        CommandAPduGetResponse().apply {
            this.deserialize(buffer)
        }
    }
    @Test
    fun create_get_response_response() {
        val response = ResponseApduGetResponse().apply {
            this.info = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertArrayEquals(
            response.info.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
    @Test
    fun update_get_response_response() {
        val response = ResponseApduGetResponse().apply {
            this.info = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }

        response.apply {
            this.info = ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertArrayEquals(
            response.info.toByteArray(),
            ubyteArrayOf(
                0x08u, 0x09u, 0x0Au, 0x0Bu, 0x0Cu, 0x0Du, 0x0Eu, 0x0Fu
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
    }
    @Test
    fun serialize_get_response_response() {
        val response = ResponseApduGetResponse().apply {
            this.info = ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            )
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_get_response_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            0x90u, 0x00u,
        )
        val response = ResponseApduGetResponse().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            response.info.toByteArray(),
            ubyteArrayOf(
                0x00u, 0x01u, 0x02u, 0x03u, 0x04u, 0x05u, 0x06u, 0x07u,
            ).toByteArray()
        )
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
    }
}