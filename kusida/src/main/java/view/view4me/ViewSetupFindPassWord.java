package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

import ctrl.OCtrlRegLogin;
import model.ManagerAnswer;
import model.answer.DataFindway;
import view.view4me.set.ClipTitleMeSet;

public class  ViewSetupFindPassWord extends LinearLayoutBase {
    private ClipTitleMeSet titleHead;
    private RelativeLayout find_by_phone_layout;
    private RelativeLayout find_by_problem_layout;
    private RelativeLayout find_by_aderess_layout;
    private ImageView find_by_phone_iv;
    private ImageView find_by_problem_iv;
    private ImageView find_by_aderess_iv;
    private ImageView crossline1, crossline2, crossline3;
    private boolean[] isSelect = new boolean[]{true, false, false};

    public ViewSetupFindPassWord(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_findpasswordway, this, true);
        titleHead = (ClipTitleMeSet) findViewById(R.id.title_head);
        find_by_phone_layout = (RelativeLayout) findViewById(R.id.view_me_findpasswordway_by_phone_layout);
        find_by_problem_layout = (RelativeLayout) findViewById(R.id.view_me_findpasswordway_by_problem_layout);
        find_by_aderess_layout = (RelativeLayout) findViewById(R.id.view_me_findpasswordway_by_aderess_layout);
        find_by_phone_iv = (ImageView) findViewById(R.id.view_me_findpasswordway_by_phone_iv);
        find_by_problem_iv = (ImageView) findViewById(R.id.view_me_findpasswordway_by_problem_iv);
        find_by_aderess_iv = (ImageView) findViewById(R.id.view_me_findpasswordway_by_aderess_iv);
        crossline1 = (ImageView) findViewById(R.id.crossline_1);
        crossline2 = (ImageView) findViewById(R.id.crossline_2);
        crossline3 = (ImageView) findViewById(R.id.crossline_3);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GET_FIND_PASSWORD_WAY, this);
    }

    @Override

    protected void initViews() {
        OCtrlRegLogin.getInstance().ccmd1122_get_findpassword_way();
//        handleChangeData();
    }

    @Override
    protected void initEvents() {
        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (isSelect[0] == true) {
                    if (isSelect[1] == false && isSelect[2] == false) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{1});
                    } else if (isSelect[1] == true && isSelect[2] == false) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{1, 2});
                    } else if (isSelect[1] == false && isSelect[2] == true) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{1, 3});
                    } else if (isSelect[1] == true && isSelect[2] == true) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{1, 2, 3});
                    }
                } else if (isSelect[1] == true) {
                    if (isSelect[2] == false) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{2});
                    } else if (isSelect[2] == true) {
                        OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{2, 3});
                    }
                } else if (isSelect[2] == true) {
                    OCtrlRegLogin.getInstance().ccmd1121_select_findpassword_way(new Integer[]{3});
                }
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
            }
        });
        find_by_phone_layout.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (isSelect[0] == true) {
                    if (isSelect[1] == false && isSelect[2] == false) {
                        find_by_phone_iv.setImageResource(R.drawable.car_set_on);
                        //判断另外两个
                    } else {
                        isSelect[0] = false;
                        find_by_phone_iv.setImageResource(R.drawable.car_set_off);
                    }
                } else {
                    isSelect[0] = true;
                    find_by_phone_iv.setImageResource(R.drawable.car_set_on);
                }
            }
        });
        find_by_aderess_layout.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (isSelect[1] == true) {
                    if (isSelect[0] == false && isSelect[2] == false) {
                        find_by_aderess_iv.setImageResource(R.drawable.car_set_on);
                        //判断另外两个
                    } else {
                        isSelect[1] = false;
                        find_by_aderess_iv.setImageResource(R.drawable.car_set_off);
                    }
                } else {
                    isSelect[1] = true;
                    find_by_aderess_iv.setImageResource(R.drawable.car_set_on);
                }
            }
        });
        find_by_problem_layout.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                if (isSelect[2] == true) {
                    if (isSelect[0] == false && isSelect[1] == false) {
                        find_by_problem_iv.setImageResource(R.drawable.car_set_on);
                        //判断另外两个
                    } else {
                        isSelect[2] = false;
                        find_by_problem_iv.setImageResource(R.drawable.car_set_off);
                    }
                } else {
                    //2
                    isSelect[2] = true;
                    find_by_problem_iv.setImageResource(R.drawable.car_set_on);
                }
            }
        });

    }


    @Override
    protected void invalidateUI() {
        List<DataFindway> list = ManagerAnswer.getInstance().secretTypeslist;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).ide == 1) {
                    find_by_phone_layout.setVisibility(View.VISIBLE);
//                    crossline1.setVisibility(View.GONE);
                    if (list.get(i).isSelect == 1) {
                        find_by_phone_iv.setImageResource(R.drawable.car_set_on);
                        isSelect[0] = true;
                    } else {
                        find_by_phone_iv.setImageResource(R.drawable.car_set_off);
                        isSelect[0] = false;
                    }
                }
                if (list.get(i).ide == 2) {
                    find_by_aderess_layout.setVisibility(View.VISIBLE);
//                    crossline2.setVisibility(View.GONE);
                    if (list.get(i).isSelect == 1) {
                        find_by_aderess_iv.setImageResource(R.drawable.car_set_on);
                        isSelect[1] = true;
                    } else {
                        find_by_aderess_iv.setImageResource(R.drawable.car_set_off);
                        isSelect[1] = false;
                    }
                }
//            else {
//                find_by_aderess_layout.setVisibility(View.GONE);
//                crossline2.setVisibility(View.GONE);
//            }
                if (list.get(i).ide == 3) {
                    find_by_problem_layout.setVisibility(View.VISIBLE);
                    crossline3.setVisibility(View.GONE);
                    if (list.get(i).isSelect == 1) {
                        find_by_problem_iv.setImageResource(R.drawable.car_set_on);
                        isSelect[2] = true;
                    } else {
                        find_by_problem_iv.setImageResource(R.drawable.car_set_off);
                        isSelect[2] = false;
                    }
                }
            }

        }
    }


    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.GET_FIND_PASSWORD_WAY)) {
           handleChangeData();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.GET_FIND_PASSWORD_WAY, this);
        super.onDetachedFromWindow();
    }
}
