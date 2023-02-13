package com.baidu.mapapi.overlayutil;

public class BaiduZoomLevel {
	//这是指1比几
	public static int[] meter = new int[]{5,10,
	20, 50, 100, 200, 500,
	1000, 2000,  5000, 10000, 20000,
	25000, 50000, 100000, 200000, 500000,
	1000000,  2000000};
	public static int[] level = new int[]{22,21,
	20, 19, 18, 17, 16,
	15, 14,  13, 12, 11,
	10, 9, 8, 7, 6,
	5,  4};
	
	public static int getLevelFromMeter(int meterr){
		for(int i=0;i<meter.length;i++){
			if(i == meter.length-1){
				return level[1];
			}
			int len = meter[i];
			int len1 = meter[i+1];
			if(meterr>=len && meterr<=len1){
				return level[i];
			}
		}
		return 0;
	}
}
