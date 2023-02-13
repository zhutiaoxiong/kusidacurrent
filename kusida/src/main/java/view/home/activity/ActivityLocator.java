package view.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.client.proj.kusida.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.jetbrains.annotations.NotNull;

import view.home.fragment.LocatorListFragment;
import view.home.fragment.LocatorLocationFragment;
import view.home.fragment.LocatorMessageFragment;

public class ActivityLocator extends AllActivity {
    private BottomNavigationView mBottomNavigationView;

    //2个Fragment
    private Fragment locatorLocationFragment;
    //2个Fragment
    private Fragment locatorListFragment;
    private Fragment locatorMessageFragment;

    //标记当前显示的Fragment
    private int mFragmentId = 0;

    // 标记四个Fragment
    private static final int FRAGMENT_LocatorLocation = 0;
    private static final int FRAGMENT_LocatorList = 1;
    private static final int FRAGMENT_LocatorMessage = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        initBottomNavigation();
        if(savedInstanceState==null){
            setFragment(FRAGMENT_LocatorLocation);
        }else{
            //恢复销毁前显示的Fragment
            setFragment(savedInstanceState.getInt("fragment_id"));
        }
    }
    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(locatorLocationFragment == null && fragment instanceof LocatorLocationFragment){
            locatorLocationFragment =  (LocatorLocationFragment)fragment;
        }else if(locatorListFragment == null && fragment instanceof LocatorListFragment){
            locatorListFragment = (LocatorListFragment)fragment;
        }else if(locatorMessageFragment == null && fragment instanceof LocatorMessageFragment){
            locatorMessageFragment = (LocatorMessageFragment)fragment;
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
//        //通过FragmentManager获取保存在FragmentTransaction中的Fragment实例
//        FragmentManager mFragmentManager = getSupportFragmentManager();
//        locatorMainFragment = mFragmentManager.findFragmentByTag("locatormain_fragment");
//        locatorLocationFragment = mFragmentManager.findFragmentByTag("locatorlocation_fragment");
//        locatorListFragment = mFragmentManager.findFragmentByTag("locatorlist_fragment");
//        locatorMessageFragment = mFragmentManager.findFragmentByTag("locatormessage_fragment");
//        //恢复销毁前显示的Fragment
//        if(savedInstanceState!=null){
//            setFragment(savedInstanceState.getInt("fragment_id"));
//        }
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bv_bottomNavigation);
        mBottomNavigationView.setSelectedItemId(R.id.menu_location);
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        // 解决当item大于三个时，非平均布局问题
//        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_main:
                       Intent intent=new Intent(ActivityLocator.this,ActivityMyHome.class);
                       startActivity(intent);
                       finish();
                        break;
                    case R.id.menu_location:
                        setFragment(FRAGMENT_LocatorLocation);
                        break;
                    case R.id.menu_list:
                        setFragment(FRAGMENT_LocatorList);
                        break;
                    case R.id.menu_message:
                        setFragment(FRAGMENT_LocatorMessage);
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
            case FRAGMENT_LocatorLocation:
                mFragmentId = FRAGMENT_LocatorLocation;
                if(locatorLocationFragment == null){
                    locatorLocationFragment = new LocatorLocationFragment();
                    mTransaction.add(R.id.ll_frameLayout, locatorLocationFragment, "locatorlocation_fragment");
                }else {
                    mTransaction.show(locatorLocationFragment);
                }
                break;
            case FRAGMENT_LocatorList:
                mFragmentId = FRAGMENT_LocatorList;
                if(locatorListFragment == null){
                    locatorListFragment = new LocatorListFragment();
                    mTransaction.add(R.id.ll_frameLayout, locatorListFragment, "locatorlist_fragment");
                }else {
                    mTransaction.show(locatorListFragment);
                }
                break;
            case FRAGMENT_LocatorMessage:
                mFragmentId = FRAGMENT_LocatorMessage;
                if(locatorMessageFragment == null){
                    locatorMessageFragment = new LocatorMessageFragment();
                    mTransaction.add(R.id.ll_frameLayout, locatorMessageFragment, "locatormessage_fragment");
                }else {
                    mTransaction.show(locatorMessageFragment);
                }
                break;
        }
        //提交事务
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(locatorLocationFragment != null){
            transaction.hide(locatorLocationFragment);
        }
        if(locatorListFragment != null){
            transaction.hide(locatorListFragment);
        }
        if(locatorMessageFragment != null){
            transaction.hide(locatorMessageFragment);
        }
    }
}
