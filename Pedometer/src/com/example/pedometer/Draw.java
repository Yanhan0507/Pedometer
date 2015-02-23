package com.example.pedometer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;


public class Draw 
{
	SurfaceHolder holder;
	int width,height,count=0,k=8;	
	float centerY1=0,centerY2=0;	
	float y_set1[],y_set2[];	
	boolean run=true;
	
	public void initial(SurfaceHolder SurfaceHolder_Draw,int width,int height)
	{
		holder=SurfaceHolder_Draw;
		this.width=width;
		this.height=height;
		y_set1=new float[width];
		y_set2=new float[width];
		for(int i=0;i<width;i++)
		{
			y_set1[i]=0;
			y_set2[i]=0;
		}
	}
	public void start()
	{
		run=true;
	}	
	public void stop()
	{
		run=!run;
	}
	public void paint(float y1,float y2,float centerY1,float centerY2)
	{	
		if(run)
		{
			if(count<width)
				count++;
			this.centerY1=centerY1;
			this.centerY2=centerY2;
			int i;
			for(i=0;i<width-1;i++)
			{
				y_set1[i]=y_set1[i+1];
				y_set2[i]=y_set2[i+1];
			}
			y_set1[i]=y1;
			y_set2[i]=y2;
			
			Canvas canvas = holder.lockCanvas();
			
			Paint paint=new Paint();
		
			paint.setColor(Color.BLACK);
			canvas.drawRect(0,0,width,height, paint);
			paint.setColor(Color.GREEN);
			for(i=width-count;i<width-1;i++)
            {
              	canvas.drawLine(i, (centerY1-k*(y_set1[i])), i+1, (centerY1-k*(y_set1[i+1])), paint);
            }
			
			paint.setColor(Color.WHITE);
			canvas.drawLine(0, centerY1, width, centerY1, paint);
		
			canvas.drawLine(0, 10, 0, centerY1+10, paint);
			canvas.drawLine(0, centerY1-5*k, 10, centerY1-5*k, paint);
			canvas.drawText("5", 10, centerY1-5*k+3, paint);
			canvas.drawLine(0, centerY1-10*k, 10, centerY1-10*k, paint);
			canvas.drawText("10", 10, centerY1-10*k+3, paint);
			canvas.drawLine(0, centerY1-15*k, 10, centerY1-15*k, paint);
			canvas.drawText("15", 10, centerY1-15*k+3, paint);
			canvas.drawLine(0, centerY1-20*k, 10, centerY1-20*k, paint);
			canvas.drawText("20", 10, centerY1-20*k+3, paint);
			canvas.drawText("Before Filtering", width/2-40, 20, paint);

			
			paint.setColor(Color.GREEN);
			for(i=width-count;i<width-1;i++)
            {
              	canvas.drawLine(i, (centerY2-k*(y_set2[i])), i+1, (centerY2-k*(y_set2[i+1])), paint);
            }
			
			paint.setColor(Color.WHITE);
			canvas.drawLine(0, centerY2, width, centerY2, paint);
			
			canvas.drawLine(0, 10, 0, centerY2+10, paint);
			canvas.drawLine(0, centerY2-5*k, 10, centerY2-5*k, paint);
			canvas.drawText("5", 10, centerY2-5*k+3, paint);
			canvas.drawLine(0, centerY2-10*k, 10, centerY2-10*k, paint);
			canvas.drawText("10", 10, centerY2-10*k+3, paint);
			canvas.drawLine(0, centerY2-15*k, 10, centerY2-15*k, paint);
			canvas.drawText("15", 10, centerY2-15*k+3, paint);
			canvas.drawLine(0, centerY2-20*k, 10, centerY2-20*k, paint);
			canvas.drawText("20", 10, centerY2-20*k+3, paint);
			
			canvas.drawText("After Filtering", width/2-45, centerY1+20, paint);
			
			holder.unlockCanvasAndPost(canvas);
		}
	}
}