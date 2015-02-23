package com.example.pedometer;

public class Kalman {
	private float P1=10,P2=10;
	
	private float result1=0,result2=0;
	private float kg1,kg2;
	public float kalman_filter1(float sqtr_xyz,float pre_result1,int Q,int R)
	{
		P1+=Q;
		kg1=P1 / (P1 + R);
		result1 = pre_result1 + kg1 * (sqtr_xyz - pre_result1);
		P1 = (1 - kg1) * P1;
		return result1;
	}
	public float kalman_filter2(float sqtr_xyz,float pre_result2,int Q,int R)
	{
		P2+=Q;
		kg2=P2 / (P2 + R);
		result2 = pre_result2 + kg2 * (sqtr_xyz - pre_result2);
		P2 = (1 - kg2) * P2;
		return result2;
	}	
}
