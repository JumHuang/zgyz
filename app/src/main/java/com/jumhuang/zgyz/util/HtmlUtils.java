package com.jumhuang.zgyz.util;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;

/**
 * Created by CKZ on 2017/8/8.
 */

public class HtmlUtils
{
    private static HtmlUtils instance;
    private Activity activity;
    private TextView text;
    private Drawable pic;
    private String resource;
	
    private HtmlUtils(Activity activity, TextView text)
	{
        this.activity = activity;
        this.text = text;
    }
	
    public static HtmlUtils getInstance(Activity activity, TextView text)
	{
        if (instance == null)
		{
            instance = new HtmlUtils(activity, text);
        }
        return instance;
    }

    public void setHtmlWithPic(String resource)
	{
        this.resource = resource;
        if (Build.VERSION.SDK_INT >= 24)
            text.setText(Html.fromHtml(resource, Html.FROM_HTML_MODE_COMPACT, imageGetter, null));
        else
            text.setText(Html.fromHtml(resource, imageGetter, null));

    }
	
    Html.ImageGetter imageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String s)
		{
            if (pic != null)
			{
                Log.d("TAG", "显示");
                return pic;
            }
            else
			{
                Log.d("TAG", "加载" + s);
                getPic(s);
            }
            return null;
        }
    };

    /**
     * 加载网络图片
     * @param s
     */
    private void getPic(final String s)
	{
        new Thread(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						final Drawable drawable = Drawable.createFromStream(new URL(s).openStream(), "");
						activity. runOnUiThread(new Runnable() {
								@Override
								public void run()
								{
									if (drawable != null)
									{
										WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
										DisplayMetrics outMetrics = new DisplayMetrics();
										wm.getDefaultDisplay().getMetrics(outMetrics);
										float picW = drawable.getIntrinsicWidth();
										float picH = drawable.getIntrinsicHeight();
										int width = outMetrics.widthPixels;
										drawable.setBounds(0, 0, width, (int)((picH / picW) * width));
										pic = drawable;
										if (Build.VERSION.SDK_INT >= 24)
											text.setText(Html.fromHtml(resource, Html.FROM_HTML_MODE_COMPACT, imageGetter, null));
										else
											text.setText(Html.fromHtml(resource, imageGetter, null));
									}
								}
							});
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
    }
}
