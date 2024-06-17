package com.e2.wfm.commons.sampling;

public class SamplingUntil {
    public static void main(String[] args) {
	double d = 1.01;
	double up = Math.ceil(d);
	double down = Math.floor(d);
	System.out.println("up: " + up);
	System.out.println("down: " + down);
    }
}
