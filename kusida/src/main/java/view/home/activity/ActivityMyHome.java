package view.home.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.client.proj.kusida.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import view.home.fragment.MainFragment;
import view.home.fragment.MyFragment;
import view.home.util.HttpInitDataUtils;


public class ActivityMyHome extends AllActivity {
    private BottomNavigationView mBottomNavigationView;
    //2个Fragment
    private Fragment mainFragment;
    private Fragment myFragment;

    //标记当前显示的Fragment
    private int mFragmentId = 0;

    // 标记三个Fragment
    private static final int FRAGMENT_MAIN = 0;
    private static final int FRAGMENT_MY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HttpInitDataUtils.initData();
        initBottomNavigation();
        if(savedInstanceState==null){
            setFragment(FRAGMENT_MAIN);
        }else{
            //恢复销毁前显示的Fragment
            setFragment(savedInstanceState.getInt("fragment_id"));
        }
    }

    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(mainFragment == null && fragment instanceof MainFragment){
            mainFragment =  (MainFragment)fragment;
        }else if(myFragment == null && fragment instanceof MyFragment){
            myFragment = (MyFragment)fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        //通过onSaveInstanceState方法保存当前显示的fragment
        outState.putInt("fragment_id", mFragmentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.e("myhome", "onRestoreInstanceState ");
        //    FragmentManager mFragmentManager = getSupportFragmentManager();
//            mainFragment = mFragmentManager.findFragmentByTag("locatorlocation_fragment");
//            myFragment = mFragmentManager.findFragmentByTag("locatorlist_fragment");
//        if(savedInstanceState!=null){
//            setFragment(savedInstanceState.getInt("fragment_id"));
//        }
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bv_bottomNavigation);
        mBottomNavigationView.setItemIconTintList(null);
        // 解决当item大于三个时，非平均布局问题
        //BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_main:
                       setFragment(FRAGMENT_MAIN);
                        break;
                    case R.id.menu_my:
                        setFragment(FRAGMENT_MY);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }

    private void setFragment(int index){
        //获取Fragment管理器
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        //隐藏所有Fragment
        hideFragments(mTransaction);
        switch (index){
            default:
                break;
            case FRAGMENT_MAIN:
                mFragmentId = FRAGMENT_MAIN;
                if(mainFragment == null){
                    mainFragment = new MainFragment();
                    mTransaction.add(R.id.ll_frameLayout, mainFragment, "main_fragment");
                    Log.e("myhome", "添加第一個 ");
                }else {
                    Log.e("myhome", "顯示第一個 ");
                    mTransaction.show(mainFragment);
                }
                break;
            case FRAGMENT_MY:
                mFragmentId = FRAGMENT_MY;
                if(myFragment == null){
                    myFragment = new MyFragment();
                    mTransaction.add(R.id.ll_frameLayout, myFragment, "my_fragment");
                    Log.e("myhome", "添加第二個 ");
                }else {
                    mTransaction.show(myFragment);
                    Log.e("myhome", "顯示第二個 ");
                }
                break;
        }
        //提交事务
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(mainFragment != null){
            //隐藏Fragment
            Log.e("myhome", "隱藏第一個 ");
            transaction.hide(mainFragment);
        }
        if(myFragment != null){
            Log.e("myhome", "隱藏第二個 ");
            transaction.hide(myFragment);
        }
    }
}
