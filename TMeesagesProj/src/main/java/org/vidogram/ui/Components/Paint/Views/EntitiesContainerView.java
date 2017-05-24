package org.vidogram.ui.Components.Paint.Views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.FrameLayout;

public class EntitiesContainerView extends FrameLayout
  implements ScaleGestureDetector.OnScaleGestureListener, RotationGestureDetector.OnRotationGestureListener
{
  private EntitiesContainerViewDelegate delegate;
  private ScaleGestureDetector gestureDetector = new ScaleGestureDetector(paramContext, this);
  private boolean hasTransformed;
  private float previousAngle;
  private float previousScale = 1.0F;
  private RotationGestureDetector rotationGestureDetector = new RotationGestureDetector(this);

  public EntitiesContainerView(Context paramContext, EntitiesContainerViewDelegate paramEntitiesContainerViewDelegate)
  {
    super(paramContext);
    this.delegate = paramEntitiesContainerViewDelegate;
  }

  public void bringViewToFront(EntityView paramEntityView)
  {
    if (indexOfChild(paramEntityView) != getChildCount() - 1)
    {
      removeView(paramEntityView);
      addView(paramEntityView, getChildCount());
    }
  }

  public int entitiesCount()
  {
    int i = 0;
    int j = 0;
    if (i < getChildCount())
    {
      if (!(getChildAt(i) instanceof EntityView));
      while (true)
      {
        i += 1;
        break;
        j += 1;
      }
    }
    return j;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return (paramMotionEvent.getPointerCount() == 2) && (this.delegate.shouldReceiveTouches());
  }

  public void onRotation(RotationGestureDetector paramRotationGestureDetector)
  {
    EntityView localEntityView = this.delegate.onSelectedEntityRequest();
    float f = paramRotationGestureDetector.getAngle();
    localEntityView.rotate(this.previousAngle - f + localEntityView.getRotation());
    this.previousAngle = f;
  }

  public void onRotationBegin(RotationGestureDetector paramRotationGestureDetector)
  {
    this.previousAngle = paramRotationGestureDetector.getStartAngle();
    this.hasTransformed = true;
  }

  public void onRotationEnd(RotationGestureDetector paramRotationGestureDetector)
  {
  }

  public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
  {
    float f1 = paramScaleGestureDetector.getScaleFactor();
    float f2 = f1 / this.previousScale;
    this.delegate.onSelectedEntityRequest().scale(f2);
    this.previousScale = f1;
    return false;
  }

  public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
  {
    this.previousScale = 1.0F;
    this.hasTransformed = true;
    return true;
  }

  public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector)
  {
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.delegate.onSelectedEntityRequest() == null);
    label56: 
    do
    {
      return false;
      int i;
      if (paramMotionEvent.getPointerCount() == 1)
      {
        i = paramMotionEvent.getActionMasked();
        if (i != 0)
          break label56;
        this.hasTransformed = false;
      }
      do
      {
        this.gestureDetector.onTouchEvent(paramMotionEvent);
        this.rotationGestureDetector.onTouchEvent(paramMotionEvent);
        return true;
      }
      while ((i != 1) && (i != 2));
    }
    while ((this.hasTransformed) || (this.delegate == null));
    this.delegate.onEntityDeselect();
    return false;
  }

  public static abstract interface EntitiesContainerViewDelegate
  {
    public abstract void onEntityDeselect();

    public abstract EntityView onSelectedEntityRequest();

    public abstract boolean shouldReceiveTouches();
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.ui.Components.Paint.Views.EntitiesContainerView
 * JD-Core Version:    0.6.0
 */