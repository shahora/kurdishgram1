package org.vidogram.ui.Components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import org.vidogram.messenger.ImageReceiver;
import org.vidogram.tgnet.TLObject;
import org.vidogram.tgnet.TLRPC.FileLocation;

public class BackupImageView extends View
{
  private int height = -1;
  private ImageReceiver imageReceiver;
  private int width = -1;

  public BackupImageView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public BackupImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public BackupImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void init()
  {
    this.imageReceiver = new ImageReceiver(this);
  }

  public ImageReceiver getImageReceiver()
  {
    return this.imageReceiver;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.imageReceiver.onAttachedToWindow();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.imageReceiver.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if ((this.width != -1) && (this.height != -1))
      this.imageReceiver.setImageCoords((getWidth() - this.width) / 2, (getHeight() - this.height) / 2, this.width, this.height);
    while (true)
    {
      this.imageReceiver.draw(paramCanvas);
      return;
      this.imageReceiver.setImageCoords(0, 0, getWidth(), getHeight());
    }
  }

  public void setAspectFit(boolean paramBoolean)
  {
    this.imageReceiver.setAspectFit(paramBoolean);
  }

  public void setImage(String paramString1, String paramString2, Drawable paramDrawable)
  {
    setImage(null, paramString1, paramString2, paramDrawable, null, null, null, null, 0);
  }

  public void setImage(TLObject paramTLObject, String paramString, Bitmap paramBitmap)
  {
    setImage(paramTLObject, null, paramString, null, paramBitmap, null, null, null, 0);
  }

  public void setImage(TLObject paramTLObject, String paramString, Bitmap paramBitmap, int paramInt)
  {
    setImage(paramTLObject, null, paramString, null, paramBitmap, null, null, null, paramInt);
  }

  public void setImage(TLObject paramTLObject, String paramString, Drawable paramDrawable)
  {
    setImage(paramTLObject, null, paramString, paramDrawable, null, null, null, null, 0);
  }

  public void setImage(TLObject paramTLObject, String paramString, Drawable paramDrawable, int paramInt)
  {
    setImage(paramTLObject, null, paramString, paramDrawable, null, null, null, null, paramInt);
  }

  public void setImage(TLObject paramTLObject, String paramString1, String paramString2, Drawable paramDrawable)
  {
    setImage(paramTLObject, null, paramString1, paramDrawable, null, null, null, paramString2, 0);
  }

  public void setImage(TLObject paramTLObject, String paramString1, String paramString2, Drawable paramDrawable, Bitmap paramBitmap, TLRPC.FileLocation paramFileLocation, String paramString3, String paramString4, int paramInt)
  {
    if (paramBitmap != null)
      paramDrawable = new BitmapDrawable(null, paramBitmap);
    while (true)
    {
      this.imageReceiver.setImage(paramTLObject, paramString1, paramString2, paramDrawable, paramFileLocation, paramString3, paramInt, paramString4, false);
      return;
    }
  }

  public void setImage(TLObject paramTLObject, String paramString, TLRPC.FileLocation paramFileLocation, int paramInt)
  {
    setImage(paramTLObject, null, paramString, null, null, paramFileLocation, null, null, paramInt);
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    this.imageReceiver.setImageBitmap(paramBitmap);
  }

  public void setImageDrawable(Drawable paramDrawable)
  {
    this.imageReceiver.setImageBitmap(paramDrawable);
  }

  public void setImageResource(int paramInt)
  {
    Drawable localDrawable = getResources().getDrawable(paramInt);
    this.imageReceiver.setImageBitmap(localDrawable);
    invalidate();
  }

  public void setOrientation(int paramInt, boolean paramBoolean)
  {
    this.imageReceiver.setOrientation(paramInt, paramBoolean);
  }

  public void setRoundRadius(int paramInt)
  {
    this.imageReceiver.setRoundRadius(paramInt);
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    this.width = paramInt1;
    this.height = paramInt2;
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.ui.Components.BackupImageView
 * JD-Core Version:    0.6.0
 */