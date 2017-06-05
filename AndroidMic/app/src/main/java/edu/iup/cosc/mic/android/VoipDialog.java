package edu.iup.cosc.mic.android;

import glamb.android.mic.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VoipDialog extends Dialog {
	private MicManager manager;
	private TextView status;

	public VoipDialog(Context context, MicManager manager) {
		super(context);
		this.manager = manager;
		
		setContentView(R.layout.voip_dialog);
		setTitle("Microphone");

		status = (TextView) findViewById(R.id.status);
		ImageView image = (ImageView) findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_launcher);

		Button dialogButton = (Button) findViewById(R.id.cancelButton);
		dialogButton.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				VoipDialog.this.manager.kill();
			}
		});
	}
	
	public void setStatus(String text) {
		status.setText(text);
	}
}
