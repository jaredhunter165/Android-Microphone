package edu.iup.cosc.mic.server;

public class AudioContent {
	private byte[] content;
	
	public AudioContent(byte[] content) {
		super();
		this.content = content;
	
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}
