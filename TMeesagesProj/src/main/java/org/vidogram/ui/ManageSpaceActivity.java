package org.vidogram.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;
import org.vidogram.messenger.AndroidUtilities;
import org.vidogram.messenger.ApplicationLoader;
import org.vidogram.messenger.LocaleController;
import org.vidogram.messenger.NotificationCenter;
import org.vidogram.tgnet.ConnectionsManager;
import org.vidogram.ui.ActionBar.ActionBarLayout;
import org.vidogram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.vidogram.ui.ActionBar.BaseFragment;
import org.vidogram.ui.ActionBar.DrawerLayoutContainer;
import org.vidogram.ui.Components.LayoutHelper;

public class ManageSpaceActivity extends Activity
  implements ActionBarLayout.ActionBarLayoutDelegate
{
  private static ArrayList<BaseFragment> layerFragmentsStack;
  private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
  private ActionBarLayout actionBarLayout;
  private int currentConnectionState;
  protected DrawerLayoutContainer drawerLayoutContainer;
  private boolean finished;
  private ActionBarLayout layersActionBarLayout;

  static
  {
    layerFragmentsStack = new ArrayList();
  }

  private boolean handleIntent(Intent paramIntent, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (AndroidUtilities.isTablet())
      if (this.layersActionBarLayout.fragmentsStack.isEmpty())
        this.layersActionBarLayout.addFragmentToStack(new CacheControlActivity());
    while (true)
    {
      this.actionBarLayout.showLastFragment();
      if (AndroidUtilities.isTablet())
        this.layersActionBarLayout.showLastFragment();
      paramIntent.setAction(null);
      return false;
      if (!this.actionBarLayout.fragmentsStack.isEmpty())
        continue;
      this.actionBarLayout.addFragmentToStack(new CacheControlActivity());
    }
  }

  private void onFinish()
  {
    if (this.finished)
      return;
    this.finished = true;
  }

  private void updateCurrentConnectionState()
  {
    String str = null;
    if (this.currentConnectionState == 2)
      str = LocaleController.getString("WaitingForNetwork", 2131166609);
    while (true)
    {
      this.actionBarLayout.setTitleOverlayText(str);
      return;
      if (this.currentConnectionState == 1)
      {
        str = LocaleController.getString("Connecting", 2131165571);
        continue;
      }
      if (this.currentConnectionState != 4)
        continue;
      str = LocaleController.getString("Updating", 2131166542);
    }
  }

  public void fixLayout()
  {
    if (!AndroidUtilities.isTablet());
    do
      return;
    while (this.actionBarLayout == null);
    this.actionBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        ManageSpaceActivity.this.needLayout();
        if (ManageSpaceActivity.this.actionBarLayout != null)
        {
          if (Build.VERSION.SDK_INT < 16)
            ManageSpaceActivity.this.actionBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        else
          return;
        ManageSpaceActivity.this.actionBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
      }
    });
  }

  public boolean needAddFragmentToStack(BaseFragment paramBaseFragment, ActionBarLayout paramActionBarLayout)
  {
    return true;
  }

  public boolean needCloseLastFragment(ActionBarLayout paramActionBarLayout)
  {
    if (AndroidUtilities.isTablet())
    {
      if ((paramActionBarLayout == this.actionBarLayout) && (paramActionBarLayout.fragmentsStack.size() <= 1))
      {
        onFinish();
        finish();
        return false;
      }
      if ((paramActionBarLayout == this.layersActionBarLayout) && (this.actionBarLayout.fragmentsStack.isEmpty()) && (this.layersActionBarLayout.fragmentsStack.size() == 1))
      {
        onFinish();
        finish();
        return false;
      }
    }
    else if (paramActionBarLayout.fragmentsStack.size() <= 1)
    {
      onFinish();
      finish();
      return false;
    }
    return true;
  }

  public void needLayout()
  {
    RelativeLayout.LayoutParams localLayoutParams;
    int i;
    if (AndroidUtilities.isTablet())
    {
      localLayoutParams = (RelativeLayout.LayoutParams)this.layersActionBarLayout.getLayoutParams();
      localLayoutParams.leftMargin = ((AndroidUtilities.displaySize.x - localLayoutParams.width) / 2);
      if (Build.VERSION.SDK_INT < 21)
        break label209;
      i = AndroidUtilities.statusBarHeight;
      localLayoutParams.topMargin = (i + (AndroidUtilities.displaySize.y - localLayoutParams.height - i) / 2);
      this.layersActionBarLayout.setLayoutParams(localLayoutParams);
      if ((AndroidUtilities.isSmallTablet()) && (getResources().getConfiguration().orientation != 2))
        break label214;
      i = AndroidUtilities.displaySize.x / 100 * 35;
      if (i >= AndroidUtilities.dp(320.0F))
        break label244;
      i = AndroidUtilities.dp(320.0F);
    }
    label209: label214: label244: 
    while (true)
    {
      localLayoutParams = (RelativeLayout.LayoutParams)this.actionBarLayout.getLayoutParams();
      localLayoutParams.width = i;
      localLayoutParams.height = -1;
      this.actionBarLayout.setLayoutParams(localLayoutParams);
      if ((AndroidUtilities.isSmallTablet()) && (this.actionBarLayout.fragmentsStack.size() == 2))
      {
        ((BaseFragment)this.actionBarLayout.fragmentsStack.get(1)).onPause();
        this.actionBarLayout.fragmentsStack.remove(1);
        this.actionBarLayout.showLastFragment();
      }
      return;
      i = 0;
      break;
      localLayoutParams = (RelativeLayout.LayoutParams)this.actionBarLayout.getLayoutParams();
      localLayoutParams.width = -1;
      localLayoutParams.height = -1;
      this.actionBarLayout.setLayoutParams(localLayoutParams);
      return;
    }
  }

  public boolean needPresentFragment(BaseFragment paramBaseFragment, boolean paramBoolean1, boolean paramBoolean2, ActionBarLayout paramActionBarLayout)
  {
    return true;
  }

  public void onBackPressed()
  {
    if (PhotoViewer.getInstance().isVisible())
    {
      PhotoViewer.getInstance().closePhoto(true, false);
      return;
    }
    if (this.drawerLayoutContainer.isDrawerOpened())
    {
      this.drawerLayoutContainer.closeDrawer(false);
      return;
    }
    if (AndroidUtilities.isTablet())
    {
      if (this.layersActionBarLayout.getVisibility() == 0)
      {
        this.layersActionBarLayout.onBackPressed();
        return;
      }
      this.actionBarLayout.onBackPressed();
      return;
    }
    this.actionBarLayout.onBackPressed();
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    AndroidUtilities.checkDisplaySize(this, paramConfiguration);
    super.onConfigurationChanged(paramConfiguration);
    fixLayout();
  }

  protected void onCreate(Bundle paramBundle)
  {
    ApplicationLoader.postInitApplication();
    requestWindowFeature(1);
    setTheme(2131361942);
    getWindow().setBackgroundDrawableResource(2130838109);
    super.onCreate(paramBundle);
    int i = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (i > 0)
      AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(i);
    this.actionBarLayout = new ActionBarLayout(this);
    this.drawerLayoutContainer = new DrawerLayoutContainer(this);
    this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    setContentView(this.drawerLayoutContainer, new ViewGroup.LayoutParams(-1, -1));
    Object localObject1;
    if (AndroidUtilities.isTablet())
    {
      getWindow().setSoftInputMode(16);
      localObject1 = new RelativeLayout(this);
      this.drawerLayoutContainer.addView((View)localObject1);
      Object localObject2 = (FrameLayout.LayoutParams)((RelativeLayout)localObject1).getLayoutParams();
      ((FrameLayout.LayoutParams)localObject2).width = -1;
      ((FrameLayout.LayoutParams)localObject2).height = -1;
      ((RelativeLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      localObject2 = new View(this);
      BitmapDrawable localBitmapDrawable = (BitmapDrawable)getResources().getDrawable(2130837662);
      localBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
      ((View)localObject2).setBackgroundDrawable(localBitmapDrawable);
      ((RelativeLayout)localObject1).addView((View)localObject2, LayoutHelper.createRelative(-1, -1));
      ((RelativeLayout)localObject1).addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
      localObject2 = new FrameLayout(this);
      ((FrameLayout)localObject2).setBackgroundColor(2130706432);
      ((RelativeLayout)localObject1).addView((View)localObject2, LayoutHelper.createRelative(-1, -1));
      ((FrameLayout)localObject2).setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          if ((!ManageSpaceActivity.this.actionBarLayout.fragmentsStack.isEmpty()) && (paramMotionEvent.getAction() == 1))
          {
            float f1 = paramMotionEvent.getX();
            float f2 = paramMotionEvent.getY();
            paramView = new int[2];
            ManageSpaceActivity.this.layersActionBarLayout.getLocationOnScreen(paramView);
            int i = paramView[0];
            int j = paramView[1];
            if ((ManageSpaceActivity.this.layersActionBarLayout.checkTransitionAnimation()) || ((f1 > i) && (f1 < i + ManageSpaceActivity.this.layersActionBarLayout.getWidth()) && (f2 > j) && (f2 < ManageSpaceActivity.this.layersActionBarLayout.getHeight() + j)))
              return false;
            if (!ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.isEmpty())
            {
              while (ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.size() - 1 > 0)
                ManageSpaceActivity.this.layersActionBarLayout.removeFragmentFromStack((BaseFragment)ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.get(0));
              ManageSpaceActivity.this.layersActionBarLayout.closeLastFragment(true);
            }
            return true;
          }
          return false;
        }
      });
      ((FrameLayout)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
        }
      });
      this.layersActionBarLayout = new ActionBarLayout(this);
      this.layersActionBarLayout.setRemoveActionBarExtraHeight(true);
      this.layersActionBarLayout.setBackgroundView((View)localObject2);
      this.layersActionBarLayout.setUseAlphaAnimations(true);
      this.layersActionBarLayout.setBackgroundResource(2130837651);
      ((RelativeLayout)localObject1).addView(this.layersActionBarLayout, LayoutHelper.createRelative(530, 528));
      this.layersActionBarLayout.init(layerFragmentsStack);
      this.layersActionBarLayout.setDelegate(this);
      this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
      this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
      this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
      this.actionBarLayout.init(mainFragmentsStack);
      this.actionBarLayout.setDelegate(this);
      NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, new Object[] { this });
      this.currentConnectionState = ConnectionsManager.getInstance().getConnectionState();
      localObject1 = getIntent();
      if (paramBundle == null)
        break label512;
    }
    label512: for (boolean bool = true; ; bool = false)
    {
      handleIntent((Intent)localObject1, false, bool, false);
      needLayout();
      return;
      this.drawerLayoutContainer.addView(this.actionBarLayout, new ViewGroup.LayoutParams(-1, -1));
      break;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    onFinish();
  }

  public void onLowMemory()
  {
    super.onLowMemory();
    this.actionBarLayout.onLowMemory();
    if (AndroidUtilities.isTablet())
      this.layersActionBarLayout.onLowMemory();
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    handleIntent(paramIntent, true, false, false);
  }

  protected void onPause()
  {
    super.onPause();
    this.actionBarLayout.onPause();
    if (AndroidUtilities.isTablet())
      this.layersActionBarLayout.onPause();
  }

  public boolean onPreIme()
  {
    return false;
  }

  public void onRebuildAllFragments(ActionBarLayout paramActionBarLayout)
  {
    if ((AndroidUtilities.isTablet()) && (paramActionBarLayout == this.layersActionBarLayout))
    {
      this.actionBarLayout.rebuildAllFragmentViews(true);
      this.actionBarLayout.showLastFragment();
    }
  }

  protected void onResume()
  {
    super.onResume();
    this.actionBarLayout.onResume();
    if (AndroidUtilities.isTablet())
      this.layersActionBarLayout.onResume();
  }

  public void presentFragment(BaseFragment paramBaseFragment)
  {
    this.actionBarLayout.presentFragment(paramBaseFragment);
  }

  public boolean presentFragment(BaseFragment paramBaseFragment, boolean paramBoolean1, boolean paramBoolean2)
  {
    return this.actionBarLayout.presentFragment(paramBaseFragment, paramBoolean1, paramBoolean2, true);
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.ui.ManageSpaceActivity
 * JD-Core Version:    0.6.0
 */