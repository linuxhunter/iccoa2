package com.example.iccoa2

import com.example.iccoa2.apdu.CommandApduSelect
import com.example.iccoa2.apdu.ResponseApduSelect
import com.example.iccoa2.apdu.ResponseApduTrailer
import org.junit.Test

import org.junit.Assert.*

@OptIn(ExperimentalUnsignedTypes::class)
class ApduSelectUnitTest {
    @Test
    fun create_select_request() {
        val request = CommandApduSelect().apply {
            this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
        }
        assertArrayEquals(request.aid.toByteArray(), ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u).toByteArray())
    }
    @Test
    fun update_select_request() {
        val request = CommandApduSelect().apply {
            this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
        }
        request.apply {
            this.aid = ubyteArrayOf(0x01u, 0x02u, 0x03u, 0x04u)
        }
        assertArrayEquals(request.aid.toByteArray(), ubyteArrayOf(0x01u, 0x02u, 0x03u, 0x04u).toByteArray())
    }
    @Test
    fun serialize_select_request() {
        val request = CommandApduSelect().apply {
            this.aid = ubyteArrayOf(0x10u, 0x20u, 0x30u, 0x40u)
        }
        val serializedRequest = request.serialize()
        assertArrayEquals(
            serializedRequest.toByteArray(),
            ubyteArrayOf(
                0x00u, 0xA4u, 0x04u, 0x00u,
                0x04u,
                0x10u, 0x20u, 0x30u, 0x40u,
                0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_select_request() {
        val buffer: UByteArray = ubyteArrayOf(
            0x00u, 0xA4u, 0x04u, 0x00u,
            0x04u,
            0x10u, 0x20u, 0x30u, 0x40u,
            0x00u,
        )
        val request = CommandApduSelect().apply {
            this.deserialize(buffer)
        }
        assertArrayEquals(
            request.aid.toByteArray(),
            ubyteArrayOf(
                0x10u, 0x20u, 0x30u, 0x40u,
            ).toByteArray()
        )
    }
    @Test
    fun create_select_response() {
        val response = ResponseApduSelect().apply {
            this.version = 0x0102u
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        assertEquals(response.version.toInt(), 0x0102)
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
        assertEquals(response.status.isSuccess(), true)
    }
    @Test
    fun update_select_response() {
        val response = ResponseApduSelect().apply {
            this.version = 0x0102u
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        response.apply {
            this.version = 0x0304u
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x61u
                this.sw2 = 0x10u
            }
        }
        assertEquals(response.version.toInt(), 0x0304)
        assertEquals(response.status.sw1.toInt(), 0x61)
        assertEquals(response.status.sw2.toInt(), 0x10)
        assertEquals(response.status.isSuccess(), false)
        assertEquals(response.status.isRemain(), true)
        assertEquals(response.status.remainBytes().toInt(), 0x10)
    }
    @Test
    fun serialize_select_response() {
        val response = ResponseApduSelect().apply {
            this.version = 0x0102u
            this.status = ResponseApduTrailer().apply {
                this.sw1 = 0x90u
                this.sw2 = 0x00u
            }
        }
        val serializedResponse = response.serialize()
        assertArrayEquals(
            serializedResponse.toByteArray(),
            ubyteArrayOf(
                0x01u, 0x02u,
                0x90u, 0x00u,
            ).toByteArray()
        )
    }
    @Test
    fun deserialize_select_response() {
        val buffer: UByteArray = ubyteArrayOf(
            0x01u, 0x02u,
            0x90u, 0x00u,
        )
        val response = ResponseApduSelect().apply {
            this.deserialize(buffer)
        }
        assertEquals(response.version.toInt(), 0x0102)
        assertEquals(response.status.sw1.toInt(), 0x90)
        assertEquals(response.status.sw2.toInt(), 0x00)
        assertEquals(response.status.isSuccess(), true)
    }
}