package cn.Arthur.ZZ.KillTheBird;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;
import cn.Arthur.ZZ.KillTheBird.extend.AppDetail;
import cn.Arthur.ZZ.KillTheBird.extend.AppWall;
import cn.Arthur.ZZ.KillTheBird.extend.QuitPopAd;
import cn.Arthur.ZZ.KillTheBird.extend.SlideWall;

public class DemoApp extends Activity implements View.OnClickListener, UpdatePointsNotifier {

	private TextView pointsTextView;
	private TextView SDKVersionView;

	private String displayPointsText;

	final Handler mHandler = new Handler();

	private View slidingDrawerView;
	
	Handler ADHandler=new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		AppConnect.getInstance("76a446cb7ed9eec03ff1d43f37e9fbc5", "waps", this);

		Button offersButton = (Button) findViewById(R.id.OffersButton);
		Button gameOffersButton = (Button) findViewById(R.id.gameOffersButton);
		Button appOffersButton = (Button) findViewById(R.id.appOffersButton);
		Button moreAppsButton = (Button) findViewById(R.id.moreAppsButton);
		Button spendButton = (Button) findViewById(R.id.spendButton);
		Button feedbackButton = (Button) findViewById(R.id.feedbackButton);
		Button awardButton = (Button) findViewById(R.id.awardButton);
		Button diyAdButton = (Button) findViewById(R.id.diyAdButton);
		Button diyAdListButton = (Button) findViewById(R.id.diyAdListButton);
		Button popAdButton = (Button) findViewById(R.id.popAdButton);
		Button ownAppDetailButton = (Button) findViewById(R.id.ownAppDetailButton);
		Button funAdButton = (Button) findViewById(R.id.funAdButton);
		
		offersButton.setOnClickListener(this);
		gameOffersButton.setOnClickListener(this);
		appOffersButton.setOnClickListener(this);
		moreAppsButton.setOnClickListener(this);
		spendButton.setOnClickListener(this);
		feedbackButton.setOnClickListener(this);
		awardButton.setOnClickListener(this);
		diyAdButton.setOnClickListener(this);
		diyAdListButton.setOnClickListener(this);
		popAdButton.setOnClickListener(this);
		ownAppDetailButton.setOnClickListener(this);
		funAdButton.setOnClickListener(this);
		pointsTextView = (TextView) findViewById(R.id.PointsTextView);
		SDKVersionView = (TextView) findViewById(R.id.SDKVersionView);
		AppConnect.getInstance(this).initAdInfo();
		AppConnect.getInstance(this).initPopAd(this);
    	AppConnect.getInstance(this).initFunAd(this);
		String showAd = AppConnect.getInstance(this).getConfig("showAd", "defaultValue");
		
		SDKVersionView.setText("在线配置:showAd = "+showAd);
		
		SDKVersionView.setText(SDKVersionView.getText()+"\nSDK版本: " + AppConnect.LIBRARY_VERSION_NUMBER);
		
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.AdLinearLayout);
		AppConnect.getInstance(this).showBannerAd(this, layout);
		
		LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
		AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10);// 10绉掑埛鏂颁竴娆�
		
    	slidingDrawerView = SlideWall.getInstance().getView(this);
    	if(slidingDrawerView != null){
    		this.addContentView(slidingDrawerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	}
    	
    	//AppConnect.getInstance(this).initUninstallAd(this);
    	
    	ADHandler.postDelayed(ShowPop, 5*60*100);
    	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(SlideWall.getInstance().slideWallDrawer != null
					&& SlideWall.getInstance().slideWallDrawer.isOpened()){				
				SlideWall.getInstance().closeSlidingDrawer();
			}else{
				QuitPopAd.getInstance().show(this);
			}
			
		}
		return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		QuitPopAd.getInstance().close();
		if(slidingDrawerView != null){
			((ViewGroup)slidingDrawerView.getParent()).removeView(slidingDrawerView);
			slidingDrawerView = null;
			slidingDrawerView = SlideWall.getInstance().getView(this);
			if(slidingDrawerView != null){
				this.addContentView(slidingDrawerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
		}
		super.onConfigurationChanged(newConfig);
	}

	public void onClick(View v) {
		if (v instanceof Button) {
			int id = ((Button) v).getId();

			switch (id) {
			case R.id.OffersButton:
				AppConnect.getInstance(this).showOffers(this);
				break;
			case R.id.popAdButton:
				AppConnect.getInstance(this).showPopAd(this);
				break;
			case R.id.appOffersButton:
				AppConnect.getInstance(this).showAppOffers(this);
				break;
			case R.id.gameOffersButton:
				AppConnect.getInstance(this).showGameOffers(this);
				break;
			case R.id.diyAdListButton:
				Intent appWallIntent = new Intent(this, AppWall.class);
				this.startActivity(appWallIntent);
				break;
			case R.id.diyAdButton:
				AdInfo adInfo = AppConnect.getInstance(DemoApp.this).getAdInfo();
				AppDetail.getInstanct().showAdDetail(DemoApp.this,adInfo);
				break;
			case R.id.spendButton:
				AppConnect.getInstance(this).spendPoints(10, this);
				break;
			case R.id.awardButton:
				AppConnect.getInstance(this).awardPoints(10, this);
				break;
			case R.id.moreAppsButton:
				AppConnect.getInstance(this).showMore(this);
				break;
			case R.id.ownAppDetailButton:
				AppConnect.getInstance(this).showMore(this, "76a446cb7ed9eec03ff1d43f37e9fbc5");
				break;
			case R.id.funAdButton:
				//璋冪敤鍔熻兘骞垮憡鎺ュ彛锛堜娇鐢ㄦ祻瑙堝櫒鎺ュ彛锛�
				String uriStr = "http://www.baidu.com";
				AppConnect.getInstance(this).showBrowser(this, uriStr);
				break;
			case R.id.feedbackButton:
				//鐢ㄦ埛鍙嶉
				AppConnect.getInstance(this).showFeedback(this);
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		AppConnect.getInstance(this).getPoints(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).close();
		super.onDestroy();
	}


	/**
	 * 鐢ㄤ簬鐩戝惉鎻掑睆骞垮憡鐨勬樉绀轰笌鍏抽棴
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Dialog dialog = AppConnect.getInstance(this).getPopAdDialog();
		if(dialog != null){
			if(dialog.isShowing()){
			}
			dialog.setOnCancelListener(new OnCancelListener(){
				public void onCancel(DialogInterface dialog) {
				}
			});
		}
	}

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (pointsTextView != null) {
				pointsTextView.setText(displayPointsText);
			}
		}
	};
	
	Runnable ShowPop = new Runnable() {
		public void run() {
			AppConnect.getInstance(DemoApp.this).showPopAd(DemoApp.this);
			ADHandler.postDelayed(ShowPop, 5*60*1000);
		}
	};

	/**
	 * AppConnect.getPoints()鏂规硶鐨勫疄鐜帮紝蹇呴』瀹炵幇	  
	 * @param currencyName	铏氭嫙璐у竵鍚嶇О.
	 * @param pointTotal	铏氭嫙璐у竵浣欓.
	 */
	public void getUpdatePoints(String currencyName, int pointTotal) {
		displayPointsText = currencyName + ": " + pointTotal;
		mHandler.post(mUpdateResults);
	}

	/**
	 * AppConnect.getPoints() 鏂规硶鐨勫疄鐜帮紝蹇呴』瀹炵幇
	 * @param error	璇锋眰澶辫触鐨勯敊璇俊鎭�
	 */
	public void getUpdatePointsFailed(String error) {
		displayPointsText = error;
		mHandler.post(mUpdateResults);
	}
	
}