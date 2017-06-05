package edu.iup.cosc.mic.android;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import static android.os.Process.THREAD_PRIORITY_AUDIO;
import static android.os.Process.THREAD_PRIORITY_URGENT_AUDIO;

public class MicThread extends Thread {
	private MicManager micManager;
	private AudioRecord recorder;
	private boolean running = true;

	private static int[] mSampleRates = new int[] {  44100 };
	private static short[] encodings = new short[] { AudioFormat.ENCODING_PCM_16BIT };
	private static short[] channels = new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO };

	public MicThread(MicManager micManager) {
		this.micManager = micManager;
	}

	public boolean initRecorder() {
		for (int rate : mSampleRates) {
			for (short audioFormat : encodings) {
				for (short channelConfig : channels) {
					try {
						Log.d("AudioReader", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
								+ channelConfig);
						int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat) * 2 ;

						if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
							// check if we can instantiate and have a success
							recorder = new AudioRecord(AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

							if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
								Log.d("AudioReader", "Connected rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
										+ channelConfig);
								return true;
							} else {
								recorder = null;
							}
						}
					} catch (Exception e) {
//	                    Log.e("AudioReader", rate + "Exception, keep trying.",e);
					}
				}
			}
		}
		return false;
	}

	public int getBufferSize() {
		return AudioRecord.getMinBufferSize(recorder.getSampleRate(), recorder.getChannelConfiguration(), recorder.getAudioFormat());
	}

	public int getSampleRate() {
		return recorder.getSampleRate();
	}

	public int getChannelConfiguration() {
		return recorder.getChannelConfiguration();
	}

	public int getAudioFormat() {
		return recorder.getAudioFormat();
	}

	public void run(){
		byte[] buffer = new byte[getBufferSize() ];
		int pNo = 0;

		if(recorder.getState() == AudioRecord.STATE_INITIALIZED){
			recorder.startRecording();

			while(running){
				recorder.read(buffer, 0, buffer.length );
				ByteBuffer.wrap(buffer).putInt(pNo++).putLong(System.currentTimeMillis());

				try {
					micManager.send(buffer);
				} catch (IOException e) {
					micManager.kill();
				}
			}

			recorder.stop();
			recorder.release();
			recorder = null;
		}
	}

	public void kill(){
		if (!running) {
			return;
		}

		running = false;
	}
}
