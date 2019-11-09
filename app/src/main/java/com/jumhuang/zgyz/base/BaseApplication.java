package com.jumhuang.zgyz.base;

import android.app.*;
import com.pgyersdk.crash.*;
import com.pgyersdk.update.*;

public class BaseApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		PgyCrashManager.register();
		PgyCrashManager.setIsIgnoreDefaultHander(true);
		new PgyUpdateManager.Builder()
			.setForced(true)                //设置是否强制提示更新
			.setUserCanRetry(true)         //失败后是否提示重新下载
			.setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk， 默认为true
			.register();
	}
}
