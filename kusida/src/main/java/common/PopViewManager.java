package common;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.client.proj.kusida.R;
import annualreminder.view.ViewAnnualReminderMain;
import com.wearkulala.www.wearfunc.ViewWear;

import view.ActivityChat;
import view.ViewBuyCar;
import view.clip.gesture.GestureEditPage;
import view.clip.gesture.GestureVerityPage;
import view.find.ViewProSet;
import view.loginreg.ViewVerificationCodeHow;
import view.safetyneed.ViewResetByPhone;
import view.safetyneed.ViewSafetyReply;
import view.safetyneed.ViewSafetyResetAderess;
import view.safetyneed.ViewSafetyResetItem;
import view.safetyneed.ViewSafetyResetPassword;
import view.safetyneed.ViewSafetyResetPhone;
import view.safetyneed.ViewSafetyResetQustion;
import view.view4app.ViewCodriverAuthorization;
import view.view4app.ViewCodriverManage;
import view.view4app.ViewGpsFavorite;
import view.view4app.ViewGpsPathList;
import view.view4app.ViewGpsPathShow;
import view.view4app.ViewQrcode;
import view.view4app.ViewUserList;
import view.view4app.ViewViolationCarList;
import view.view4app.ViewViolationDetail;
import view.view4app.ViewViolationVioList;
import view.view4app.ViewWarning;
import view.view4app.carpath.AppPathFind;
import view.view4app.carpath.ViewCarPosCollect;
import view.view4app.carpath.ViewGpsPathCollectList;
import view.view4app.carpath.ViewHomePosChoose;
import view.view4app.carpath.ViewHomePosSet;
import view.view4app.carpath.ViewNaviMain;
import view.view4app.carpath.ViewNaviSearch;
import view.view4app.codriver.ViewCodriverList;
import view.view4app.maintain.ViewAddMaintain;
import view.view4app.maintain.ViewFirstIn;
import view.view4app.maintain.ViewMaintain;
import view.view4control.ViewTreaSureGet;
import view.view4info.ViewCarDressUp;
import view.view4info.ViewDownLoadManager;
import view.view4info.ViewFindForLookBigPic;
import view.view4info.ViewFindPay;
import view.view4info.ViewInfoList;
import view.view4info.ViewSelectCar;
import view.view4info.ViewTouchIn;
import view.view4info.card.ViewCollectAchievement;
import view.view4info.temcontrol.ViewTemControl;
import view.view4me.ViewAbout;
import view.view4me.ViewAboutIntro;
import view.view4me.ViewAboutLicence;
import view.view4me.ViewAboutSuggest;
import view.view4me.ViewContactus;
import view.view4me.ViewMeHelp;
import view.view4me.ViewMeHelpLookPic;
import view.view4me.ViewMeHelpLookText;
import view.view4me.ViewMeLanguage;
import view.view4me.ViewMessageUser;
import view.view4me.ViewMichelle;
import view.view4me.ViewPay;
import view.view4me.ViewPayFriend;
import view.view4me.ViewSafety;
import view.view4me.ViewSetup;
import view.view4me.ViewSetupFindPassWord;
import view.view4me.ViewSwitchConfirm;
import view.view4me.ViewSwitchMantance;
import view.view4me.ViewSwitchMessage;
import view.view4me.ViewSwitchPrivate;
import view.view4me.ViewSwitchVoice;
import view.view4me.ViewWallet;
import view.view4me.View_RechargeVouchers;
import view.view4me.carmanage.ViewCarModify;
import view.view4me.carmanage.ViewCarNew;
import view.view4me.carmanage.ViewCarmanBlue;
import view.view4me.carmanage.ViewCarmanBlueNew;
import view.view4me.carmanage.ViewCarmanInfoDetail;
import view.view4me.carmanage.ViewCarmanMain;
import view.view4me.carmanage.ViewCarmanModel;
import view.view4me.carmanage.ViewCarmanModelBind;
import view.view4me.carmanage.ViewCarmanModelUnBind;
import view.view4me.nfcmoudle.ViewNfc;
import view.view4me.nfcmoudle.ViewNfcBlue;
import view.view4me.nfcmoudle.ViewNfcBlueId;
import view.view4me.nfcmoudle.ViewNfcId;
import view.view4me.shake.ViewNoKey;
import view.view4me.shake.ViewSwitchShake;
import view.view4me.shake.ViewSwitchShakeBind;
import view.view4me.shake.ViewSwitchShakePhone;
import view.view4me.shake.ViewSwitchShakeProduct;
import view.view4me.shake.ViewSwitchShakeSet;
import view.view4me.shake.ViewSwitchShakeSetTxt;
import view.view4me.shake.ViewSwitchShakeTips;

public class PopViewManager {
    private PopViewManager() {
    }

    public static ViewGroup findViewByResId(Context parent, int resId) {
        switch (resId) {
            case R.layout.activity_chat:
                Intent intent = new Intent();
                intent.setClass(parent, ActivityChat.class);
                parent.startActivity(intent);
                return null;
            //手势密码
            case R.layout.clip_gesture_edit:
                return (new GestureEditPage(parent, null));
            case R.layout.clip_gesture_verify:
                return (new GestureVerityPage(parent, null));
            //密保问题
            //车应用

            case R.layout.gps_path_collect_list:
                return (new ViewGpsPathCollectList(parent, null));
            case R.layout.view_annual_reminder_main:
                return (new ViewAnnualReminderMain(parent, null));
            case R.layout.view_violation_carlist:
                return (new ViewViolationCarList(parent, null));
            case R.layout.view_app_warnings:
                return (new ViewWarning(parent, null));
            case R.layout.view_violation_violist:
                return (new ViewViolationVioList(parent, null));
            case R.layout.view_violation_detail:
                return (new ViewViolationDetail(parent, null));
            case R.layout.view_app_codriver:
                return (new ViewCodriverManage(parent, null));
            case R.layout.view_app_codriver_authorization:
                return (new ViewCodriverAuthorization(parent, null));
            case R.layout.view_app_codriverlist:
                return (new ViewCodriverList(parent, null));
            case R.layout.view_apppath_find:
                return (new AppPathFind(parent, null));
            case R.layout.view_app_userlist:
                return (new ViewUserList(parent, null));
            case R.layout.view_app_qrcode:
                return (new ViewQrcode(parent, null));
            case R.layout.view_app_gps_path_list:
                return (new ViewGpsPathList(parent, null));
            case R.layout.view_app_gps_favorite:
                return (new ViewGpsFavorite(parent, null));
            case R.layout.view_app_gps_path_show:
                return (new ViewGpsPathShow(parent, null));
            case R.layout.view_maintance_remind_me:
                return (new ViewMaintain(parent, null));
            case R.layout.view_recharge_vouchers:
                return (new View_RechargeVouchers(parent, null));
            case R.layout.view_car_maintenance_remind:
                return (new ViewFirstIn(parent, null));
            case R.layout.view_add_maintain:
                return (new ViewAddMaintain(parent, null));
            //导航
            case R.layout.view_navi_main:
                return (new ViewNaviMain(parent, null));
            case R.layout.view_navi_search:
                return (new ViewNaviSearch(parent, null));
            case R.layout.view_navi_home_pos_set:
                return (new ViewHomePosSet(parent, null));
            case R.layout.view_navi_home_pos_choose:
                return (new ViewHomePosChoose(parent, null));
            case R.layout.view_car_pos_collect:
                return (new ViewCarPosCollect(parent, null));
            //发现
            case R.layout.view_info_infolist:
                return (new ViewInfoList(parent, null));
            case R.layout.view_find_pay_for_skinderm:
                return (new ViewFindPay(parent, null));
            case R.layout.view_find_downloadmanager:
                return (new ViewDownLoadManager(parent, null));
            case R.layout.view_find_select_car:
                return (new ViewSelectCar(parent, null));
            case R.layout.view_find_car_dressup:
                return (new ViewCarDressUp(parent, null));
            case R.layout.view_find_for_look_bigpic:
                return (new ViewFindForLookBigPic(parent, null));
            case R.layout.view_collection_achievement:
                return (new ViewCollectAchievement(parent, null));
            //打开宝箱的页面
            case R.layout.treasure_get:
                return (new ViewTreaSureGet(parent, null));
            //我
            case R.layout.carman_main:
                return (new ViewCarmanMain(parent, null));
            case R.layout.carman_info_detail:
                return (new ViewCarmanInfoDetail(parent, null));
            case R.layout.carman_model_set:
                return (new ViewCarmanModel(parent, null));
            case R.layout.carman_model_set_bind:
                return (new ViewCarmanModelBind(parent, null));
            case R.layout.carman_model_set_unbind:
                return (new ViewCarmanModelUnBind(parent, null));
            case R.layout.carman_blue_set:
                return (new ViewCarmanBlue(parent, null));
            case R.layout.carman_blue_set_new:
                return (new ViewCarmanBlueNew(parent, null));
            case R.layout.view_me_message_user:
                return (new ViewMessageUser(parent, null));
            case R.layout.view_me_newcar:
                return (new ViewCarNew(parent, null));
            case R.layout.view_me_carinfo_modify:
                return (new ViewCarModify(parent, null));
//            case R.layout.view_me_userinfo:
//                return (new ViewUserInfo(parent, null));
            case R.layout.view_me_switch_message:
                return (new ViewSwitchMessage(parent, null));
            case R.layout.view_me_switch_voice:
                return (new ViewSwitchVoice(parent, null));
            case R.layout.view_me_switch_confirm:
                return (new ViewSwitchConfirm(parent, null));
            case R.layout.view_me_switch_private:
                return (new ViewSwitchPrivate(parent, null));
            case R.layout.view_me_switch_shake:
                return (new ViewSwitchShake(parent, null));
            case R.layout.view_me_switch_set:
                return (new ViewSwitchShakeSet(parent, null));
            case R.layout.view_me_switch_set_txt:
                return (new ViewSwitchShakeSetTxt(parent, null));
            case R.layout.view_me_switch_tips:
                return (new ViewSwitchShakeTips(parent, null));
            case R.layout.view_me_switch_phone:
                return (new ViewSwitchShakePhone(parent, null));
            case R.layout.view_me_switch_product:
                return (new ViewSwitchShakeProduct(parent, null));
            case R.layout.view_me_switch_bind:
                return (new ViewSwitchShakeBind(parent, null));
//            case R.layout.view_me_nokey:
//                return (new ViewNoKey(parent, null));
            case R.layout.view_me_nokey_new:
                return (new ViewNoKey(parent, null));
            case R.layout.view_me_touch_in:
                return (new ViewTouchIn(parent, null));
            case R.layout.view_me_nfc:
                return (new ViewNfc(parent, null));
            case R.layout.view_me_nfc_blue:
                return (new ViewNfcBlue(parent, null));
            case R.layout.view_tem_contorl:
                return (new ViewTemControl(parent, null));
            case R.layout.view_me_nfc_id:
                return (new ViewNfcId(parent, null));
            case R.layout.view_me_nfc_blue_id:
                return (new ViewNfcBlueId(parent, null));
            case R.layout.view_me_wear:
                return (new ViewWear(parent, null));

            case R.layout.view_me_setup:
                return (new ViewSetup(parent, null));
            case R.layout.view_me_safety:
                return (new ViewSafety(parent, null));
            case R.layout.view_me_wallet:
                return (new ViewWallet(parent, null));
            case R.layout.view_me_pay:
                return (new ViewPay(parent, null));
            case R.layout.view_me_pay_firend:
                return (new ViewPayFriend(parent, null));
            case R.layout.view_me_contactus:
                return (new ViewContactus(parent, null));
            case R.layout.view_me_language:
                return (new ViewMeLanguage(parent, null));
            case R.layout.view_me_switch_mantance:
                return (new ViewSwitchMantance(parent, null));
            case R.layout.view_me_help:
                return (new ViewMeHelp(parent, null));
            case R.layout.view_me_help_lookpic:
                return (new ViewMeHelpLookPic(parent, null));
            case R.layout.view_me_help_looktext:
                return (new ViewMeHelpLookText(parent, null));
//            case R.layout.view_me_chat:
//                return(new ViewChat(parent, null));
            case R.layout.view_me_about:
                return (new ViewAbout(parent, null));
            case R.layout.view_me_about_intro:
                return (new ViewAboutIntro(parent, null));
            case R.layout.view_me_about_licence:
                return (new ViewAboutLicence(parent, null));
            case R.layout.view_me_about_suggest:
                return (new ViewAboutSuggest(parent, null));
            case R.layout.view_me_share:
                return (new ViewMichelle(parent, null));
            case R.layout.view_me_findpasswordway:
                return (new ViewSetupFindPassWord(parent, null));
            case R.layout.view_loginreg_verificationcode_how:
                return (new ViewVerificationCodeHow(parent, null));
            case R.layout.view_safety_resetitem:
                return (new ViewSafetyResetItem(parent, null));//
            case R.layout.view_safety_reset_byphone:
                return (new ViewResetByPhone(parent, null));
            case R.layout.view_safety_reset_check_reply:
                return (new ViewSafetyReply(parent, null));
            case R.layout.view_safety_reset_phone:
                return (new ViewSafetyResetPhone(parent, null));//
            case R.layout.view_safety_reset_password:
                return (new ViewSafetyResetPassword(parent, null));
            case R.layout.view_safety_reset_aderess:
                return (new ViewSafetyResetAderess(parent, null));
            case R.layout.view_safety_reset_qustion:
                return (new ViewSafetyResetQustion(parent, null));
            case R.layout.buy_car:
                return (new ViewBuyCar(parent, null));
            case R.layout.view_project_set:
                return (new ViewProSet(parent, null));
        }
        return null;
    }
}
