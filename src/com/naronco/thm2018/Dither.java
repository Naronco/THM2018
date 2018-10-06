package com.naronco.thm2018;

import java.util.Arrays;

public class Dither {
	public static Integer[] palette = new Integer[]{
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
			0x8A6F30,
			0x1a1011,
			0x1e1422,
			0x181c33,
			0x1d2b47,
			0x13386f,
			0x33506c,
			0x4d6177,
			0x1b797d,
			0x28724c,
			0x185f35,
			0x374a1b,
			0x173425,
			0x122529,
			0x1c1e19,
			0x3a1f1f,
			0x413018,
			0x70372d,
			0x7f4d31,
			0x72662f,
			0x7e6d65,
			0x7f7f7f,
			0x5b564d,
			0x433f42,
			0x353534,
			0x292b2c,
			0x45213b,
			0x191956,
			0x312b6c,
			0x5d3d6b,
			0x254b47,
			0x183745,
			0x44282c,
			0x50345c,
			0x404c88,
			0x4c70bc,
			0x309428,
			0x88d420,
			0xcc043c,
			0x48404c,
			0x6830cc,
			0x40fc8c,
			0x90c448,
			0x3c8c64,
			0x30646c,
			0x4c5040,
			0x985454,
			0xac8040,
			0x2c9078,
			0x54cc84,
			0x30107c,
			0x50240c,
			0x545454,
			0xf4e4cc,
			0xb4a8b0,
			0x8c8c8c,
			0x6c7074,
			0xb8589c,
			0x4040e4,
			0x847420,
			0xf8a41c,
			0x60c8bc,
			0x4094b8
	};

	private static double colorDistance(int col0, int col1) {
		int r0 = (col0 >> 16) & 0xFF;
		int g0 = (col0 >> 8) & 0xFF;
		int b0 = (col0) & 0xFF;
		int r1 = (col1 >> 16) & 0xFF;
		int g1 = (col1 >> 8) & 0xFF;
		int b1 = (col1) & 0xFF;

		long rmean = ((long) r0 + (long) r1) / 2;
		long r = (long) r0 - (long) r1;
		long g = (long) g0 - (long) g1;
		long b = (long) b0 - (long) b1;
		return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
	}

	private static long lookupColors(int color) {
		int closest = 0;
		double distance = 0xFFFFFF;
		double secondDistance = 0xFFFFFF;
		int secondClosest = 0;
		for (int other : palette) {
			double odist = colorDistance(color, other);
			if (odist < distance) {
				secondClosest = closest;
				closest = other;
				distance = odist;
			} else if (odist < secondDistance && other != closest) {
				secondClosest = other;
				secondDistance = odist;
			}
		}
		return (((long) secondClosest) << 32l) | (long) closest;
	}

	private static int[] ditherLookup = {
			0, 0, 0, 3,
			1, 2, 4, 3,
			5, 10, 6, 7,
			13, 11, 12, 15
	};

	private static int rollDither(int dither, int x) {
		return (dither >> (x & 3)) & 0x1;
	}

	private static int ditherOffset(int x, int y) {
		return x + y * 2;
	}

	public static int lookupColor(int x, int y, int color) {
		long colors = lookupColors(color);
		int closest = (int) (colors & 0xFFFFFFl);
		int secondClosest = (int) (colors >>> 32);
		int dither = ditherLookup[(int) (colorDistance(closest, color) * 16) % 0xF];
		if (rollDither(dither, ditherOffset(x, y)) == 0) {
			return closest;
		} else {
			return secondClosest;
		}
	}
}
