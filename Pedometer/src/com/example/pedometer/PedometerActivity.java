
package com.example.pedometer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import android.R.color;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class PedometerActivity extends Activity implements SensorEventListener,OnClickListener
{
	//pedometer application @Yanhan
	private Button Button_Write,Button_Stop;	
	private boolean doWrite=false,hasMeasured=false,hasInitial=false,hasStart=false;	
	private boolean doCalculateFrequence=true;
	private SensorManager sensorManager;
	private float x,y,z,sqrt_xyz,centerY1,centerY2;	
	private float result1,result2;
	@SuppressWarnings("unused")
	private int frequence=0;	
	private TextView TextView_Acc;		
	private TextView TextView_Time;		
	private TextView TextView_Step;    
	private SurfaceView SurfaceView_Draw;	
	private SurfaceHolder SurfaceHolder_Draw;
	File sdCardDir,acc_file,filter_file;	
	private String message,message1;
	private int dataCount=0;
	private int sum_step=0;
	int  draw_width,draw_height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		
		TextView_Acc = (TextView)findViewById(R.id.TextView_Acc);
		TextView_Time= (TextView)findViewById(R.id.TextView_Time);
		TextView_Step= (TextView)findViewById(R.id.TextView_Step);
		SurfaceView_Draw=(SurfaceView)findViewById(R.id.SurfaceView_Draw);
		SurfaceHolder_Draw=SurfaceView_Draw.getHolder();

		
        ViewTreeObserver vto = SurfaceView_Draw.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                if (hasMeasured == false)
                {
                    draw_width = SurfaceView_Draw.getMeasuredWidth();
                    draw_height=SurfaceView_Draw.getMeasuredHeight();
                    hasMeasured = true;
                }
                return true;
            }
        });
		


        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
 
        sensorManager.registerListener(this, 
        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
        		sensorManager.SENSOR_DELAY_GAME);	
        
        Button_Write=(Button)findViewById(R.id.Button_Write);
        Button_Write.setOnClickListener(this);
        Button_Stop=(Button)findViewById(R.id.Button_Stop);
        Button_Stop.setOnClickListener(this);
        
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            sdCardDir = Environment.getExternalStorageDirectory();
            String sdcardPath = sdCardDir.getAbsolutePath(); 
            acc_file = new File(sdcardPath+"/"+"acc.csv"); 	
            filter_file=new File(sdcardPath+"/"+"filter.csv");
        }  
	}

	


    public void onPause()
    {	
    	super.onPause();
    }

 
    public void writeFileSdcard(String message,Boolean end) 
    {
        try
		{
        	RandomAccessFile outFile = new RandomAccessFile(acc_file, "rw"); 
        	if(end==true)
        		outFile.seek(acc_file.length());  
        	else 
        		outFile.seek(0);
			
        	outFile.write(message.getBytes());  
        	outFile.close();  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    public void writeFileSdcard1(String message,Boolean end) 
    {
        try
		{
        	RandomAccessFile outFile = new RandomAccessFile(filter_file, "rw"); 
        	if(end==true)
        		outFile.seek(filter_file.length()); 
        	else 
        		outFile.seek(0);
			
        	outFile.write(message.getBytes()); 
        	outFile.close();  
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    public void deleteFile()
    {
    	if(acc_file.exists())
    		acc_file.delete();
    	if(filter_file.exists())
    		filter_file.delete();
    }
    
    Timer timer=new Timer();
    DecimalFormat df = new DecimalFormat("#,##0.0");
    Draw draw=new Draw();
    steps steps=new steps();

    public void onClick(View v) {
		if (v.getId() == R.id.Button_Write)
		{	
			if(!hasStart)
			{
				timer.start();
				timer.hasStart=true;
				hasStart=true;
			}
			else
				timer.start();
			steps.start();
			deleteFile();
			doWrite = true;
			writeFileSdcard("\n",false);
			
		
			centerY1=SurfaceView_Draw.getHeight()/2;
			centerY2=SurfaceView_Draw.getHeight();
			draw.initial(SurfaceHolder_Draw,draw_width,draw_height);
			draw.start();
			hasInitial=true;
		}
		if (v.getId() == R.id.Button_Stop)
		{
			
			doWrite = false;
			message=new String();
			message=df.format(timer.getTime())+"\n";
			writeFileSdcard(message,false);
			timer.stop();
			draw.stop();
			steps.stop();
		}
	}
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) 
    {	
	}
    String m1=new String();
    Kalman kalman=new Kalman();
    float pre_result1=10,pre_result2=10;
    public void onSensorChanged(SensorEvent event)
    {
    	TextView_Time.setText("Time:"+df.format(timer.getTime())+"s");
    	message=new String();
    	
    	if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
    	{
    		x = event.values[0];
			y = event.values[1];
			z = event.values[2];
    		sqrt_xyz=(float) Math.sqrt(x*x+y*y+z*z);
 		
			result1=kalman.kalman_filter1(sqrt_xyz,pre_result1,5,3000);
			pre_result1=result1;
			result2=kalman.kalman_filter2(pre_result1,pre_result2,1000,100000);
			sum_step=steps.maximum( pre_result2, result2);
			pre_result2=result2;
			TextView_Step.setText(sum_step+"Step");
			if(hasInitial)
    			draw.paint(sqrt_xyz,result2,centerY1-10,centerY2-10);	
    		message = df.format(x) + ",";
			message += df.format(y) + ",";
			message += df.format(z) + "\n";
			//message1=df.format(result1)+"\n";
			message1=result1+"\n";
			TextView_Acc.setText("Acceleration"+message+m1);
			
			if(doCalculateFrequence)
    		{	
				dataCount++;
				if((int)timer.getTime()==2)
				{
					doCalculateFrequence=false;
					m1="Frequency"+dataCount/2+"Hz";
				}
    		}
			if (doWrite)
			{
				writeFileSdcard(message,true);
				writeFileSdcard1(message1,true);
			}
    	}
    }
}