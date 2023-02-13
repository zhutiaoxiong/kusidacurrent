package view.clip.child;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;

public class ClipSearchHead extends RelativeLayoutBase{
	private EditText      txt_search;
	private LinearLayout  lin_search;
	public ImageView     img_search;
	public ClipSearchHead(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_favorite_searchhead, this, true);
		txt_search = (EditText) findViewById(R.id.txt_search);
		lin_search = (LinearLayout) findViewById(R.id.lin_search);
		img_search = (ImageView) findViewById(R.id.img_search);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		img_search.setVisibility(INVISIBLE);

	}

	@Override
	protected void initEvents() {
		txt_search.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					lin_search.setVisibility(INVISIBLE);
					img_search.setVisibility(VISIBLE);
				}else{
					lin_search.setVisibility(VISIBLE);
					img_search.setVisibility(INVISIBLE);
				}
			}
		});

	}

	@Override
	protected void invalidateUI() {
	}

	public void setSearchStr(String search){
		txt_search.setText(search);
	}
	// ==================================get=====================================
	public String getSearchStr(){
		return txt_search.getText().toString();
	}
}