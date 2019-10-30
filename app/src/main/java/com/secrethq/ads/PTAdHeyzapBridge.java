package com.secrethq.ads;

import java.lang.ref.WeakReference;

import org.cocos2dx.lib.Cocos2dxActivity;

import android.util.Log;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;

public class PTAdHeyzapBridge {
	private static native String bannerId();
	private static native String interstitialId();
	private static native void interstitialDidFail();
	private static native void bannerDidFail();
	private static native void rewardVideoComplete();
	
	private static final String TAG = "PTAdAppodealBridge";
	private static Cocos2dxActivity activity;
	private static WeakReference<Cocos2dxActivity> s_activity;

	private static boolean isBannerShown;

	public static void initBridge(Cocos2dxActivity activity){
		Log.v(TAG, "PTAdAppodealBridge -- INIT");
		PTAdHeyzapBridge.s_activity = new WeakReference<Cocos2dxActivity>(activity);
		PTAdHeyzapBridge.activity = activity;

		PTAdHeyzapBridge.initBanner();
		PTAdHeyzapBridge.initInterstitial();
    	PTAdHeyzapBridge.initVideo();
	}

	public static void initBanner(){
		Log.v(TAG, "PTAdAppodealBridge -- Init Banner");

		Appodeal.setBannerCallbacks(new BannerCallbacks() {
			@Override
			public void onBannerLoaded(int height, boolean isPrecache) {
				Log.d("Appodeal", "onBannerLoaded");
			}
			@Override
			public void onBannerFailedToLoad() {
				Log.d("Appodeal", "onBannerFailedToLoad");
			}
			@Override
			public void onBannerShown() {
				Log.d("Appodeal", "onBannerShown");
			}
			@Override
			public void onBannerClicked() {
				Log.d("Appodeal", "onBannerClicked");
			}
			@Override
			public void onBannerExpired() {
				Log.d("Appodeal", "onBannerExpired");
			}
		});
	}

	public static void initInterstitial(){
		Log.v(TAG, "PTAdHeyzapBridge -- Init Interstitial");

		Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
			@Override
			public void onInterstitialLoaded(boolean isPrecache) {
				Log.d("Appodeal", "onInterstitialLoaded");
			}
			@Override
			public void onInterstitialFailedToLoad() {
				Log.d("Appodeal", "onInterstitialFailedToLoad");
			}
			@Override
			public void onInterstitialShown() {
				Log.d("Appodeal", "onInterstitialShown");
			}
			@Override
			public void onInterstitialClicked() {
				Log.d("Appodeal", "onInterstitialClicked");
			}
			@Override
			public void onInterstitialClosed() {
				Log.d("Appodeal", "onInterstitialClosed");
			}
			@Override
			public void onInterstitialExpired() {
				Log.d("Appodeal", "onInterstitialExpired");
			}
		});
	}
	
	public static void initVideo() {

		Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
			@Override
			public void onRewardedVideoLoaded(boolean isPrecache) {
				Log.d("Appodeal", "onRewardedVideoLoaded");
			}
			@Override
			public void onRewardedVideoFailedToLoad() {
				Log.d("Appodeal", "onRewardedVideoFailedToLoad");
			}
			@Override
			public void onRewardedVideoShown() {
				Log.d("Appodeal", "onRewardedVideoShown");
			}
			@Override
			public void onRewardedVideoFinished(double amount, String name) {
				PTAdHeyzapBridge.s_activity.get().runOnUiThread(new Runnable() {
					public void run() {
						rewardVideoComplete();
					}
				});
				Log.v(TAG, "PTAdAppodealBridge -- rewardVideoComplete Complete ");
			}
			@Override
			public void onRewardedVideoClosed(boolean finished) {
				Log.d("Appodeal", "onRewardedVideoClosed");
			}
			@Override
			public void onRewardedVideoExpired() {
				Log.d("Appodeal", "onRewardedVideoExpired");
			}
		});
	}
	
	public static void showRewardedVideo(){
		Log.v(TAG, "PTAdAppodealBridge -- showRewardedVideo");

		PTAdHeyzapBridge.s_activity.get().runOnUiThread(new Runnable() {
			public void run() {
				if(Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
					Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
				}
			}
		});

	}
		
	public static void startSession( String sdkKey ){
		if(sdkKey != null){
			Log.v(TAG, "PTAdAppodealBridge -- Start Session: " + sdkKey);
			Appodeal.setFramework("buildbox", "2.1.7");
			Appodeal.initialize(activity, sdkKey, Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.REWARDED_VIDEO);

			Log.v(TAG, "Appodeal SDK Version : " + Appodeal.getVersion());
		}else{
			Log.v(TAG, "Start Session : null ");
		}
    }

	public static void showFullScreen(){
		Log.v(TAG, "PTAdAppodealBridge -- showFullScreen");
		PTAdHeyzapBridge.s_activity.get().runOnUiThread(new Runnable() {
			public void run() {
				Appodeal.show(activity, Appodeal.INTERSTITIAL);
			}
		});
	}

	public static void showBannerAd(){
		Log.v(TAG, "PTAdAppodealBridge -- showBannerAd");
		PTAdHeyzapBridge.s_activity.get().runOnUiThread(new Runnable() {
			public void run() {
				Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
			}
		});
	}

	public static void hideBannerAd(){
		Log.v(TAG, "PTAdAppodealBridge -- hideBannerAd");
		isBannerShown = false;
		Appodeal.hide(activity, Appodeal.BANNER);
	}

	public static boolean isBannerVisible(){
		return isBannerShown;
	}

	public static boolean isRewardedVideoAvialable(){
		return Appodeal.isLoaded(Appodeal.REWARDED_VIDEO);
	}
}
