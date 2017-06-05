package edu.iup.cosc.mic.android;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Handler;
import android.util.Log;

public class MicManager {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	private DatagramSocket toServerUDP;
	private InetAddress inetAddress;

	private MicThread micThread;

	private Handler statusHandler;

	public MicManager(Handler statusHandler) {
		this.statusHandler = statusHandler;
	}

	public void kill() {
		if (micThread != null) {
			micThread.kill();
			micThread = null;
		}

		statusHandler.sendEmptyMessage(S.Mic.STOPPED);

		try {
			synchronized (toServer) {
				toServer.writeUTF("kill");
			}
		} catch (Exception e) {
		}

		try {
			toServer.close();
		} catch (Exception e) {
		}

		toServer = null;

		try {
			fromServer.close();
		} catch (Exception e) {
		}

		fromServer = null;

		try {
			server.close();
		} catch (Exception e) {
		}

		server = null;

	}

	public void askQ(final String host, final String name) {
		Log.d("runable start", "askQ Started");
		new Thread(new Runnable() {

			public void run() {
				Log.d("run method","trying run method");
				try {
					server = new Socket(host, S.Net.PORT);
					
					inetAddress = server.getInetAddress();
					fromServer = new DataInputStream(server.getInputStream());
					toServer = new DataOutputStream(server.getOutputStream());
					Log.d("inputStream",fromServer.toString());
					Log.d("outputStream",toServer.toString());
					toServer.writeUTF(name);

					statusHandler.sendEmptyMessage(S.Mic.WAITING_FOR_READY);

					if (!fromServer.readUTF().equals("ready")) {
						MicManager.this.kill();
						return;
					}
					
					statusHandler.sendEmptyMessage(S.Mic.READY);
					
					synchronized (toServer) {
						toServer.writeUTF("OK");
					}

					toServerUDP = new DatagramSocket();

					micThread = new MicThread(MicManager.this);

					if (micThread.initRecorder()) {
						toServer.writeInt(micThread.getBufferSize());
						toServer.writeInt(micThread.getSampleRate());
						toServer.writeInt(micThread.getAudioFormat());
						toServer.writeInt(micThread.getChannelConfiguration());

						toServer.flush();

						if (!fromServer.readUTF().equals("go")) {
							MicManager.this.kill();
							return;
						} 

						micThread.start();

						statusHandler.sendEmptyMessage(S.Mic.RUNNING_FULL);

						if (fromServer.readUTF().equals("kill")) {
							MicManager.this.kill();
						}
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					Log.d("MicManager", "Exception on askQ() " + e.getMessage());
					MicManager.this.kill();
				}
			}
		}).start();
	}

	public void send(byte[] buffer) throws IOException {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				inetAddress, 6060);
		toServerUDP.send(packet);
	}

}
