package com.example.pedometer;


public class steps 
{
	private int judge=-1;
	private int i=1;
	int count=0;
	boolean run=false;
	public void start()
	{
		run=true;
	}
	public void stop()
	{
		run=false;
	}
	public int maximum(float pre_result2,float result2) 
	{
		if (run&&pre_result2>10.3&&result2>10.3) 
		{
			if (judge == -1) {
				judge = 0;
				return count;
			}
			if (judge == 0 && result2 > pre_result2) {
				judge = 1;
			}
			if (judge == 1 && result2 < pre_result2) {
				judge = 0;
				count += 1;
				if (i >= 10) {
					i = 0;
				} else {
					i = 0;
					count -= 1;
				}
			}
			i += 1;
		}
		return count;
	}
}
