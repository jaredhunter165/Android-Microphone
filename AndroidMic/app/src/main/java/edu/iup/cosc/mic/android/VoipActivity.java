package edu.iup.cosc.mic.android;

import glamb.android.mic.R;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class VoipActivity extends Activity {
		
	private MicManager manager;
	private Handler handler;
	private EditText ipBox;
	private EditText nameBox;
	private VoipDialog speakDialog;
	
	public void askQ(View view){
		speakDialog = new VoipDialog(this, manager);

		speakDialog.show();
		
    	manager.askQ(ipBox.getText().toString(), nameBox.getText().toString());
    }
    
    public void close(View view){
    	manager.kill();
    	this.finish(); 
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voip_activity);
        ipBox = ((EditText)findViewById(R.id.ip_address));
        nameBox = ((EditText)findViewById(R.id.userName));
        handler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.what){
        		case S.Mic.RUNNING_FULL:
        			if (speakDialog != null) {
        				speakDialog.setStatus("Speak Now");
        			}
        			break;
        		case S.Mic.READY:
        			if (speakDialog != null) {
         				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        				v.vibrate(50);
        			}
        			break;
        		case S.Mic.WAITING_FOR_READY:
        			if (speakDialog != null) {
        				speakDialog.setStatus("Waiting for Connection...");
        			}
        			break;
        		case S.Mic.STOPPED:
        			if (speakDialog != null) {
        				speakDialog.dismiss();
        				speakDialog = null;
        			}
        			break;
        		}
        	}
        };
        
        manager = new MicManager(handler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_voip, menu);
        return true;
    }   
}
