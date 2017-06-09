package com.github.codedex.sourceparser.code

import android.util.Log
import okio.Buffer
import okio.BufferedSource
import okio.ByteString

/**
 * Created by fabianterhorst on 30.09.16.
 */

class SourceCodeParser {

    /*file*/
    private val PACKAGE = ByteString.encodeUtf8("package")
    private val IMPORT = ByteString.encodeUtf8("import")
    private val CLASS = ByteString.encodeUtf8("class")
    private val INTERFACE = ByteString.encodeUtf8("interface")
    /*syntax*/
    private val SEMICOLON = ByteString.encodeUtf8(";")
    private val EMPTY = ByteString.encodeUtf8(" ")
    private val LINE_BREAK = ByteString.encodeUtf8("\n")
    private val BRACKET_OPENED = ByteString.encodeUtf8("{")
    private val BRACKET_CLOSED = ByteString.encodeUtf8("}")

    fun parse(buffer: BufferedSource) {
        var line: String?
        do {
            line = buffer.readUtf8Line()
            if (line != null) {
                val lineBuffer = Buffer()
                lineBuffer.writeUtf8(line)
                var index = lineBuffer.indexOf(EMPTY)
                if (index < 1) {
                    removeBlankLineAtStart(lineBuffer)
                    index = lineBuffer.indexOf(EMPTY)
                    if (index < 0) {
                        index = lineBuffer.size() - 1
                    }
                }
                if (index > 0) {
                    Log.d("read", lineBuffer.readUtf8(index))
                }
                //now read every single word
                /*var index = buffer.indexOf(IMPORT)
                if (index > 0) {
                    buffer.skip(index)
                    index = buffer.indexOf(SEMICOLON)
                    Log.d("import", buffer.readUtf8(index))
                }*/
            }
        } while (line != null)
    }

    fun removeBlankLineAtStart(buffer: Buffer) {
        while (buffer.rangeEquals(0, EMPTY)) {
            buffer.skip(1)
        }
    }
}
