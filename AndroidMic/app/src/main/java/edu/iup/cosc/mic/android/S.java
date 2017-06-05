package edu.iup.cosc.mic.android;

import android.os.Message;

public class S {
	public static class Net {
		public static final int PORT = 6060;
		public static final long TIMEOUT = 5000;
	}
	
	public static class Mic {
		public static Message msg = new Message();
		public static final int STOPPED = 0;
		public static final int RUNNING_FULL = 1;
		public static final int WAITING_FOR_READY = 2;
		public static final int READY = 3;
	}
}
