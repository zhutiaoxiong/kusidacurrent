package view.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.client.proj.kusida.R;

import view.home.commonview.CommonMoreItem;
import view.home.commonview.CommonTitletem;


public class ActivityMore extends AllActivity {
    private CommonTitletem title;
    private CommonMoreItem jiankong;
    private CommonMoreItem guiji;
    private CommonMoreItem mingling;
    private CommonMoreItem dili;
    private CommonMoreItem shebei;
    private CommonMoreItem yuancheng;
    private CommonMoreItem baojing;
    private CommonMoreItem licheng;
    private CommonMoreItem shanchu;
    private static final String TAG="ActivityMore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initView();
        initEvent();
    }
    private void initView(){
        title = findViewById(R.id.titleme);
        jiankong = findViewById(R.id.jiankong);
        guiji = findViewById(R.id.guiji);
        mingling = findViewById(R.id.mingling);
        dili = findViewById(R.id.dili);
        shebei = findViewById(R.id.shebei);
        yuancheng = findViewById(R.id.yuancheng);
        baojing = findViewById(R.id.baojing);
        licheng = findViewById(R.id.licheng);
        shanchu = findViewById(R.id.shanchu);
    }
    private void initEvent(){
        title.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick back" );
                finish();
            }
        });
        jiankong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick jiankong" );
            }
        });
        guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick guiji" );
            }
        });
        mingling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick mingling" );
            }
        });
        dili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick dili" );
            }
        });
        shebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick shebei" );
            }
        });
        yuancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick yuancheng" );
            }
        });
        baojing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick baojing" );
                Intent intent=new Intent(ActivityMore.this,ActivityMessage.class);
                startActivity(intent);
            }
        });
        licheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick licheng" );
            }
        });
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick shanchu" );
            }
        });
    }
}
