package common.pinyinzhuanhuan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.loginreg.DataUser;

/**
 * Created by qq522414074 on 2016/7/11.
 */
public class GetContacts {
    public Map<String, String> getLocalContactInfo(Context context) {
        Map<String, String> map = new HashMap<String, String>();
        ContentResolver cr = context.getContentResolver();
        String str[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        Cursor cur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, str, null,
                null, null);
        if (cur != null) {
            while (cur.moveToNext()) {

                String number1 = (cur.getString(cur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));// 得到手机号码
                String number = number1.replaceAll(" ", "");
                System.out.println(number);
                if (TextUtils.isEmpty(number))
                    continue;
                String name = (cur.getString(cur
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                map.put(name, number);
            }
            cur.close();
        }
        return map;
    }

    public Map<String, String> getSIMContactsInfo(final Context context) {
        Map<String, String> map = new HashMap<String, String>();
        //获取系统服务
        TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ContentResolver cr = context.getContentResolver();
        final String SIM_URI_ADN = "content://icc/adn";// SIM卡
        Uri uri = Uri.parse(SIM_URI_ADN);
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number1 = cursor.getString(cursor.getColumnIndex("number"));
                String number = number1.replaceAll(" ", "");
                if (TextUtils.isEmpty(number))
                    continue;
                map.put(name, number);

            }
            cursor.close();
        }
        return map;
    }

    public Map<String, String> mergeMap(Map<String, String> map1, Map<String, String> map2) {
        map1.putAll(map2);
        return map1;
    }

    public List<DataUser> getData(Map<String, String> map) {
        List<DataUser> listarray = new ArrayList<DataUser>();
        if(map==null||map.size()==0){return null;}
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String Fpinyin = null;
            String pinyin=null;
            String name = entry.getKey();
            String number = entry.getValue();
            if(TextUtils.isEmpty(name)){

            }else{
                 pinyin = CharacterParser.getPingYin(name);
            }
            if (pinyin != null) {
                Fpinyin = pinyin.substring(0, 1).toUpperCase();
            }else{
                Fpinyin="Z";
            }
            DataUser person = new DataUser();
            person.name = name;
            person.phoneNum = number;
            //person.setPinYin(pinyin);
            // 正则表达式，判断首字母是否是英文字母
            if (Fpinyin.matches("[A-Z]")) {
                person.sortLetters = Fpinyin;
            } else {
                person.sortLetters = "Z";
            }
            listarray.add(person);
        }
        return listarray;
    }
}
