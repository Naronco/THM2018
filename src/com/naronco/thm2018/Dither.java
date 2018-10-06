package com.naronco.thm2018;

public class Dither {
    public static int[] palette = new int[]{
            0,
            0x222034,
            0x45283C,
            0x663931,
            0x8F563B,
            0xDF7126,
            0xD9A066,
            0xEEC39A,
            0xFBF236,
            0x99E550,
            0x6ABE30,
            0x37946E,
            0x4B692F,
            0x524B24,
            0x323C39,
            0x3F3F74,
            0x306082,
            0x5B6EE1,
            0x639BFF,
            0x5FCDE4,
            0xCBDBFC,
            0xFFFFFF,
            0x9BADB7,
            0x847E87,
            0x696A6A,
            0x595652,
            0x76428A,
            0xAC3232,
            0xD95763,
            0xD77BBA,
            0x8F974A,
            0x8A6F30
    };
    
    private static int colorDistance(int col0, int col1) {
        int r0 = (col0 >> 16) & 0xFF;
        int g0 = (col0 >> 8) & 0xFF;
        int b0 = (col0) & 0xFF;
        int r1 = (col1 >> 16) & 0xFF;
        int g1 = (col1 >> 8) & 0xFF;
        int b1 = (col1) & 0xFF;
        return Math.abs(r0 - r1) + Math.abs(g0 - g1) + Math.abs(b0 - b1);
    }
    
    private static long lookupColors(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        int closest = 0;
        int distance = 0xFFFFFF;
        int secondClosest = 0;
        for (int other : palette) {
            int or = (other >> 16) & 0xFF;
            int og = (other >> 8) & 0xFF;
            int ob = (other) & 0xFF;
            int odist = Math.abs(r - or) + Math.abs(g - og) + Math.abs(ob - b);
            if (odist < distance) {
            	secondClosest = closest;
                closest = other;
                distance = odist;
            }
        }
        return (((long)secondClosest) << 32l) | (long)closest;
    }
    
	private static int[] dither = {
			14, 2, 16, 4,
			0, 12, 2, 13,
			16, 0, 13, 2,
			3, 12, 6, 16
	};
	
	private static int rollDither(int dither, int x) {
		int ret = dither << (x & 3);
		int left = (ret >> 4) & ~(0xFF << x);
		return (ret | left) % 0xf;
	}

    public static int lookupColor(int x, int y, int color) {
    	long colors = lookupColors(color);
    	int closest = (int)(colors & 0xFFFFFFl);
    	int secondClosest = (int)(colors >>> 32);
    	int dither = (int)(colorDistance(closest, color) / (double)0x1ff * 16) % 0xF;
    	if (rollDither(dither, x) == 0) {
    		return closest;
    	} else {
    		return secondClosest;
    	}
    }
}
