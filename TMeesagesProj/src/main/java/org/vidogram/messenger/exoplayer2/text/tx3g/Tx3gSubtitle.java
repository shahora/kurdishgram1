package org.vidogram.messenger.exoplayer2.text.tx3g;

import java.util.Collections;
import java.util.List;
import org.vidogram.messenger.exoplayer2.text.Cue;
import org.vidogram.messenger.exoplayer2.text.Subtitle;
import org.vidogram.messenger.exoplayer2.util.Assertions;

final class Tx3gSubtitle
  implements Subtitle
{
  public static final Tx3gSubtitle EMPTY = new Tx3gSubtitle();
  private final List<Cue> cues;

  private Tx3gSubtitle()
  {
    this.cues = Collections.emptyList();
  }

  public Tx3gSubtitle(Cue paramCue)
  {
    this.cues = Collections.singletonList(paramCue);
  }

  public List<Cue> getCues(long paramLong)
  {
    if (paramLong >= 0L)
      return this.cues;
    return Collections.emptyList();
  }

  public long getEventTime(int paramInt)
  {
    if (paramInt == 0);
    for (boolean bool = true; ; bool = false)
    {
      Assertions.checkArgument(bool);
      return 0L;
    }
  }

  public int getEventTimeCount()
  {
    return 1;
  }

  public int getNextEventTimeIndex(long paramLong)
  {
    if (paramLong < 0L)
      return 0;
    return -1;
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.exoplayer2.text.tx3g.Tx3gSubtitle
 * JD-Core Version:    0.6.0
 */