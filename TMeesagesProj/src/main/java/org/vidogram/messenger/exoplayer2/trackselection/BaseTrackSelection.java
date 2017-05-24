package org.vidogram.messenger.exoplayer2.trackselection;

import android.os.SystemClock;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.vidogram.messenger.exoplayer2.Format;
import org.vidogram.messenger.exoplayer2.source.TrackGroup;
import org.vidogram.messenger.exoplayer2.source.chunk.MediaChunk;
import org.vidogram.messenger.exoplayer2.util.Assertions;

public abstract class BaseTrackSelection
  implements TrackSelection
{
  private final long[] blacklistUntilTimes;
  private final Format[] formats;
  protected final TrackGroup group;
  private int hashCode;
  protected final int length;
  protected final int[] tracks;

  public BaseTrackSelection(TrackGroup paramTrackGroup, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length > 0);
    for (boolean bool = true; ; bool = false)
    {
      Assertions.checkState(bool);
      this.group = ((TrackGroup)Assertions.checkNotNull(paramTrackGroup));
      this.length = paramArrayOfInt.length;
      this.formats = new Format[this.length];
      i = 0;
      while (i < paramArrayOfInt.length)
      {
        this.formats[i] = paramTrackGroup.getFormat(paramArrayOfInt[i]);
        i += 1;
      }
    }
    Arrays.sort(this.formats, new DecreasingBandwidthComparator(null));
    this.tracks = new int[this.length];
    int i = j;
    while (i < this.length)
    {
      this.tracks[i] = paramTrackGroup.indexOf(this.formats[i]);
      i += 1;
    }
    this.blacklistUntilTimes = new long[this.length];
  }

  public final boolean blacklist(int paramInt, long paramLong)
  {
    long l = SystemClock.elapsedRealtime();
    boolean bool = isBlacklisted(paramInt, l);
    int i = 0;
    if ((i < this.length) && (!bool))
    {
      if ((i != paramInt) && (!isBlacklisted(i, l)));
      for (bool = true; ; bool = false)
      {
        i += 1;
        break;
      }
    }
    if (!bool)
      return false;
    this.blacklistUntilTimes[paramInt] = Math.max(this.blacklistUntilTimes[paramInt], l + paramLong);
    return true;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    do
    {
      return true;
      if ((paramObject == null) || (getClass() != paramObject.getClass()))
        return false;
      paramObject = (BaseTrackSelection)paramObject;
    }
    while ((this.group == paramObject.group) && (Arrays.equals(this.tracks, paramObject.tracks)));
    return false;
  }

  public int evaluateQueueSize(long paramLong, List<? extends MediaChunk> paramList)
  {
    return paramList.size();
  }

  public final Format getFormat(int paramInt)
  {
    return this.formats[paramInt];
  }

  public final int getIndexInTrackGroup(int paramInt)
  {
    return this.tracks[paramInt];
  }

  public final Format getSelectedFormat()
  {
    return this.formats[getSelectedIndex()];
  }

  public final int getSelectedIndexInTrackGroup()
  {
    return this.tracks[getSelectedIndex()];
  }

  public final TrackGroup getTrackGroup()
  {
    return this.group;
  }

  public int hashCode()
  {
    if (this.hashCode == 0)
      this.hashCode = (System.identityHashCode(this.group) * 31 + Arrays.hashCode(this.tracks));
    return this.hashCode;
  }

  public final int indexOf(int paramInt)
  {
    int i = 0;
    while (i < this.length)
    {
      if (this.tracks[i] == paramInt)
        return i;
      i += 1;
    }
    return -1;
  }

  public final int indexOf(Format paramFormat)
  {
    int i = 0;
    while (i < this.length)
    {
      if (this.formats[i] == paramFormat)
        return i;
      i += 1;
    }
    return -1;
  }

  protected final boolean isBlacklisted(int paramInt, long paramLong)
  {
    return this.blacklistUntilTimes[paramInt] > paramLong;
  }

  public final int length()
  {
    return this.tracks.length;
  }

  private static final class DecreasingBandwidthComparator
    implements Comparator<Format>
  {
    public int compare(Format paramFormat1, Format paramFormat2)
    {
      return paramFormat2.bitrate - paramFormat1.bitrate;
    }
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.exoplayer2.trackselection.BaseTrackSelection
 * JD-Core Version:    0.6.0
 */