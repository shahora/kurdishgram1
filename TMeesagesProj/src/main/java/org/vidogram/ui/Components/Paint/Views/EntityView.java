package org.vidogram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import java.util.UUID;
import org.vidogram.messenger.AndroidUtilities;
import org.vidogram.ui.Components.Point;
import org.vidogram.ui.Components.Rect;

public class EntityView extends FrameLayout
{
  private boolean announcedDragging = false;
  private boolean announcedSelection = false;
  private EntityViewDelegate delegate;
  private GestureDetector gestureDetector;
  private boolean hasPanned = false;
  private boolean hasReleased = false;
  private boolean hasTransformed = false;
  private int offsetX;
  private int offsetY;
  protected Point position = new Point();
  private float previousLocationX;
  private float previousLocationY;
  private boolean recognizedLongPress = false;
  protected SelectionView selectionView;
  private UUID uuid = UUID.randomUUID();

  public EntityView(Context paramContext, Point paramPoint)
  {
    super(paramContext);
    this.position = paramPoint;
    this.gestureDetector = new GestureDetector(paramContext, new GestureDetector.SimpleOnGestureListener()
    {
      public void onLongPress(MotionEvent paramMotionEvent)
      {
        if ((EntityView.this.hasPanned) || (EntityView.this.hasTransformed) || (EntityView.this.hasReleased));
        do
        {
          return;
          EntityView.access$302(EntityView.this, true);
        }
        while (EntityView.this.delegate == null);
        EntityView.this.performHapticFeedback(0);
        EntityView.this.delegate.onEntityLongClicked(EntityView.this);
      }
    });
  }

  private boolean onTouchMove(float paramFloat1, float paramFloat2)
  {
    float f1 = ((View)getParent()).getScaleX();
    Point localPoint = new Point((paramFloat1 - this.previousLocationX) / f1, (paramFloat2 - this.previousLocationY) / f1);
    float f2 = (float)Math.hypot(localPoint.x, localPoint.y);
    if (this.hasPanned)
      f1 = 6.0F;
    while (f2 > f1)
    {
      pan(localPoint);
      this.previousLocationX = paramFloat1;
      this.previousLocationY = paramFloat2;
      if (!this.announcedDragging)
      {
        this.announcedDragging = true;
        if (this.delegate != null)
          this.delegate.onBeganEntityDragging(this);
      }
      this.hasPanned = true;
      return true;
      f1 = 16.0F;
    }
    return false;
  }

  private void onTouchUp()
  {
    if ((!this.recognizedLongPress) && (!this.hasPanned) && (!this.hasTransformed) && (!this.announcedSelection) && (this.delegate != null))
      this.delegate.onEntitySelected(this);
    if ((this.announcedDragging) && (this.delegate != null))
      this.delegate.onFinishedEntityDragging(this);
    this.recognizedLongPress = false;
    this.hasPanned = false;
    this.hasTransformed = false;
    this.hasReleased = true;
    this.announcedSelection = false;
    this.announcedDragging = false;
  }

  protected SelectionView createSelectionView()
  {
    return null;
  }

  public void deselect()
  {
    if (this.selectionView == null)
      return;
    if (this.selectionView.getParent() != null)
      ((ViewGroup)this.selectionView.getParent()).removeView(this.selectionView);
    this.selectionView = null;
  }

  public Point getPosition()
  {
    return this.position;
  }

  public float getScale()
  {
    return getScaleX();
  }

  protected Rect getSelectionBounds()
  {
    return new Rect(0.0F, 0.0F, 0.0F, 0.0F);
  }

  public UUID getUUID()
  {
    return this.uuid;
  }

  public boolean isSelected()
  {
    return this.selectionView != null;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.delegate.allowInteraction(this);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = true;
    if ((paramMotionEvent.getPointerCount() > 1) || (!this.delegate.allowInteraction(this)))
      return false;
    float f1 = paramMotionEvent.getRawX();
    float f2 = paramMotionEvent.getRawY();
    switch (paramMotionEvent.getActionMasked())
    {
    case 4:
    default:
      bool = false;
    case 0:
    case 5:
    case 2:
    case 1:
    case 3:
    case 6:
    }
    while (true)
    {
      this.gestureDetector.onTouchEvent(paramMotionEvent);
      return bool;
      if ((!isSelected()) && (this.delegate != null))
      {
        this.delegate.onEntitySelected(this);
        this.announcedSelection = true;
      }
      this.previousLocationX = f1;
      this.previousLocationY = f2;
      this.hasReleased = false;
      continue;
      bool = onTouchMove(f1, f2);
      continue;
      onTouchUp();
    }
  }

  public void pan(Point paramPoint)
  {
    Point localPoint = this.position;
    localPoint.x += paramPoint.x;
    localPoint = this.position;
    localPoint.y += paramPoint.y;
    updatePosition();
  }

  public void rotate(float paramFloat)
  {
    setRotation(paramFloat);
    updateSelectionView();
  }

  public void scale(float paramFloat)
  {
    setScale(Math.max(getScale() * paramFloat, 0.1F));
    updateSelectionView();
  }

  public void select(ViewGroup paramViewGroup)
  {
    SelectionView localSelectionView = createSelectionView();
    this.selectionView = localSelectionView;
    paramViewGroup.addView(localSelectionView);
    localSelectionView.updatePosition();
  }

  public void setDelegate(EntityViewDelegate paramEntityViewDelegate)
  {
    this.delegate = paramEntityViewDelegate;
  }

  public void setOffset(int paramInt1, int paramInt2)
  {
    this.offsetX = paramInt1;
    this.offsetY = paramInt2;
  }

  public void setPosition(Point paramPoint)
  {
    this.position = paramPoint;
    updatePosition();
  }

  public void setScale(float paramFloat)
  {
    setScaleX(paramFloat);
    setScaleY(paramFloat);
  }

  public void setSelectionVisibility(boolean paramBoolean)
  {
    if (this.selectionView == null)
      return;
    SelectionView localSelectionView = this.selectionView;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localSelectionView.setVisibility(i);
      return;
    }
  }

  protected void updatePosition()
  {
    float f1 = getWidth() / 2.0F;
    float f2 = getHeight() / 2.0F;
    setX(this.position.x - f1);
    setY(this.position.y - f2);
    updateSelectionView();
  }

  public void updateSelectionView()
  {
    if (this.selectionView != null)
      this.selectionView.updatePosition();
  }

  public static abstract interface EntityViewDelegate
  {
    public abstract boolean allowInteraction(EntityView paramEntityView);

    public abstract void onBeganEntityDragging(EntityView paramEntityView);

    public abstract boolean onEntityLongClicked(EntityView paramEntityView);

    public abstract boolean onEntitySelected(EntityView paramEntityView);

    public abstract void onFinishedEntityDragging(EntityView paramEntityView);
  }

  public class SelectionView extends FrameLayout
  {
    public static final int SELECTION_LEFT_HANDLE = 1;
    public static final int SELECTION_RIGHT_HANDLE = 2;
    public static final int SELECTION_WHOLE_HANDLE = 3;
    private int currentHandle;
    protected Paint dotPaint = new Paint(1);
    protected Paint dotStrokePaint = new Paint(1);
    protected Paint paint = new Paint(1);

    public SelectionView(Context arg2)
    {
      super();
      setWillNotDraw(false);
      this.paint.setColor(-1);
      this.dotPaint.setColor(-12793105);
      this.dotStrokePaint.setColor(-1);
      this.dotStrokePaint.setStyle(Paint.Style.STROKE);
      this.dotStrokePaint.setStrokeWidth(AndroidUtilities.dp(1.0F));
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      boolean bool2 = false;
      boolean bool1 = bool2;
      switch (paramMotionEvent.getActionMasked())
      {
      default:
        bool1 = bool2;
      case 4:
      case 0:
      case 5:
      case 2:
      case 1:
      case 3:
      case 6:
      }
      while (true)
      {
        if (this.currentHandle == 3)
          EntityView.this.gestureDetector.onTouchEvent(paramMotionEvent);
        return bool1;
        int i = pointInsideHandle(paramMotionEvent.getX(), paramMotionEvent.getY());
        bool1 = bool2;
        if (i == 0)
          continue;
        this.currentHandle = i;
        EntityView.access$702(EntityView.this, paramMotionEvent.getRawX());
        EntityView.access$802(EntityView.this, paramMotionEvent.getRawY());
        EntityView.access$202(EntityView.this, false);
        bool1 = true;
        continue;
        if (this.currentHandle == 3)
        {
          f1 = paramMotionEvent.getRawX();
          f2 = paramMotionEvent.getRawY();
          bool1 = EntityView.this.onTouchMove(f1, f2);
          continue;
        }
        bool1 = bool2;
        if (this.currentHandle == 0)
          continue;
        EntityView.access$102(EntityView.this, true);
        Point localPoint = new Point(paramMotionEvent.getRawX() - EntityView.this.previousLocationX, paramMotionEvent.getRawY() - EntityView.this.previousLocationY);
        float f1 = (float)Math.toRadians(getRotation());
        double d1 = localPoint.x;
        double d2 = Math.cos(f1);
        double d3 = localPoint.y;
        float f2 = (float)(Math.sin(f1) * d3 + d1 * d2);
        f1 = f2;
        if (this.currentHandle == 1)
          f1 = f2 * -1.0F;
        f1 = f1 * 2.0F / getWidth();
        EntityView.this.scale(f1 + 1.0F);
        f2 = getLeft() + getWidth() / 2;
        float f3 = getTop() + getHeight() / 2;
        float f4 = paramMotionEvent.getRawX() - ((View)getParent()).getLeft();
        float f5 = paramMotionEvent.getRawY() - ((View)getParent()).getTop() - AndroidUtilities.statusBarHeight;
        f1 = 0.0F;
        if (this.currentHandle == 1)
          f1 = (float)Math.atan2(f3 - f5, f2 - f4);
        while (true)
        {
          EntityView.this.rotate((float)Math.toDegrees(f1));
          EntityView.access$702(EntityView.this, paramMotionEvent.getRawX());
          EntityView.access$802(EntityView.this, paramMotionEvent.getRawY());
          if (!EntityView.this.announcedDragging)
          {
            EntityView.access$1002(EntityView.this, true);
            if (EntityView.this.delegate != null)
              EntityView.this.delegate.onBeganEntityDragging(EntityView.this);
          }
          bool1 = true;
          break;
          if (this.currentHandle != 2)
            continue;
          f1 = (float)Math.atan2(f5 - f3, f4 - f2);
        }
        EntityView.this.onTouchUp();
        this.currentHandle = 0;
        bool1 = true;
      }
    }

    protected int pointInsideHandle(float paramFloat1, float paramFloat2)
    {
      return 0;
    }

    protected void updatePosition()
    {
      Rect localRect = EntityView.this.getSelectionBounds();
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)getLayoutParams();
      localLayoutParams.leftMargin = ((int)localRect.x + EntityView.this.offsetX);
      localLayoutParams.topMargin = ((int)localRect.y + EntityView.this.offsetY);
      localLayoutParams.width = (int)localRect.width;
      localLayoutParams.height = (int)localRect.height;
      setLayoutParams(localLayoutParams);
      setRotation(EntityView.this.getRotation());
    }
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.ui.Components.Paint.Views.EntityView
 * JD-Core Version:    0.6.0
 */