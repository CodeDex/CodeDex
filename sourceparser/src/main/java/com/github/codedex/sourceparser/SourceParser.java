package com.github.codedex.sourceparser;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;

import okio.Buffer;
import okio.ByteString;

/**
 * Created by fabianterhorst on 25.09.16.
 */

public class SourceParser {

    /*file*/
    private static final ByteString PACKAGE = ByteString.encodeUtf8("package");
    private static final ByteString IMPORT = ByteString.encodeUtf8("import");
    private static final ByteString CLASS = ByteString.encodeUtf8("class");
    /*syntax*/
    private static final ByteString SEMICOLON = ByteString.encodeUtf8(";");
    private static final ByteString EMPTY = ByteString.encodeUtf8(" ");
    private static final ByteString LINE_BREAK = ByteString.encodeUtf8("\n");
    private static final ByteString BRACKET_OPENED = ByteString.encodeUtf8("{");
    private static final ByteString BRACKET_CLOSED = ByteString.encodeUtf8("}");

    public static void parse(String sourceCode) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(sourceCode);
        try {
            int nextType = 0;
            ByteString string;
            while ((string = nextByteString(buffer)) != null) {
                Log.d("string", string.utf8());
                if (nextType == 0) {
                    if (string.equals(PACKAGE)) {
                        nextType = 1;
                        long endIndex = buffer.indexOf(SEMICOLON);
                        Log.d("package", buffer.readUtf8(endIndex));
                        buffer.skip(1);//semicolon
                    }
                } else if (nextType == 1 && string.equals(IMPORT)) {
                    long endIndex = buffer.indexOf(SEMICOLON);
                    Log.d("import", buffer.readUtf8(endIndex));
                    buffer.skip(1);//semicolon
                } else if (string.equals(CLASS)) {
                    nextType = 2;
                    long endIndex = buffer.indexOf(BRACKET_OPENED);
                    Log.d("class", buffer.readUtf8(endIndex));
                    buffer.skip(1);//bracket
                }
                trimNext(buffer);
            }
        } catch (IOException eof) {
            //File end
        }
        Log.d("result", buffer.toString());//should be empty

    }

    @Nullable
    private static ByteString nextByteString(Buffer buffer) throws IOException {
        //while (buffer.rangeEquals(0, EMPTY) || buffer.rangeEquals(0, LINE_BREAK)) {
        //    buffer.skip(1);
        //}
        trimNext(buffer);
        ByteString byteString = null;
        long endIndex1 = buffer.indexOf(EMPTY);
        long endIndex2 = buffer.indexOf(LINE_BREAK);
        long endIndex = Math.min(endIndex1, endIndex1);
        if (endIndex == -1) {
            if (endIndex1 == -1) {
                endIndex = endIndex2;
            } else if (endIndex2 == -1) {
                endIndex = endIndex1;
            }
        }
        if (endIndex != -1) {
            byteString = buffer.readByteString(endIndex);
        } else {
            if (buffer.size() > 0) {
                byteString = buffer.readByteString();
            } else {
                return null;
            }
        }
        //while (buffer.rangeEquals(0, EMPTY) || buffer.rangeEquals(0, LINE_BREAK)) {
        //    buffer.skip(1);
        //}
        trimNext(buffer);
        return byteString;
    }

    private static void trimNext(Buffer buffer) throws EOFException {
        while (buffer.rangeEquals(0, EMPTY) || buffer.rangeEquals(0, LINE_BREAK)) {
            buffer.skip(1);
        }
    }
}
