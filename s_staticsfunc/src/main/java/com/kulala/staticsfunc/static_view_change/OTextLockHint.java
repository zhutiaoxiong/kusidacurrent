package com.kulala.staticsfunc.static_view_change;

import android.view.View;
import android.widget.EditText;

/**
 * @author Administrator
 *
 */
public class OTextLockHint {

	public static void setEditTextLock(EditText txt)
	{ 
		txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){//
            	EditText txtt = ((EditText)v);
            	String settext = "";
            	if(txtt.getText().toString().contains(txtt.getHint()))
            	{
            		String[] arr = new String[0];
            		arr = txtt.getText().toString().split(txtt.getHint().toString());
            		if(arr.length>1)settext = arr[1];
            	}
            		txtt.setText(settext);
            }else{//���ʧȥ�˽���
            	EditText txtt = ((EditText)v);
            	if(!txtt.getText().toString().contains(txtt.getHint()) && !txtt.getText().toString().equals(""))
            		txtt.setText(txtt.getHint()+txtt.getText().toString());
            }
        }
    });
	}
	public static String getEditTextInfo(EditText txt)
	{ 
    	if(txt.getText().toString().contains(txt.getHint()))
    	{
    		String[] arr = txt.getText().toString().split(txt.getHint().toString());
    		if(arr.length>1)return arr[1];
    	}
    	return txt.getText().toString();
	}
}
