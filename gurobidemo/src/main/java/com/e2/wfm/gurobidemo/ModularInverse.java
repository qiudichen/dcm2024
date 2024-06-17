package com.e2.wfm.gurobidemo;

public class ModularInverse {
    public static void main(String[] args) {
	int a = 13;
	int m = 11;
	System.out.println(modInverse(a, m));
    }

    static int modInverse(int a, int m) {
	a = a % m;
	for (int x = 1; x < m; x++)
	    if ((a * x) % m == 1)
		return x;
	return 1;
    }
}
