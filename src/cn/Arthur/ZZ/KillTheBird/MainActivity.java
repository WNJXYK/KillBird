package cn.Arthur.ZZ.KillTheBird;

import java.util.HashMap;

import cn.waps.AppConnect;

import com.bpkg.m.MyMediaManager;
import com.epkg.p.MyManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private SharedPreferences sp;
    private String game1="G1",game2="G2";
    public SafeInt games1,games2;
    private long clickTime = 0; //记录第一次点击的时间 
    private SoundPool soundp;
    private HashMap<String,Integer> soundm;
    private LinearLayout adlayout;
    private boolean isShowAD=false;
    private GameView gm;
    private final String APPID="ec8065f1d8f94a70893fe8b18502c009";
    private final String APPCHANNEL="k-gfan";
    private int ADTimes=1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //隐去标题栏（应用程序的名字）  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐去状态栏部分(电池等图标和一切修饰部分)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp = this.getSharedPreferences("SaveBirdGameData", this.MODE_PRIVATE);
        LoadData();
        initMusic();
        gm=new GameView(this);
        setContentView(gm);
        initAD();
   }
    
    /**
     * 初始化广告
     */
    public void initAD(){
    	ADTimes=1;
    	MyManager pm = MyManager.getInstance(MainActivity.this);
    	//AppConnect.getInstance("76a446cb7ed9eec03ff1d43f37e9fbc5","0000",this);
		pm.setCooId(MainActivity.this, APPID);//
		pm.setChannelId(MainActivity.this, APPCHANNEL);
		pm.receiveMessage(MainActivity.this, false);
    }
    
    /**
     * 广告控制
     */
    public void showAD(){
    	MyMediaManager.startAd(MainActivity.this,MyMediaManager.CENTER_CENTER,APPID, APPCHANNEL,true);
    }
    public void removeAD(){
    	isShowAD=false;
    }
    
    
    /**
     * 读取数据
     */
    public void LoadData(){
    	games1=new SafeInt(sp.getInt(game1, 0));
    	games2=new SafeInt(sp.getInt(game2, 0));
    }
    
    /**
     * 保存数据
     */
    public void SaveData(){
    	sp.edit().putInt(game1, games1.get()).commit();
		sp.edit().putInt(game2,games2.get()).commit();
    }
    
    /**
     * 刷新最高分
     * @param mode
     * @param score
     */
    public void refresh(int mode,int score){
    	if (mode==1){
    		if (score>=games1.get()) games1=new SafeInt(score);
    		SaveData();
    	}
    	if (mode==2){
    		if (score>=games2.get()) games2=new SafeInt(score);
    		SaveData();
    	}
    }
    
    /**
     * 获得最高分
     * @param mode
     * @return
     */
    public int get(int mode){
    	if (mode==1){
    		return games1.get();
    	}
    	if (mode==2){
    		return games2.get();
    	}
    	return 12345;
    }
    
    /**
     * 初始化音效
     */
    public void initMusic(){
    	soundp=new SoundPool(50,AudioManager.STREAM_MUSIC,100);
    	soundm=new HashMap<String,Integer>();
    	soundm.put("slide", soundp.load(MainActivity.this, R.raw.slide, 1));
    	soundm.put("flap", soundp.load(MainActivity.this, R.raw.flap, 1));
    	soundm.put("s1", soundp.load(MainActivity.this, R.raw.squish1, 1));
    	soundm.put("s2", soundp.load(MainActivity.this, R.raw.squish2, 1));
    	soundm.put("k1", soundp.load(MainActivity.this, R.raw.kick1, 1));
    	soundm.put("k2", soundp.load(MainActivity.this, R.raw.kick2, 1));
    }
    
    /**
     * 播放音效
     */
    public void playMusic(String str){
    	soundp.play(soundm.get(str), 1, 1, 0, 0, 1f);
    }
    
    public void onDestroy(){
    	soundp.release();
    	AppConnect.getInstance(this).close();
    	super.onDestroy();   
    }  
    
    /**
     * 返回拦截
     */
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				if ((System.currentTimeMillis() - clickTime) > 2000) {  
					Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",Toast.LENGTH_SHORT).show();  
				    clickTime = System.currentTimeMillis();  
			}else{
				 this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
    public void onPause(){  
    	gm.isStop=true;
    	super.onPause();  
    }  
    
    public void onStop(){  
    	gm.isStop=true;
    	super.onStop();  
    }  
    
    public void onResume(){
    	gm.myDraw();
    	super.onResume();  
    }  
    
    public void onRestart(){
    	gm.myDraw();
    	super.onRestart();  
    }  
    
	
}