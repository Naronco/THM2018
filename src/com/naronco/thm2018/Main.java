package com.naronco.thm2018;

import com.deviotion.ld.eggine.sound.Sound;
import org.urish.openal.ALException;
import org.urish.openal.OpenAL;
import org.urish.openal.Source;

import java.io.File;

public class Main {
	public static void main(String[] args) throws Exception {
		OpenAL openal = null;
		try {
			openal = new OpenAL();
			openal.getDevice().checkForError();
		} catch (ALException ex) {
			ex.printStackTrace();
		}

		new Game(openal).start();

		if (openal != null)
			openal.close();
	}
}
