package org.vidogram.messenger.exoplayer2.extractor.wav;

final class WavHeader
{
  private final int averageBytesPerSecond;
  private final int bitsPerSample;
  private final int blockAlignment;
  private long dataSize;
  private long dataStartPosition;
  private final int encoding;
  private final int numChannels;
  private final int sampleRateHz;

  public WavHeader(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this.numChannels = paramInt1;
    this.sampleRateHz = paramInt2;
    this.averageBytesPerSecond = paramInt3;
    this.blockAlignment = paramInt4;
    this.bitsPerSample = paramInt5;
    this.encoding = paramInt6;
  }

  public int getBitrate()
  {
    return this.sampleRateHz * this.bitsPerSample * this.numChannels;
  }

  public int getBytesPerFrame()
  {
    return this.blockAlignment;
  }

  public long getDurationUs()
  {
    return this.dataSize / this.blockAlignment * 1000000L / this.sampleRateHz;
  }

  public int getEncoding()
  {
    return this.encoding;
  }

  public int getNumChannels()
  {
    return this.numChannels;
  }

  public long getPosition(long paramLong)
  {
    return Math.min(this.averageBytesPerSecond * paramLong / 1000000L / this.blockAlignment * this.blockAlignment, this.dataSize - this.blockAlignment) + this.dataStartPosition;
  }

  public int getSampleRateHz()
  {
    return this.sampleRateHz;
  }

  public long getTimeUs(long paramLong)
  {
    return 1000000L * paramLong / this.averageBytesPerSecond;
  }

  public boolean hasDataBounds()
  {
    return (this.dataStartPosition != 0L) && (this.dataSize != 0L);
  }

  public void setDataBounds(long paramLong1, long paramLong2)
  {
    this.dataStartPosition = paramLong1;
    this.dataSize = paramLong2;
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.exoplayer2.extractor.wav.WavHeader
 * JD-Core Version:    0.6.0
 */