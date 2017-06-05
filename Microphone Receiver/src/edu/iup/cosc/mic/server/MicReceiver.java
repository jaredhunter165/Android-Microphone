package edu.iup.cosc.mic.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

import edu.iup.cosc.mic.server.ui.MicManagerFrame;

public class MicReceiver extends Thread {
	private Socket client;
	private String name;
	private DataInputStream fromClient;
	private DataOutputStream toClient;
	private DatagramSocket fromClientUDP;
	private boolean running;
	private int bufferSize;
	private int sampleRate;
	private int audioFormat;
	private int channelConfiguation;
	private byte[] buffer;
	private Player qPlayer;
	private Thread killThread;
	private MicManagerFrame serverFrame;
	private ByteArrayOutputStream byteOutputStream;
	private AudioFormat adFormat;
	private TargetDataLine targetDataLine;
	private AudioInputStream InputStream;
	private SourceDataLine sourceLine;
	private boolean isReady = false;
	private int read = 0;
	private int ai = 0; 
	private AudioContent[] ac = new AudioContent[100];
	

	public MicReceiver(MicManagerFrame serverFrame, Socket client)
			throws IOException {
		this.serverFrame = serverFrame;
		this.client = client;

		fromClient = new DataInputStream(client.getInputStream());
		toClient = new DataOutputStream(client.getOutputStream());

		name = fromClient.readUTF();

		killThread = new Thread(new Runnable() {
			public void run() {
				try {
					synchronized (fromClient) {
						String msg = fromClient.readUTF();
						if (msg.equals("kill")) {
							MicReceiver.this.serverFrame
									.removeReceiver(MicReceiver.this);
						}
					}
					killThread = null;
				} catch (IOException e) {
					MicReceiver.this.serverFrame
							.removeReceiver(MicReceiver.this);
				}
			}
		});
		killThread.start();

	}

	public String toString() {
		return name;
	}

	public void activate() throws IOException, LineUnavailableException {
		System.out.println("Activing");

		toClient.writeUTF("ready");
		toClient.flush();

		fromClientUDP = new DatagramSocket(S.Net.PORT);

		synchronized (fromClient) {
			bufferSize = fromClient.readInt();
			sampleRate = fromClient.readInt();
			audioFormat = fromClient.readInt();
			channelConfiguation = fromClient.readInt();
		}
		
		System.out
				.printf("Audio data: bufferSize=%d rate=%d format=%d channelConfig=%d\n",
						bufferSize, sampleRate, audioFormat,
						channelConfiguation);

		System.out.println(bufferSize);
		buffer = new byte[bufferSize ];

		float sampleRate = (float)this.sampleRate;
		int sampleSize = audioFormat == 2 ? 16 : audioFormat == 3 ? 8 : 0;
		int channels = channelConfiguation == 16 ? 1
				: channelConfiguation == 12 ? 2 : 0;
		boolean signed = true;
		boolean bigEndian = false;

		System.out.println("sampleRate = " + sampleRate + " sampleSize = " + sampleSize + 
						"channelse = " + channels);
		
		qPlayer = new Player(sampleRate, sampleSize, channels, signed,
				bigEndian);

		this.setPriority(MAX_PRIORITY);

		start();

		System.out.println("sending go");
		toClient.writeUTF("go");
		toClient.flush();

		killThread = new Thread(new Runnable() {
			public void run() {
				try {
					if (fromClient.readUTF().equals("kill")) {
						MicReceiver.this.kill();
					}
				} catch (IOException e) {
				}
			}
		});
		killThread.start();

	}
	
	private AudioFormat getAudioFormat() {
		float sampleR = (float)sampleRate;
		int sampleInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		//System.out.println("samp = " + sampleR + " , Channel = " + channels + " signed = " + signed + " bigEndian = " +  bigEndian);
		return new AudioFormat(sampleR, sampleInBits, channels, signed, bigEndian);
	}
	//audio index
	public void run() {
		running = true;
		//int lastNo = -1;
		
		try {
			byte[] receiveData = new byte[bufferSize * 2];
			
			//New Stuff
			while (running) {
				DatagramPacket packet = new DatagramPacket(receiveData,
						receiveData.length);
				
				fromClientUDP.receive(packet);
				
				try{
					byte audioData[] = packet.getData();
					
					
//						ac[ai] = new AudioContent(audioData);
//						ai++;
//						if(ai == ac.length){
//							ai = 0;
//						}
						
					//if(isReady == true || ai == 50){
						
						InputStream byteInputStream = new ByteArrayInputStream(audioData);
						AudioFormat adFormat = getAudioFormat();
						InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
						DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
						sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
						sourceLine.open(adFormat);
						sourceLine.start();
						Thread playThread = new Thread(new PlayThread());
						playThread.start();
						isReady = true;
						read++;
						if(read == ac.length){
							read = 0;
						}
					
					
				} catch (Exception e) {
					System.out.println(e);
				}
//				ByteBuffer bb = ByteBuffer.wrap(buffer);
//				int pNo = bb.getInt();
//				long ts = bb.getLong();
//
//				System.out.printf("%d %d\n", pNo, ts);
//
//				if (pNo >= lastNo) {
//					qPlayer.play(buffer, 0, buffer.length );
//					lastNo = pNo;
//				}
			}
		} catch (IOException e) {
			kill();
		}
	}

	public void kill() {
		running = false;

		try {
			if (SwingUtilities.isEventDispatchThread()) {
				serverFrame.clearSpeaker(MicReceiver.this);
			} else {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						serverFrame.clearSpeaker(MicReceiver.this);

					}
				});
			}
		} catch (Exception e) {
		}

		try {
			if (fromClientUDP != null) {
				fromClientUDP.close();
				fromClientUDP = null;
			}
		} catch (Exception e) {
		}

		try {
			if (killThread != null) {
				killThread.interrupt();
				killThread = null;
			}
		} catch (Exception e) {
		}

		try {
			qPlayer.close();
		} catch (Exception e) {
		}

		try {
			toClient.writeUTF("kill");
		} catch (Exception e) {
		}

		try {
			toClient.close();
		} catch (Exception e) {
		}

		try {
			fromClient.close();
		} catch (Exception e) {
		}

		try {
			client.close();
		} catch (Exception e) {
		}
	}

		
private class PlayThread extends Thread {

	byte tempBuffer[] = new byte[bufferSize * 2];
		public void run() {
			try {
		            int cnt;
		            while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
		                System.out.println(cnt);
		            	if (cnt > 0) {
		                    sourceLine.write(tempBuffer, 0, cnt);
		                }
		            }
		            sourceLine.drain();
		            sourceLine.close();
		        } catch (Exception e) {
		            System.out.println(e);
		            System.exit(0);
		        }
		    }
		}
		}
