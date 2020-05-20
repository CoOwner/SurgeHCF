package com.surgehcf.essentials.utilities;

import java.util.Random;

import com.surgehcf.essentials.SurgeExtra;

public class RandomUtils {

	
	SurgeExtra ins;
	
	public RandomUtils(){
		ins = SurgeExtra.getInstance();
	}
	
	public Integer getRandomNumber(int min, int max){
		Random rand = new Random();
		int  n = rand.nextInt(max) + min;
		return n;
	}
}
