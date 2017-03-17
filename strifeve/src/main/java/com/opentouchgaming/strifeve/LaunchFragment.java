package com.opentouchgaming.strifeve;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.opentouchgaming.androidcore.GD;
import com.opentouchgaming.androidcore.Utils;
import com.opentouchgaming.striveve.R;

public class LaunchFragment extends Fragment{                           

	String LOG = "LaunchFragment";          
	private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk41oIsqy7b3FAD1UNc7XvpGd8ognsulOL8CWf6HGj0TRJQsHftGuZM9dMw9d3j5FGJrSRIcuiGE4hP6BZr2HxcuBK7wsPmZr+7HGta+A3J4iuCZv44Zz6JXszhwPKZivlqLyJxqco1Tq1XA/kZuZB8DU1fF5CrSHPFQui1UvHjwvhTyzSiFGiWJw3OEQLf/EDiUv6gMKyXAet8vHBQZJrhFQB0LPSI5wjuWexhW7d+B9zb9U6SOLT8sHaPuLHnnJ3JvGBRx7xeKXTDZpSHP+dzBTy401oyvXVeT6jRTmHZgmwsVz8bLD4H2cujawNglDHtss2A8IQIe/UmZtHUTheQIDAQAB";

	String fullBaseDir;               

 
	@Override              
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        fullBaseDir = getActivity().getFilesDir().toString();
		//fullBaseDir = "/sdcard/strifeve/";
	}                                          

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (GD.DEBUG) Log.d(LOG,"onHiddenChanged");
		super.onHiddenChanged(hidden);      
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_launch, null);


		Button gamepadButton = (Button)mainView.findViewById(R.id.gamepad_button);
		gamepadButton.setOnClickListener(new OnClickListener() { 

			@Override
			public void onClick(View v) {
				EntryActivity.changeFragment(getActivity(), "gamepad");
			}
		}); 

		Button startfull = (Button)mainView.findViewById(R.id.start_full_button);
		startfull.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                String missingFiles;
               	if((missingFiles = Utils.checkFiles(fullBaseDir , new String[] {"strife-music.cfg"})) != null)
				{
					Utils.ExtractAsset(getActivity(), "strife-music.cfg", fullBaseDir, 505 * 1024 * 1024);
				}
				/*
				else if((missingFiles = Utils.checkFiles(fullBaseDir , new String[] {"NightDive.ogv"})) != null)
				{
					Utils.ExtractAsset(getActivity(), "NightDive.ogv", fullBaseDir, 505 * 1024 * 1024);
					Utils.ExtractAsset(getActivity(), "Strife.ogv", fullBaseDir, 505 * 1024 * 1024);
				}
                else
                {
                    startGame(fullBaseDir,false);
                }
                */
				startGame(fullBaseDir,false);
			}
		});           

		Button yt = (Button)mainView.findViewById(R.id.youtube_button);
		yt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/nightdivestudios"));
				startActivity(browserIntent);
			} 
		});

		Button fb = (Button)mainView.findViewById(R.id.facebook_button);
		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/NightDiveStudios"));
				startActivity(browserIntent);
			}
		});

		Button twit = (Button)mainView.findViewById(R.id.twitter_button);
		twit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/NightDiveStudio"));
				startActivity(browserIntent);
			}
		});
		return mainView;
	}

	void startGame(final String base,boolean ignoreMusic)  
	{
        String args = "";

        //Intent intent = new Intent(getActivity(), Game.class);
		Intent intent = new Intent(getActivity(), org.libsdl.app.SDLActivity.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.putExtra("game_path",base);

        intent.putExtra("args",args);
        startActivity(intent);
    }


}
