package com.kulala.staticsview.line;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.kulala.staticsview.R;


public class ClipAutoCompText extends AutoCompleteTextView {
	@Override
	protected void onDetachedFromWindow() {
		adapter = null;
		super.onDetachedFromWindow();
	}

	private tipHandler	handler	= null;
	private String[]	strArr;
	private ArrayAdapter<String> adapter;
	public ClipAutoCompText(Context context, AttributeSet attr) {
		super(context, attr);
		handler = new tipHandler();
		strArr = new String[]{};

		// String digit = getResources().getString(R.string.reg_digits);
		// //����R�Զ�����my_attr_digits
		// this.setKeyListener(DigitsKeyListener.getInstance(digit));
		// this.setInputType(InputType.TYPE_CLASS_TEXT);
		initClick();
	}
	private void initClick() {
		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(adapter == null)return;
				adapter.notifyDataSetChanged();
//				handleTextChange(s.toString());
//				ClipAutoCompText.this.showDropDown();
			}
		});
		// ��item
		this.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = parent.getItemAtPosition(position);
				// MyDispatcher.dispatchEvent(OEventName.DROP_SELECT_ITEM,
				// obj.toString());
			}
		});
	}

	// ===============================set===================================
	public void setData(String[] arr) {
		this.setAdapter(null);
		if (arr == null)return;
		this.strArr = arr;
		setAutoComplete(arr);
	}
	/**设了最大四行**/
	private void setAutoComplete(String[] arr) {
//		String[] likeArr4;
//		if (arr.length > 4) {
//			likeArr4 = new String[]{arr[0], arr[1], arr[2], arr[3]};
//		} else {
//			likeArr4 = arr;
//		}
		adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_onlytext, arr);
		this.setAdapter(adapter);
//		 this.setDropDownHeight(180);
		this.setThreshold(1);// 打多少字后自动提示
		this.setMaxLines(4);
		// this.setCompletionHint("最近输入的记录...");
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView view = (AutoCompleteTextView) v;
				if (hasFocus) {
					if (ClipAutoCompText.this.strArr.length > 0)
						view.showDropDown();
				}
			}
		});
	}
	// ==================================================================
	public void handleTextChange(String str) {
		Message message = new Message();
		message.what = 1;
		message.obj = str;
		handler.sendMessage(message);
	}

	@SuppressLint({"HandlerLeak"})
	class tipHandler extends Handler {
		tipHandler() {
		}

		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 1 :
						String str = (String) msg.obj;
						if (ClipAutoCompText.this.strArr != null || ClipAutoCompText.this.strArr.length > 0) {
							String[] likeArr = StringArrCheckLike(ClipAutoCompText.this.strArr, str);
								setAutoComplete(likeArr);
						}
						break;
				}
			} catch (Exception localException) {
			}
		}
	}
	// ==================================================================
	/** String[] 检测相似 **/
	public static String[] StringArrCheckLike(String[] arr, String likeValue) {
		String str = "";
		char[] chr = likeValue.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			String strObj = arr[i];
			char[] objChr = strObj.toCharArray();
			boolean isLike = true;
			for (int j = 0; j < chr.length; j++) {
				if (chr[j] != objChr[j]) {
					isLike = false;
				}
			}
			if (isLike == true) {
				str += arr[i];
				if (i < arr.length - 1)
					str += "*#*#753#*#*";
			}
		}
		return str.split("\\*#*#753#*#*");
	}
}