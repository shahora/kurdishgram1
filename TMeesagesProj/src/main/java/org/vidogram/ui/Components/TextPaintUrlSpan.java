package org.vidogram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintUrlSpan extends MetricAffectingSpan
{
  private int color;
  private String currentUrl;
  private TextPaint textPaint;
  private int textSize;

  public TextPaintUrlSpan(TextPaint paramTextPaint, String paramString)
  {
    this.textPaint = paramTextPaint;
    this.currentUrl = paramString;
  }

  public String getUrl()
  {
    return this.currentUrl;
  }

  public void updateDrawState(TextPaint paramTextPaint)
  {
    if (this.textPaint != null)
    {
      paramTextPaint.setColor(this.textPaint.getColor());
      paramTextPaint.setTypeface(this.textPaint.getTypeface());
      paramTextPaint.setFlags(this.textPaint.getFlags());
    }
  }

  public void updateMeasureState(TextPaint paramTextPaint)
  {
    if (this.textPaint != null)
    {
      paramTextPaint.setColor(this.textPaint.getColor());
      paramTextPaint.setTypeface(this.textPaint.getTypeface());
      paramTextPaint.setFlags(this.textPaint.getFlags());
    }
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.ui.Components.TextPaintUrlSpan
 * JD-Core Version:    0.6.0
 */