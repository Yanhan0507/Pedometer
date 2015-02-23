package com.example.pedometer;

import android.os.Handler;
import android.os.Message;


public class Timer
{
	private double recLen = 0; 
	boolean run=false;
	public boolean hasStart=false;
	
	MyThread myThread=new MyThread();
	public void start()
	{
		if(!hasStart)
			myThread.start();
		recLen=0;
		run=true;
		
	}
	public void stop()
	{
		run=!run;
		//myThread.interrupt();
	}
	
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				recLen+=0.1;
			}
			super.handleMessage(msg);
		}
	};
	class MyThread extends Thread implements Runnable{      // thread  
        @Override  
        public void run()
        {  
            while(true)
            {  
                try
                {  
                    Thread.sleep(100);     // sleep 100ms  
                    Message message = new Message(); 
                    if(run)
                    	message.what = 1;  
                    else 
						message.what = 0;
                    handler.sendMessage(message);
                }
                catch (Exception e) 
                {  
                }  
            }  
        }  
    }
	public double getTime()
	{
		return recLen;
	}
}