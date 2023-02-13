/*
 * Filename	CharacterParser.java
 * Company	�Ϻ�����-�ֶ��ֹ�˾��
 * @author	LuRuihui
 * @version	0.1
 */
package common.pinyinzhuanhuan;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class CharacterParser {
    public static String getPingYin(String inputString) {
        if(inputString==null||inputString.equals("")){return null;}
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (char curchar : input) {
                if (Character.toString(curchar).matches("[\u4e00-\u9fa5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    if(temp!=null&&temp.length>=1){
                        output += temp[0];
                    }
                } else
                    output += Character.toString(curchar);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

}
