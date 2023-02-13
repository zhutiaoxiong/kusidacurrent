package common;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.util.Locale;

/**
 * Created by Nathan_Lee on 2016/11/17.
 * 选择不同语言
 */

public class LanguageChoose {
    //示例：LanguageChoose.choose(Locale.US);
    public static void choose(Locale language) {
        Resources      resources = GlobalContext.getContext().getResources();
        DisplayMetrics dm        = resources.getDisplayMetrics();
        Configuration  config    = resources.getConfiguration();
        // 应用用户选择语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){//
//            config.setLocale(language);//Locale.US
            config.setLocale(Locale.SIMPLIFIED_CHINESE);//Locale.US
        }else{
//            config.locale = language;
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
        //以下事件发送其它页面，语言改变了，只需四个主页面动态改，其它页面自动刷新时获取
        ODispatcher.dispatchEvent(OEventName.LANGUAGE_CHANGE);
    }
}
