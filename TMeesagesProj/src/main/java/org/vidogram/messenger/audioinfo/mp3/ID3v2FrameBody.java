package org.vidogram.messenger.audioinfo.mp3;

import java.io.InputStream;
import java.nio.charset.Charset;
import org.vidogram.messenger.audioinfo.util.RangeInputStream;

public class ID3v2FrameBody
{
  static final ThreadLocal<Buffer> textBuffer = new ThreadLocal()
  {
    protected ID3v2FrameBody.Buffer initialValue()
    {
      return new ID3v2FrameBody.Buffer(4096);
    }
  };
  private final ID3v2DataInput data;
  private final ID3v2FrameHeader frameHeader;
  private final RangeInputStream input;
  private final ID3v2TagHeader tagHeader;

  ID3v2FrameBody(InputStream paramInputStream, long paramLong, int paramInt, ID3v2TagHeader paramID3v2TagHeader, ID3v2FrameHeader paramID3v2FrameHeader)
  {
    this.input = new RangeInputStream(paramInputStream, paramLong, paramInt);
    this.data = new ID3v2DataInput(this.input);
    this.tagHeader = paramID3v2TagHeader;
    this.frameHeader = paramID3v2FrameHeader;
  }

  private String extractString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, ID3v2Encoding paramID3v2Encoding, boolean paramBoolean)
  {
    int k = paramInt2;
    int j;
    int i;
    if (paramBoolean)
    {
      j = 0;
      i = 0;
    }
    while (true)
    {
      k = paramInt2;
      if (j < paramInt2)
      {
        if ((paramArrayOfByte[(paramInt1 + j)] != 0) || ((paramID3v2Encoding == ID3v2Encoding.UTF_16) && (i == 0) && ((paramInt1 + j) % 2 != 0)))
          break label141;
        k = i + 1;
        i = k;
        if (k != paramID3v2Encoding.getZeroBytes())
          break label144;
        k = j + 1 - paramID3v2Encoding.getZeroBytes();
      }
      try
      {
        paramID3v2Encoding = new String(paramArrayOfByte, paramInt1, k, paramID3v2Encoding.getCharset().name());
        paramArrayOfByte = paramID3v2Encoding;
        if (paramID3v2Encoding.length() > 0)
        {
          paramArrayOfByte = paramID3v2Encoding;
          if (paramID3v2Encoding.charAt(0) == 65279)
            paramArrayOfByte = paramID3v2Encoding.substring(1);
        }
        return paramArrayOfByte;
        label141: i = 0;
        label144: j += 1;
      }
      catch (java.lang.Exception paramArrayOfByte)
      {
      }
    }
    return "";
  }

  public ID3v2DataInput getData()
  {
    return this.data;
  }

  public ID3v2FrameHeader getFrameHeader()
  {
    return this.frameHeader;
  }

  public long getPosition()
  {
    return this.input.getPosition();
  }

  public long getRemainingLength()
  {
    return this.input.getRemainingLength();
  }

  public ID3v2TagHeader getTagHeader()
  {
    return this.tagHeader;
  }

  public ID3v2Encoding readEncoding()
  {
    int i = this.data.readByte();
    switch (i)
    {
    default:
      throw new ID3v2Exception("Invalid encoding: " + i);
    case 0:
      return ID3v2Encoding.ISO_8859_1;
    case 1:
      return ID3v2Encoding.UTF_16;
    case 2:
      return ID3v2Encoding.UTF_16BE;
    case 3:
    }
    return ID3v2Encoding.UTF_8;
  }

  public String readFixedLengthString(int paramInt, ID3v2Encoding paramID3v2Encoding)
  {
    if (paramInt > getRemainingLength())
      throw new ID3v2Exception("Could not read fixed-length string of length: " + paramInt);
    byte[] arrayOfByte = ((Buffer)textBuffer.get()).bytes(paramInt);
    this.data.readFully(arrayOfByte, 0, paramInt);
    return extractString(arrayOfByte, 0, paramInt, paramID3v2Encoding, true);
  }

  public String readZeroTerminatedString(int paramInt, ID3v2Encoding paramID3v2Encoding)
  {
    int m = Math.min(paramInt, (int)getRemainingLength());
    byte[] arrayOfByte = ((Buffer)textBuffer.get()).bytes(m);
    int j = 0;
    paramInt = 0;
    while (j < m)
    {
      int i = this.data.readByte();
      arrayOfByte[j] = i;
      if ((i == 0) && ((paramID3v2Encoding != ID3v2Encoding.UTF_16) || (paramInt != 0) || (j % 2 == 0)))
      {
        int k = paramInt + 1;
        paramInt = k;
        if (k == paramID3v2Encoding.getZeroBytes())
          return extractString(arrayOfByte, 0, j + 1 - paramID3v2Encoding.getZeroBytes(), paramID3v2Encoding, false);
      }
      else
      {
        paramInt = 0;
      }
      j += 1;
    }
    throw new ID3v2Exception("Could not read zero-termiated string");
  }

  public String toString()
  {
    return "id3v2frame[pos=" + getPosition() + ", " + getRemainingLength() + " left]";
  }

  static final class Buffer
  {
    byte[] bytes;

    Buffer(int paramInt)
    {
      this.bytes = new byte[paramInt];
    }

    byte[] bytes(int paramInt)
    {
      if (paramInt > this.bytes.length)
      {
        int i = this.bytes.length * 2;
        while (paramInt > i)
          i *= 2;
        this.bytes = new byte[i];
      }
      return this.bytes;
    }
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.audioinfo.mp3.ID3v2FrameBody
 * JD-Core Version:    0.6.0
 */