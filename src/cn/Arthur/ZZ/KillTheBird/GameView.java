package cn.Arthur.ZZ.KillTheBird;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
/**
 * 
 * @author Himi
 *
 */
public class GameView extends SurfaceView implements Callback, Runnable {
	//���ڿ���SurfaceView
	private SurfaceHolder sfh;
	//����һ������
	private Paint paint;
	//�ı�������
	private int textX = 10, textY = 10;
	//����һ���߳�
	private Thread th;
	//�߳������ı�ʶλ
	private boolean flag;
	//����һ������
	private Canvas canvas;
	//������Ļ�Ŀ��
	private int screenW, screenH;
	//���
	Random random=new Random();
	//����
	private Bitmap bg_1,bg_2,bg_3;
	private float bg_scale;
	//ˮ��
	private Bitmap pipe_up,pipe_down;
	private int pipeW;
	private int pipeD;
	private int fpipeD;
	private int pipeDx,pipeDy;
	private int pipeclose,pipeopen;
	private boolean pipeRunning=false;
	private int pipecloset,pipeopent;
	//��
	private ArrayList<Bird> birdlist=new ArrayList<Bird>();
	private Bitmap birdpic[][]=new Bitmap[5][4];
	private Bitmap abirdpic[]=new Bitmap[4];
	private int birdpics=3;
	private int birdH,birdW;
	private int deltatime=10;
	//Ѫ
	private Bitmap bloodpic;
	private int bloodW,bloodH;
	private int bloodtime;
	//�÷�
	private SafeInt score=new SafeInt(0);
	private Typeface mFace;   
	private int fontsize=100;
	//ģʽ
	private int mode;
	private boolean isStart;
	private boolean isEnd;
	//��ʼ��Դ
	private Bitmap tap;
	private Bitmap ready;
	private int tapDx=0;
	private int ftapDx=0;
	private int tapWay;
	//GameUI
	private int scoreW,scoreH;
	private int gameoverW,gameoverH;
	private Bitmap scoreb[]=new Bitmap[6];
	private Bitmap gameover;
	private Bitmap game1,game2;
	private int gameH,gameW;
	private Bitmap logo;
	private int logoH,logoW;
	private Bitmap help1,help2;
	private int helpH,helpW;
	//����
	private int lockS;
	//����
	private MainActivity main;
	private Handler handler;
	//Draw
	private boolean isDraw=true;
	//��ͣ
	private Bitmap stop;
	public boolean isStop=false;
	//Temp
	private ArrayList<Bird> tbirdlist=new ArrayList<Bird>();
	/**
	 * SurfaceView��ʼ������
	 */
	public GameView(Context context) {
		super(context);
		//ʵ��������
		main=(MainActivity) context;
		//ʵ��SurfaceHolder
		sfh = this.getHolder();
		//ΪSurfaceView���״̬����
		sfh.addCallback(this);
		//ʵ��һ������
		paint = new Paint();
		//���û�����ɫΪ��ɫ
		paint.setColor(Color.WHITE);
		//���ý���
		setFocusable(true);
		//��ʼ��ͼƬ
		initPic();
		//InitVal
		isStart=true;
		isEnd=true;
		//AD
		ShowADView();
	}

	/**
	 * ��Ϸ�߼�
	 */
	private void logic() {
		if (isStart==false){
			
		}else if (isEnd==true){
			
		}else{
			actBird();
			addBird(); 
			isGameEnd();
		}
		runPipe();
	}
	
	/**
	 * ��Ϸ��ͼ
	 */
	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				drawBackBG(canvas);
				drawPipe(canvas);
				drawBlood(canvas);
				drawFrontBG(canvas);
				drawBird(canvas);
				drawStop(canvas);
				drawScore(canvas);
				drawTap(canvas);
				drawGameBoard(canvas);
				drawButton(canvas);
				drawLogo(canvas);
				drawLockScreen(canvas);
				//-----------���������εķ�ʽ��ˢ��
				////���ƾ���
				//canvas.drawRect(0,0,this.getWidth(),
				//this.getHeight(), paint);
				//-----------������仭����ˢ��
				//canvas.drawColor(Color.WHITE);
				//-----------������仭��ָ������ɫ������ˢ��
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	
	/**
	 * ��Ϸ����
	 */
	public void isGameEnd(){
		ArrayList<Bird> temp=birdlist;
		int counts=temp.size();
		for (int i=0;i<counts;i++){
			if (screenW<=(int)temp.get(i).y) {
				main.refresh(mode, score.get());
				isEnd=true;
				tbirdlist=new ArrayList<Bird>();
				lockScreen();
			}
			
		}
	}
	
	/**
	 * ��ʼ��ť
	 */
	public void drawButton(Canvas canvas){
		if (isEnd==false) return;
		canvas.drawBitmap(game1,new Rect(0,0,game1.getWidth(),game1.getHeight()),new Rect(screenW*3/4-gameW/2,screenH/3-gameH/2,screenW*3/4+gameW/2,screenH/3+gameH/2), paint);
		canvas.drawBitmap(game2,new Rect(0,0,game2.getWidth(),game2.getHeight()),new Rect(screenW*3/4-gameW/2,screenH*2/3-gameH/2,screenW*3/4+gameW/2,screenH*2/3+gameH/2), paint);
	}
	
	/**
	 * ��Ϸ�÷ְ�
	 * @param canvas
	 */
	public void drawGameBoard(Canvas canvas){
		if (isEnd==false) return;
		if (mode==0) return;
		int s=score.get();
		int bs=main.get(mode);
		int index=1;
		if (score.get()>=30) index=2;
		if (score.get()>=100) index=3;
		if (score.get()>=150) index=4;
		if (score.get()>250) index=5;
		canvas.drawBitmap(gameover,new Rect(0,0,gameover.getWidth(),gameover.getHeight()),new Rect(screenW/4-gameoverW/2,screenH/4-gameoverH/2,screenW/4+gameoverW/2,screenH/4+gameoverH/2), paint);
		canvas.drawBitmap(scoreb[index],new Rect(0,0,scoreb[index].getWidth(),scoreb[index].getHeight()),new Rect(screenW/4-scoreW/2,screenH*3/4-scoreH/2,screenW/4+scoreW/2,screenH*3/4+scoreH/2), paint);
		drawText(String.valueOf(s), canvas,screenW/4-scoreW/2+(int)((float)0.707*scoreW), screenH*3/4-scoreH/2+(int)((float)0.307*scoreH),screenW/4-scoreW/2+(int)((float)0.885*scoreW), screenH*3/4-scoreH/2+(int)((float)0.438*scoreH), Color.WHITE);
		drawText(String.valueOf(bs), canvas,screenW/4-scoreW/2+(int)((float)0.707*scoreW), screenH*3/4-scoreH/2+(int)((float)0.658*scoreH),screenW/4-scoreW/2+(int)((float)0.885*scoreW), screenH*3/4-scoreH/2+(int)((float)0.789*scoreH), Color.WHITE);
	}
	
	/**
	 * ����
	 */
	public void lockScreen(){
		lockS=30;
	}
	
	/**
	 * ��������
	 */
	public void drawLockScreen(Canvas canvas){
		if (lockS<=0) {
			lockS=0;
			return;
		}else{
			if (lockS>=27) 
				canvas.drawRGB(0, 0, 0);
			else
				canvas.drawARGB(100*lockS/30, 0, 0, 0);
			lockS--;
		}
	}
	
	/**
	 * չ�ֵ÷�
	 */
	public void drawScore(Canvas canvas){
		int s=score.get();
		if (isStart==true && isEnd==false) drawScoreText("Score:"+String.valueOf(s),canvas,screenW/4*3,0,fontsize,Color.WHITE);
	}
	
	/**
	 * ���С��
	 */
	public void addBird(){
		if (deltatime<=0){
			Bird temp=null;
			if (mode==1){
				int level=1;
				int aspeed=0;
				if (score.get()>=0) {
					level=1;
					aspeed=screenW/96;
					if (score.get()<500) aspeed=score.get()*aspeed/500;
					temp=new Bird(screenW/96+aspeed+getRandom(-screenW/288,screenW/288),(float)screenH/720,level,screenH/2,getRandom(-birdW*2,-birdW/2),screenH/2+fpipeD-screenH*2/720,screenH/2-fpipeD,birdH,birdW,birdpic[getRandom(1,birdpics)],abirdpic);
					birdlist.add(temp);
				}
				if (score.get()>=10) {
					level=1;
					if (score.get()>50) level=2;
					aspeed=screenW/96;
					if (score.get()<500) aspeed=score.get()*aspeed/500;
					temp=new Bird(screenW/96+aspeed+getRandom(-screenW/288,screenW/288),(float)screenH/720,level,screenH/2,getRandom(-birdW*2,-birdW/2),screenH/2+fpipeD-screenH*2/720,screenH/2-fpipeD,birdH,birdW,birdpic[getRandom(1,birdpics)],abirdpic);
					birdlist.add(temp);
				}
				if (score.get()>=100) {
					level=1;
					if (score.get()>180) level=2;
					if (score.get()>300) level=3;
					aspeed=screenW/96;
					if (score.get()<500) aspeed=score.get()*aspeed/500;
					temp=new Bird(screenW/96+aspeed+getRandom(-screenW/288,screenW/288),(float)screenH/720,level,screenH/2,getRandom(-birdW*2,-birdW/2),screenH/2+fpipeD-screenH*2/720,screenH/2-fpipeD,birdH,birdW,birdpic[getRandom(1,birdpics)],abirdpic);
					birdlist.add(temp);
				}
			}
			if (mode==2){
				boolean flag=true;
				if (birdlist.size()+1>=3 && score.get()<=50) flag=false;
				if (birdlist.size()+1>=8 && score.get()<=100) flag=false;
				if (birdlist.size()+1>=15 && score.get()<=200) flag=false;
				if (flag==true) {
					temp=new Bird(screenW/144,(float)screenH/720,1,screenH/2,getRandom(-birdW*2,-birdW/2),screenH/2+fpipeD-screenH*2/720,screenH/2-fpipeD,birdH,birdW,birdpic[getRandom(1,birdpics)],abirdpic);
					birdlist.add(temp);
				}
			}
			deltatime=50;
		}else{
			deltatime--;
		}
		
	}
	/**
	 * С�񶯻�
	 */
	private void actBird(){
		ArrayList<Bird> temp=birdlist;
		int counts=temp.size();
		for (int i=0;i<counts;i++){
			if (temp.get(i).act(50,getRandom(10*screenH/720,15*screenH/720))) main.playMusic("flap");;
		}
	}
	/**
	 * ����С��
	 */
	public void drawBird(Canvas canvas){
		ArrayList<Bird> temp=birdlist;
		int counts=temp.size();
		for (int i=0;i<counts;i++){
			temp.get(i).draw(canvas);;
		}
	}
	/**
	 * ɱ��С��
	 */
	public void killBird(int up,int down){
		ArrayList<Bird> temp=birdlist;
		ArrayList<Bird> finish=new ArrayList<Bird>();
		int counts=temp.size();
		for (int i=0;i<counts;i++){
			int x=temp.get(i).x;
			int y=temp.get(i).y;
			if (screenW/2-pipeW/2<=y && screenW/2+pipeW/2>=y && (x-birdH/2<=up||x+birdH/2>=down)){
				if (bloodtime<=0) bloodtime=1;
				main.playMusic("s"+String.valueOf(getRandom(1,2)));
				if (mode==1) score.add(1);
				if (mode==2) {
					isEnd=true;
					main.refresh(mode, score.get());
				}
			}else{
				finish.add(temp.get(i));
			}
		}
		birdlist=finish;
	}
	/**
	 * ����С��
	 */
	public void rapBird(int up,int down){
		ArrayList<Bird> temp=birdlist;
		int counts=temp.size();
		for (int i=0;i<counts;i++){
			int x=temp.get(i).x;
			int y=temp.get(i).y;
			if (screenW/2-pipeW/2>=y && screenW/2-pipeW/2<=y+birdW/2 && (x-birdH/2<=up||x+birdH/2>=down)){
				//temp.get(i).levelUp(-1,screenW/20*(pipeW-screenW/2+y)/pipeW*2);
				if (mode==1)temp.get(i).levelUp(-1,temp.get(i).level*temp.get(i).v*2*(pipeW-screenW/2+y)/pipeW*2,screenH/40);
				if (mode==2)temp.get(i).levelUp(-1,temp.get(i).level*temp.get(i).v*3*(pipeW-screenW/2+y)/pipeW*2,screenH/40);
				main.playMusic("k"+String.valueOf(getRandom(1,2)));
				if (mode==2) score.add(1);
			}else if (screenW/2+pipeW/2>=y-birdW/2 && screenW/2+pipeW/2<=y && (x-birdH/2<=up||x+birdH/2>=down)){
				if (mode==2) score.add(1);
				temp.get(i).levelUp(1,temp.get(i).level*temp.get(i).v,screenH/40);
				main.playMusic("k"+String.valueOf(getRandom(1,2)));
			}else if (screenW/2-pipeW/2<=y && screenW/2+pipeW/2>=y && (x-birdH/2<=up||x+birdH/2>=down)){
				if (mode==1)temp.get(i).levelUp(-1,temp.get(i).level*temp.get(i).v*2*(pipeW-screenW/2+y)/pipeW*2,screenH/40);
				if (mode==2)temp.get(i).levelUp(-1,temp.get(i).level*temp.get(i).v*3*(pipeW-screenW/2+y)/pipeW*2,screenH/40);
				main.playMusic("k"+String.valueOf(getRandom(1,2)));
				if (mode==2) score.add(1);
			}
		}
	}
	
	/**
	 * ���Ӷ���
	 */
	public void runPipe(){
		if(pipeopen!=0 || pipeclose!=0){
			pipeDx=getRandom(-5,5);
			pipeDy=getRandom(-5,5);
			if (pipeopen>pipeopent) pipeDx=pipeDy=0;
		}
		if (pipeopen!=0){
			if (pipeopen>pipeopent){
				pipeopen--;
			}else{
				pipeD+=fpipeD/pipeopent;
				pipeopen--;
				if (pipeopen<=0) {
					pipeopen=0;
					pipeD=fpipeD;
					pipeDx=pipeDy=0;
					pipeRunning=false;
				}
			}
			
		}
		if (pipeclose!=0){
			pipeD-=fpipeD/pipecloset;
			pipeclose--;
			if (pipeclose<=0) {
				pipeopen=pipeopent+5;
				pipeclose=0;
				pipeD=0;
				pipeDx=pipeDy=0;
			}
			killBird(screenH/2-pipeD,screenH/2+pipeD);
		}
		rapBird(screenH/2-pipeD,screenH/2+pipeD);
	}
	
	/**
	 * ��ʼˮ�ܶ���
	 */
	public void startPipe(){
		pipeRunning=true;
		pipeclose=pipecloset;
		main.playMusic("slide");
	}
	
	/**
	 * ѪҺ����
	 */
	public void drawBlood(Canvas canvas){
		if (pipeopen<=0&&bloodtime==1) return;
		if (bloodtime<=0) return;
		int w=bloodpic.getWidth()/5;
		int h=bloodpic.getHeight()/6;
		int x=(bloodtime+1)%5-1-1;
		int y=(bloodtime-1)/5;
		canvas.drawBitmap(bloodpic, new Rect(x*w,y*h,x*w+w,y*h+h), new Rect(screenW/2-bloodW/2,screenH/2,screenW/2+bloodW/2,screenH/2+bloodH), paint);
		canvas.drawBitmap(bloodpic, new Rect(x*w,y*h,x*w+w,y*h+h), new Rect(screenW/2-bloodW/2,screenH/2,screenW/2+bloodW/2,screenH/2+bloodH), paint);
		bloodtime++;
		bloodtime++;
		bloodtime++;
		if (bloodtime>=30) bloodtime=0;
	}
	
	/**
	 * ��ʼ����
	 */
	public void drawTap(Canvas canvas){
		if (isStart==false){
			canvas.drawBitmap(ready, new Rect(0,0,ready.getWidth(),ready.getHeight()),new Rect(screenW/2-ready.getWidth()/2,screenH/4-ready.getHeight()/2+tapDx,screenW/2+ready.getWidth()/2,screenH/4+ready.getHeight()/2+tapDx),paint);
			canvas.drawBitmap(tap, new Rect(0,0,tap.getWidth(),tap.getHeight()),new Rect(screenW/2-tap.getWidth()/2,screenH/4*3-tap.getHeight()/2+tapDx,screenW/2+tap.getWidth()/2,screenH/4*3+tap.getHeight()/2+tapDx),paint);
			if (mode==1)canvas.drawBitmap(help1, new Rect(0,0,help1.getWidth(),help1.getHeight()), new Rect(screenW/2-helpW/2,screenH/2-helpH/2,screenW/2+helpW/2,screenH/2+helpH/2),paint);
			if (mode==2)canvas.drawBitmap(help2, new Rect(0,0,help1.getWidth(),help1.getHeight()), new Rect(screenW/2-helpW/2,screenH/2-helpH/2,screenW/2+helpW/2,screenH/2+helpH/2),paint);
			if (tapDx*tapDx>=ftapDx*ftapDx) tapWay*=-1;
			tapDx+=tapWay;
		}
	}
	
	/**
	 * AD
	 */
	public void ShowADView(){
		handler = new Handler();  
	    //�½�һ���̶߳���  
		handler.post(updateThread);
	}
	
	
	/**
	 * �����Ļ���
	 */
	public void drawBackBG(Canvas canvas){
		//bg_scale=(float)screenH/3/bg_3.getHeight();
		canvas.drawColor(Color.rgb(36, 191, 242));
		for (int i=1;i<=screenW;i+=bg_3.getWidth()*bg_scale){
			canvas.drawBitmap(bg_3, new Rect(0,0,bg_3.getWidth(),bg_3.getHeight()), new Rect(i,(int)(screenH-bg_3.getHeight()*bg_scale-bg_1.getHeight()*bg_scale),(int)(i+bg_3.getWidth()*bg_scale),(int)(screenH-bg_1.getHeight()*bg_scale)), paint);
		}
		for (int i=1;i<=screenW;i+=bg_2.getWidth()*bg_scale){
			canvas.drawBitmap(bg_2, new Rect(0,0,bg_2.getWidth(),bg_2.getHeight()), new Rect(i,(int)(screenH-bg_2.getHeight()*bg_scale-bg_1.getHeight()*bg_scale),(int)(i+bg_2.getWidth()*bg_scale),(int)(screenH-bg_1.getHeight()*bg_scale)), paint);
		}
	}
	public void drawFrontBG(Canvas canvas){
		//bg_scale=(float)screenH/3/bg_3.getHeight();
		for (int i=1;i<=screenW;i+=bg_1.getWidth()*bg_scale){
			canvas.drawBitmap(bg_1, new Rect(0,0,bg_1.getWidth(),bg_1.getHeight()), new Rect(i,(int)(screenH-bg_1.getHeight()*bg_scale),(int)(i+bg_1.getWidth()*bg_scale),screenH), paint);
		}
	}
	
	
	/**
	 * LOGO
	 */
	public void drawLogo(Canvas canvas){
		if (mode!=0) return ;
		canvas.drawBitmap(logo, new Rect(0,0,logo.getWidth(),logo.getHeight()),new Rect(screenW/4-logoW/2,screenH/2-logoH/2,screenW/4+logoW/2,screenH/2+logoH/2),paint);
	}
	/**
	 * ����ˮ��
	 */
	public void drawPipe(Canvas canvsa){
		//pipeW=screenW/10;
		canvas.drawBitmap(pipe_up, new Rect(0,pipe_up.getHeight()-screenH/2,pipe_up.getWidth(),pipe_up.getHeight()), new Rect(screenW/2-pipeW/2+pipeDy,0,screenW/2+pipeW/2+pipeDy,screenH/2-pipeD+pipeDx),paint);
		canvas.drawBitmap(pipe_down, new Rect(0,0,pipe_down.getWidth(),screenH/2), new Rect(screenW/2-pipeW/2-pipeDy,screenH/2+pipeD-pipeDx,screenW/2+pipeW/2-pipeDy,screenH),paint);
	}
	
	/**
	 * ������ͣ
	 */
	public void drawStop(Canvas canvas){
		if (isEnd==true || isStart==false || mode==0) return;
		if (isStop==false)canvas.drawBitmap(stop, new Rect(0,0,stop.getWidth(),stop.getHeight()),new Rect(screenW-50,0,screenW,50), paint);
		if (isStop==true){
			canvas.drawARGB(50, 0, 0, 0);
			drawText("�����Ļ������Ϸ",canvas,screenW/2,screenH/2,fontsize,Color.WHITE);
		}
	
	}
	/**
	 * ��������
	 * @param str
	 * @param canvas
	 */
	private void drawText(String str,Canvas canvas,int x1,int y1,int x2,int y2,int color){

		Paint countPaint = new Paint();
		countPaint.setColor(color);
		countPaint.setTextSize(100);
		countPaint.setTypeface(mFace);
		countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		int textHeight=textBounds.bottom-textBounds.top;
		while (textWidth>x2-x1 || textHeight>y2-y1){
			countPaint.setTextSize(countPaint.getTextSize()-1);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
			textWidth =textBounds.right-textBounds.left;
			textHeight=textBounds.bottom-textBounds.top;
		}
		canvas.drawText(str, (x2-x1)/2+x1-textWidth/2, (y2-y1)/2+y1+textHeight/2,countPaint);
	}
	private void drawText(String str,Canvas canvas,int x,int y,int size,int color){
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(color);
		countPaint.setTextSize(size);
		//countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		while (x+textWidth/2>screenW||x-textWidth/2<0){
			countPaint.setTextSize(countPaint.getTextSize()-1);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
			textWidth =textBounds.right-textBounds.left;
		}
		int textHeight = textBounds.bottom - textBounds.top;
		canvas.drawText(str, x-textWidth/2, y + textHeight/2,countPaint);
	}
	private void drawScoreText(String str,Canvas canvas,int x,int y,int size,int color){
		Paint countPaint = new Paint();
		countPaint.setColor(color);
		countPaint.setTextSize(size);
		countPaint.setTypeface(mFace);
		//countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		int textHeight = textBounds.bottom - textBounds.top;
		canvas.drawText(str, x-textWidth/2, y+textHeight*2,countPaint);
	}
	
	
	/**
	 * ���ͼƬ��Դ
	 */
	public void initPic(){
		bg_1=getRes(R.drawable.bg_ground);
		bg_2=getRes(R.drawable.bg_trees);
		bg_3=getRes(R.drawable.bg_city);
		pipe_up=getRes(R.drawable.pipe_up);
		pipe_down=getRes(R.drawable.pipe_down);
		birdpic[1][1]=getRes(R.drawable.bird1_1);
		birdpic[1][2]=getRes(R.drawable.bird1_2);
		birdpic[1][3]=getRes(R.drawable.bird1_3);
		birdpic[2][1]=getRes(R.drawable.bird2_1);
		birdpic[2][2]=getRes(R.drawable.bird2_2);
		birdpic[2][3]=getRes(R.drawable.bird2_3);
		birdpic[3][1]=getRes(R.drawable.bird3_1);
		birdpic[3][2]=getRes(R.drawable.bird3_2);
		birdpic[3][3]=getRes(R.drawable.bird3_3);
		abirdpic[1]=getRes(R.drawable.angrybird1);
		abirdpic[2]=getRes(R.drawable.angrybird2);
		abirdpic[3]=getRes(R.drawable.angrybird3);
		bloodpic=getRes(R.drawable.blood);
		tap=getRes(R.drawable.tap);
		ready=getRes(R.drawable.ready);
		scoreb[1]=getRes(R.drawable.score);
		scoreb[2]=getRes(R.drawable.score1);
		scoreb[3]=getRes(R.drawable.score2);
		scoreb[4]=getRes(R.drawable.score3);
		scoreb[5]=getRes(R.drawable.score4);
		gameover=getRes(R.drawable.gameover);
		game1=getRes(R.drawable.game1);
		game2=getRes(R.drawable.game2);
		logo=getRes(R.drawable.logo);
		help1=getRes(R.drawable.help1);
		help2=getRes(R.drawable.help2);
		stop=getRes(R.drawable.stop);
	}
	
	/**
	 * ��ʼ��һЩ��ֵ
	 */
	public void initVal(){
		bg_scale=(float)screenH/3/bg_3.getHeight();
		pipeW=screenW/8;
		fpipeD=pipeD=screenH/8;
		pipeDx=pipeDy=0;
		pipeclose=pipeopen=0;
		pipecloset=2;
		pipeopent=10;
		pipeRunning=false;
		birdH=screenH/16;
		birdW=birdpic[1][1].getWidth()*birdH/birdpic[1][1].getHeight();
		bloodW=pipeW*2;
		bloodH=screenH/4;
		bloodtime=0;
		score=new SafeInt(0);
		tapDx=0;
		tapWay=screenH/320;
		ftapDx=screenH/80;
		scoreW=screenW*8/2/10;
		scoreH=scoreW*scoreb[1].getHeight()/scoreb[1].getWidth();
		gameoverW=screenW*6/2/10;
		gameoverH=gameoverW*gameover.getHeight()/gameover.getWidth();;
		gameW=screenW/4;
		gameH=game1.getHeight()*gameW/game1.getWidth();
		logoW=screenW/2/10*9;
		logoH=logo.getHeight()*logoW/logo.getWidth();
		helpH=fpipeD*2;
		helpW=helpH*help1.getWidth()/help1.getHeight();
		lockS=0;
		birdlist=tbirdlist;
	}
	
	/**
	 * ��ʼ����Ϸ��Ϣ
	 */
	public void initGame(){
		birdlist=new ArrayList<Bird>();
		bloodtime=0;
		score=new SafeInt(0);
		pipeDx=pipeDy=0;
		pipeclose=pipeopen=0;
		fpipeD=pipeD=screenH/8;
		pipeRunning=false;
		isStart=false;
		isEnd=false;
	}
	/**
	 * ���������С
	 */
	private void initFontSize(){
		mFace = Typeface.createFromAsset(getContext().getAssets(),"233.ttf");
		String str="1000";
		Paint countPaint=new Paint();
		countPaint.setTypeface(mFace);
		countPaint.setTextSize(fontsize);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		while (textBounds.height()>screenH/4){
			fontsize--;
			countPaint.setTextSize(fontsize);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
		}
	}
	
	/**
	 * ��������
	 */
	private int getRandom(int min,int max){  
		int ran=Math.abs(random.nextInt());  
		int returnRan=ran%(max-min+1)+min;  
		return returnRan;
	}
	
	/**
	 * �����¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x=textX = (int) event.getX();
		int y=textY = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isStop==true){
				isStop=false;
				main.removeAD();
				return true;
			}
			if (!isEnd && isStart && screenW-50<=x && x<=screenW && y<=50){
				isStop=true;
				myDraw();
				//main.showAD();
				return true;
			}
			if (!isStop && !pipeRunning && isStart && !isEnd) startPipe();
			if (!isStop && !isStart) isStart=true;
			if (isEnd){
				if (screenW*3/4-gameW/2<=x && screenH/3-gameH/2<=y && screenW*3/4+gameW/2 >=x && screenH/3+gameH/2>=y){
					mode=1;
					initGame();
					lockScreen();
					main.removeAD();
				}
				if (screenW*3/4-gameW/2<=x && screenH*2/3-gameH/2<=y && screenW*3/4+gameW/2 >=x && screenH*2/3+gameH/2>=y){
					mode=2;
					initGame();
					lockScreen();
					main.removeAD();
				}
			}
		}
		return true;
	}
	
	/**
	 * �����¼�����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * ����ͼƬ��Դ
	 */
	public Bitmap getRes(int resID) {
		return BitmapFactory.decodeResource(getResources(),resID);
	}
	
	/**
	 * SurfaceView��ͼ��������Ӧ�˺���
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		//��ʼ����ֵ
		initVal();
		//�����ʼ��
		initFontSize();
		//�߳̿���
		flag = true;
		//ʵ���߳�
		th = new Thread(this);
		//�����߳�
		th.start();
	}
	
	/**
	 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		tbirdlist=birdlist;
		flag = false;
	}
	
	/**
	 * ��Ϸ�߳�
	 */
	public void run() {
		while (flag) {
			isDraw=!isDraw;
			if (isDraw)myDraw();
			if (isStop==false) {
				long start = System.currentTimeMillis();
				logic();
				long end = System.currentTimeMillis();
				try {
					if (end - start < 30) {
						Thread.sleep(30 - (end - start));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * AD
	 */
	 Runnable updateThread = new Runnable(){  
	        //��Ҫִ�еĲ���д���̶߳����run��������  
	        public void run(){  
	            if (isEnd==true && mode!=0) main.showAD();  
	            handler.postDelayed(updateThread, 1000);  
	        }  
	    };  
}
