/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.orbeon.apache.xerces.impl.io

import java.io.IOException
import java.io.InputStream
import java.io.Reader

import org.orbeon.apache.xerces.impl.msg.XMLMessageFormatter
import org.orbeon.apache.xerces.util.MessageFormatter

object ASCIIReader {

  /**
   Default byte buffer size (2048).
   */
  val DEFAULT_BUFFER_SIZE = 2048
}

/**
 * A simple ASCII byte reader. This is an optimized reader for reading
 * byte streams that only contain 7-bit ASCII characters.
 */
class ASCIIReader(
  protected val fInputStream : InputStream,
  protected val fBuffer      : Array[Byte],
  val fFormatter             : MessageFormatter
) extends Reader {

  /**
   * Constructs an ASCII reader from the specified input stream
   * and buffer size.
   *
   * @param inputStream The input stream.
   * @param size        The initial buffer size.
   * @param messageFormatter  the MessageFormatter to use to message reporting.
   */
  def this(
    inputStream      : InputStream,
    size             : Int,
    messageFormatter : MessageFormatter
  ) =
    this(inputStream, new Array[Byte](size), messageFormatter)

  /**
   * Constructs an ASCII reader from the specified input stream
   * using the default buffer size.
   *
   * @param inputStream The input stream.
   * @param messageFormatter  the MessageFormatter to use to message reporting.
   */
  def this(inputStream: InputStream, messageFormatter: MessageFormatter) =
    this(inputStream, ASCIIReader.DEFAULT_BUFFER_SIZE, messageFormatter)

  /**
   * Read a single character.  This method will block until a character is
   * available, an I/O error occurs, or the end of the stream is reached.
   *
   *  Subclasses that intend to support efficient single-character input
   * should override this method.
   *
   * @return     The character read, as an integer in the range 0 to 127
   *             (`0x00-0x7f`), or -1 if the end of the stream has
   *             been reached
   *
   * @throws  IOException  If an I/O error occurs
   */
  override def read(): Int = {
    val b0 = fInputStream.read()
    if (b0 >= 0x80) {
      throw new MalformedByteSequenceException(
        fFormatter,
        XMLMessageFormatter.XML_DOMAIN,
        "InvalidASCII",
        Array(b0.toString)
      )
    }
    b0
  }

  /**
   * Read characters into a portion of an array.  This method will block
   * until some input is available, an I/O error occurs, or the end of the
   * stream is reached.
   *
   * @param      ch     Destination buffer
   * @param      offset Offset at which to start storing characters
   * @param      _length Maximum number of characters to read
   *
   * @return     The number of characters read, or -1 if the end of the
   *             stream has been reached
   *
   * @throws  IOException  If an I/O error occurs
   */
  def read(ch: Array[Char], offset: Int, _length: Int): Int = {

    var length = _length

    if (length > fBuffer.length) {
      length = fBuffer.length
    }
    val count = fInputStream.read(fBuffer, 0, length)
    for (i <- 0 until count) {
      val b0 = fBuffer(i)
      if (b0 < 0) {
        throw new MalformedByteSequenceException(fFormatter, XMLMessageFormatter.XML_DOMAIN,
          "InvalidASCII", Array(Integer toString b0 & 0x0FF))
      }
      ch(offset + i) = b0.toChar
    }
    count
  }

  /**
   * Skip characters.  This method will block until some characters are
   * available, an I/O error occurs, or the end of the stream is reached.
   *
   * @param  n  The number of characters to skip
   *
   * @return    The number of characters actually skipped
   *
   * @throws  IOException  If an I/O error occurs
   */
  override def skip(n: Long): Long = fInputStream.skip(n)

  /**
   * Tell whether this stream is ready to be read.
   *
   * @return True if the next read() is guaranteed not to block for input,
   * false otherwise.  Note that returning false does not guarantee that the
   * next read will block.
   *
   * @throws  IOException  If an I/O error occurs
   */
  override def ready(): Boolean = false

  /**
   * Tell whether this stream supports the mark() operation.
   */
  override def markSupported(): Boolean = fInputStream.markSupported()

  /**
   * Mark the present position in the stream.  Subsequent calls to reset()
   * will attempt to reposition the stream to this point.  Not all
   * character-input streams support the mark() operation.
   *
   * @param  readAheadLimit  Limit on the number of characters that may be
   *                         read while still preserving the mark.  After
   *                         reading this many characters, attempting to
   *                         reset the stream may fail.
   *
   * @throws  IOException  If the stream does not support mark(),
   *                          or if some other I/O error occurs
   */
  override def mark(readAheadLimit: Int): Unit = {
    fInputStream.mark(readAheadLimit)
  }

  /**
   * Reset the stream.  If the stream has been marked, then attempt to
   * reposition it at the mark.  If the stream has not been marked, then
   * attempt to reset it in some way appropriate to the particular stream,
   * for example by repositioning it to its starting point.  Not all
   * character-input streams support the reset() operation, and some support
   * reset() without supporting mark().
   *
   * @throws  IOException  If the stream has not been marked,
   *                          or if the mark has been invalidated,
   *                          or if the stream does not support reset(),
   *                          or if some other I/O error occurs
   */
  override def reset(): Unit = {
    fInputStream.reset()
  }

  /**
   * Close the stream.  Once a stream has been closed, further read(),
   * ready(), mark(), or reset() invocations will throw an IOException.
   * Closing a previously-closed stream, however, has no effect.
   *
   * @throws  IOException  If an I/O error occurs
   */
  def close(): Unit = {
    fInputStream.close()
  }
}
