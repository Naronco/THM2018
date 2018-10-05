package com.deviotion.ld.eggine.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sound {
	private Clip clip;

	public Sound(File file) throws Exception {
		this.clip = AudioSystem.getClip();

		AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);

		clip.open(inputStream);
	}

	public static List<AudioFormat> getSupportedAudioFormats() {
		List<AudioFormat> result = new ArrayList<AudioFormat>();
		for(Line.Info info : AudioSystem.getSourceLineInfo(
				new Line.Info(SourceDataLine.class))) {
			if(info instanceof SourceDataLine.Info) {
				result.addAll(Arrays.asList(
						((SourceDataLine.Info)info).getFormats()));
			}
		}
		return result;
	}

	public void play() {
		if (clip.isActive() || clip.isRunning()) {
			clip.stop();
			clip.flush();
		}
		clip.setMicrosecondPosition(0);
		clip.start();
	}

	public void playInfinitely() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}

	public Clip getClip() { return clip; }
}
