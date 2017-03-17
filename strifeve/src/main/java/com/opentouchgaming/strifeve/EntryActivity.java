package com.opentouchgaming.strifeve;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.opentouchgaming.androidcore.AppSettings;
import com.opentouchgaming.androidcore.GD;
import com.opentouchgaming.androidcore.controls.ActionInput;
import com.opentouchgaming.androidcore.controls.ControlConfig;
import com.opentouchgaming.androidcore.controls.GamePadFragment;
import com.opentouchgaming.striveve.R;

import java.util.ArrayList;

public class EntryActivity extends FragmentActivity  {
       
	final static int LAUNCH_FRAG = 0;
                     
	LaunchFragment launchFrag;
	GamePadFragment gamePadFrag;
	/**       
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		GD.init(this);
		//Utils.expired();

		setContentView(R.layout.activity);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();

		AppSettings.setGame(GD.IDGame.Strife);
		AppSettings.reloadSettings(getApplication());



		GamePadFragment.gamepadActions = getGamepadAction();

		FragmentManager fm  = getFragmentManager();
		FragmentTransaction ft =  getFragmentManager().beginTransaction();

		launchFrag  = (LaunchFragment)fm.findFragmentByTag("play");
		gamePadFrag  = (GamePadFragment)fm.findFragmentByTag("gamepad");

		if (launchFrag == null)
		{
			//launchFrag = (LaunchFragment)Fragment.instantiate(this,"com.rws.postal.LaunchFragment");
			//gamePadFrag = (GamePadFragment)Fragment.instantiate(this,"com.rws.postal.GamePadFragment");
			launchFrag = new LaunchFragment();
			gamePadFrag = new GamePadFragment();

			ft.add(android.R.id.content, launchFrag, "play");	
			ft.add(android.R.id.content, gamePadFrag, "gamepad");	

		}
		else
		{
			//Utils.MyLog(1, LOG, "Fragments not recreated");
		}

		ft.hide(gamePadFrag);
		ft.show(launchFrag);
		ft.commit();

/*
		if (IntroDialog.showIntro(this))
		{
			IntroDialog.show(this,"Duke 3D",R.raw.intro);
		}
		else
		{
			if (AboutDialog.showAbout(this))
				AboutDialog.show(this);
		}
*/
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_quake, menu);
		return true;
	}


	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (gamePadFrag == null)
			gamePadFrag = (GamePadFragment)getFragmentManager().findFragmentByTag("gamepad");

		if (gamePadFrag.onGenericMotionEvent(event))
			return true; 
		
		
		return super.onGenericMotionEvent(event);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (gamePadFrag == null)
			gamePadFrag = (GamePadFragment)getFragmentManager().findFragmentByTag("gamepad");

		if (gamePadFrag.onKeyDown(keyCode, event))
			return true;
		else
		{
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				if (gamePadFrag.isVisible())
				{
					changeFragment(this, "play");
					return true;
				}
			}
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (gamePadFrag == null)
			gamePadFrag = (GamePadFragment)getFragmentManager().findFragmentByTag("gamepad");

		if ( gamePadFrag.onKeyUp(keyCode, event))
			return true;
		else
			return super.onKeyUp(keyCode, event);
	} 



	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private final FragmentActivity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(FragmentActivity activity, String tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state.  If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);

			if (mFragment == null) //Actually create all fragments NOW
			{
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				FragmentTransaction ft =  mActivity.getFragmentManager().beginTransaction();
				ft.add(android.R.id.content, mFragment, mTag);	
				ft.commit();
			}


			//if (mFragment != null && !mFragment.isDetached()) {
			if (mFragment != null && !mFragment.isHidden()) {
				FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				//ft.detach(mFragment);
				ft.hide(mFragment);
				ft.commit();
			}
		}  

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				//ft.attach(mFragment);
				//ft.setCustomAnimations(R., R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
				ft.show(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				//ft.detach(mFragment);
				ft.hide(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			//Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
	}


    public static void changeFragment(Activity activity, String name)
    {
        Fragment  launcher = activity.getFragmentManager().findFragmentByTag("play");
        Fragment  gamepad = activity.getFragmentManager().findFragmentByTag("gamepad");

        FragmentTransaction ft =  activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);

        if (name.contains("gamepad"))
        {
            ft.hide(launcher);
            ft.show(gamepad);
        }
        else if (name.contains("play"))
        {
            ft.hide(gamepad);
            ft.show(launcher);
        }

        ft.commit();
    }


	public static ArrayList<ActionInput> getGamepadAction()
	{
		ArrayList<ActionInput> actions = new ArrayList<ActionInput>();

		actions.add(new ActionInput("analog_move_fwd","Forward/Back", ControlConfig.ACTION_ANALOG_FWD, ControlConfig.Type.ANALOG,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_Y));
		actions.add(new ActionInput("analog_move_strafe","Strafe",ControlConfig.ACTION_ANALOG_STRAFE, ControlConfig.Type.ANALOG,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_X));
		actions.add(new ActionInput("analog_look_yaw","Look Left/Look Right",ControlConfig.ACTION_ANALOG_YAW, ControlConfig.Type.ANALOG,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_Z));
		actions.add(new ActionInput("analog_look_pitch","Look Up/Look Down",ControlConfig.ACTION_ANALOG_PITCH, ControlConfig.Type.ANALOG,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_RZ));

		actions.add(new ActionInput("attack","Attack",ControlConfig.PORT_ACT_ATTACK, ControlConfig.Type.BUTTON,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_RTRIGGER));

		actions.add(new ActionInput("use","Use/Open",ControlConfig.PORT_ACT_USE, ControlConfig.Type.BUTTON,
				ControlConfig.Type.BUTTON,KeyEvent.KEYCODE_BUTTON_A));

		actions.add(new ActionInput("next_weapon","Next Weapon",ControlConfig.PORT_ACT_NEXT_WEP, ControlConfig.Type.BUTTON,
				ControlConfig.Type.BUTTON,KeyEvent.KEYCODE_BUTTON_R1));
		actions.add(new ActionInput("prev_weapon","Previous Weapon",ControlConfig.PORT_ACT_PREV_WEP, ControlConfig.Type.BUTTON,
				ControlConfig.Type.BUTTON,KeyEvent.KEYCODE_BUTTON_L1));

		actions.add(new ActionInput("inv_use","Use Item",ControlConfig.PORT_ACT_INVUSE, ControlConfig.Type.BUTTON));
		actions.add(new ActionInput("inv_drop","Drop Item",ControlConfig.PORT_ACT_INVDROP, ControlConfig.Type.BUTTON));
		actions.add(new ActionInput("inv_next","Next Item",ControlConfig.PORT_ACT_INVNEXT, ControlConfig.Type.BUTTON));
		actions.add(new ActionInput("inv_prev","Prev Item",ControlConfig.PORT_ACT_INVPREV, ControlConfig.Type.BUTTON));
		actions.add(new ActionInput("show_weap","Show Stats/Weapons",ControlConfig.PORT_ACT_SHOW_WEAPONS, ControlConfig.Type.BUTTON));
		actions.add(new ActionInput("show_keys","Show Keys",ControlConfig.PORT_ACT_SHOW_KEYS, ControlConfig.Type.BUTTON));

		actions.add(new ActionInput("menu_up","Menu Up",ControlConfig.MENU_UP, ControlConfig.Type.MENU,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_HAT_Y,false));
		actions.add(new ActionInput("menu_down","Menu Down",ControlConfig.MENU_DOWN, ControlConfig.Type.MENU,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_HAT_Y));
		actions.add(new ActionInput("menu_left","Menu Left",ControlConfig.MENU_LEFT, ControlConfig.Type.MENU,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_HAT_X,false));
		actions.add(new ActionInput("menu_right","Menu Right",ControlConfig.MENU_RIGHT, ControlConfig.Type.MENU,
				ControlConfig.Type.ANALOG,MotionEvent.AXIS_HAT_X));
		actions.add(new ActionInput("menu_select","Menu Select",ControlConfig.MENU_SELECT, ControlConfig.Type.MENU,
				ControlConfig.Type.BUTTON,KeyEvent.KEYCODE_BUTTON_A));
		actions.add(new ActionInput("menu_back","Menu Back",ControlConfig.MENU_BACK, ControlConfig.Type.MENU,
				ControlConfig.Type.BUTTON,KeyEvent.KEYCODE_BUTTON_B));

		return actions;
	}
}
