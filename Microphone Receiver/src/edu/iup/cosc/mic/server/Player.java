package edu.iup.cosc.mic.server;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Player extends Thread{
	private SourceDataLine dataline;
	private DataLine.Info info;
	
	public Player(float sampleRate, int sampleSize, int channels, boolean signed, boolean bigEndian) throws LineUnavailableException {
		AudioFormat format = new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
	       
        info = new DataLine.Info(SourceDataLine.class, format);

        dataline = (SourceDataLine) AudioSystem.getLine(info);
        dataline.open(format);
        dataline.start();
	}
	
	public void play(byte[] buffer, int offset, int length) {
		dataline.write(buffer, offset, length);	
	}
	
	public void close() {
		dataline.drain();
		dataline.close();	
		dataline = null;
	}
}
