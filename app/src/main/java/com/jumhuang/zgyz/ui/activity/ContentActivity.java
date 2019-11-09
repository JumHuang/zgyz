package com.jumhuang.zgyz.ui.activity;

import android.os.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.zgyz.*;
import com.jumhuang.zgyz.base.*;
import java.io.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.safety.*;

import android.support.v7.widget.Toolbar;
import android.text.*;
import android.graphics.drawable.*;
import android.util.*;
import java.net.*;
import android.content.*;
import com.jumhuang.zgyz.util.*;

public class ContentActivity extends BaseActivity
{
	private Toolbar toolbar;
	private SwipeRefreshLayout swipeview;
	private String data;
	private Document doc;
	private Handler handler=null;

	private TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		swipeview = (SwipeRefreshLayout)findViewById(R.id.content_swiperefresh);
		content = (TextView)findViewById(R.id.content_content);

        toolbar = (Toolbar) findViewById(R.id.content_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
			{ 
				@Override 
				public void onClick(View v)
				{ 
					finish(); 
				} 
			});

		data = getIntent().getStringExtra("DATA");

        swipeview.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        swipeview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
            {
                @Override
                public void onRefresh()
                {
                    swipeview.setRefreshing(false);
                    new Handler().postDelayed(new Runnable() 
                        {
                            @Override
                            public void run()
                            {
                                Read();
                            }
                        }, 3000);
                }
            });

		Read();
	}

	Html.ImageGetter imgGetter = new Html.ImageGetter() 
	{
        public Drawable getDrawable(String source)
		{
            Drawable drawable = null;
            URL url = null;
            try
			{
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "img");
            }
			catch (Exception e)
			{
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
    };

	private String get(String classes)
	{
		return Jsoup.clean(doc.select(classes).toString(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
	}

	private void Read()
	{
		//创建属于主线程的handler  
        handler = new Handler();

		new Thread() {
			@Override
			public void run()
			{
				try
				{
					doc = Jsoup.connect(data).get();

					//android中相关的view和控件不是线程安全的，必须单独做处理
					//详细网址：https://blog.csdn.net/djx123456/article/details/6325983
					handler.post(runnableUi);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 构建Runnable对象，在runnable中更新界面  
    Runnable runnableUi=new  Runnable(){  
        @Override  
        public void run()
		{  
			try
			{
				//更新界面  
				toolbar.setTitle(doc.select("div.articlecontent").select("h3").text());

				String text="div.MyContent";

				String contents=doc.select("div#MyContent").get(0).toString();
				String d=contents.replace("/uploadfiles", "http://www.zgyz.net/uploadfiles");
				if (contents != null)
				{
					//content.setText(contents);
					//content.setText(Html.fromHtml(contents, Html.FROM_HTML_MODE_COMPACT,imgGetter, null));
					HtmlUtils.getInstance(ContentActivity.this,content).setHtmlWithPic(d);
				}
			}  
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
    };

	@Override
	protected void onStart()
	{
		//Read();
		super.onStart();
	}
}
