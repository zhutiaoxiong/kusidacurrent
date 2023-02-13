package model;

import android.text.TextUtils;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import model.answer.DataAnswer;
import model.answer.DataFindway;

public class ManagerAnswer {
    public List<DataAnswer> secretInfoslist;//密保问题列表
    private String[] usedQuestion = new String[]{"", "", ""};//能被使用的提问
    public List<DataFindway> secretTypeslist;
    public static String verifyStr;
    public static String phoneNum;

    // ========================out======================
    private static ManagerAnswer _instance;

    private ManagerAnswer() {
    }

    public static ManagerAnswer getInstance() {
        if (_instance == null)
            _instance = new ManagerAnswer();
        return _instance;
    }

    // =======================get==========================
    public DataAnswer getQuestionFromStr(String qusName) {
        if (secretInfoslist == null) return null;
        for (DataAnswer ques : secretInfoslist) {
            if (ques.title.equals(qusName)) {
                return ques;
            }
        }
        return null;
    }

    public void changeUsedQuestion(String str, int pos) {
        usedQuestion[pos] = str;
    }

    public List<String> getUnUsedQuestion() {
        if (secretInfoslist == null) return new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        for (DataAnswer ques : secretInfoslist) {
            if (ques.title.equals(usedQuestion[0]) ||
                    ques.title.equals(usedQuestion[1]) ||
                    ques.title.equals(usedQuestion[2])) {
            } else {//与三种文字都不同
                list.add(ques.title);
            }
        }
        return list;
    }

    public List<Integer> getUsedIde(List<String> list, List<DataAnswer> list1) {
        List<Integer> list2 = null;
        for (int i = 0; i < list.size(); i++) {
            for (int j = list1.size(); j > 0; j--) {
                if (list.get(i).equals(list1.get(j).title)) {
                    list2.add(list1.get(j).ide);
                }
            }
        }
        return list2;
    }

    // =================================================
    public void saveSecretInfoslist(JsonArray secretInfos) {
        if (secretInfos == null) return;
        this.secretInfoslist = DataAnswer.fromJsonArray(secretInfos);
    }

    public void saveSecretTypeslist(JsonArray secretTypes) {
        if (secretTypes == null) return;
        this.secretTypeslist = DataFindway.fromJsonArray(secretTypes);
    }

    public  String getPhoneNumStar(String phoneNum) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(phoneNum) && phoneNum.length() > 6) {

            for (int i = 0; i < phoneNum.length(); i++) {
                char c = phoneNum.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}