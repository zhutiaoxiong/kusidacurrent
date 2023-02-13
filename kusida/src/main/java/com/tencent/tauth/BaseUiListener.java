package com.tencent.tauth;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

/**
 * Created by qq522414074 on 2017/3/27.
 */

public class BaseUiListener implements  IUiListener  {
    @Override
    public void onComplete(Object o) {
        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"拒绝分享");
    }

    @Override
    public void onCancel() {
        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"取消分享");
    }
}
