package com.jumhuang.zgyz.ui.activity;

import android.*;
import android.content.pm.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.zgyz.*;
import com.jumhuang.zgyz.adapter.*;
import com.jumhuang.zgyz.base.*;
import java.lang.Process;
import com.jumhuang.zgyz.entity.*;
import com.jumhuang.zgyz.permission.*;
import com.jumhuang.zgyz.permission.request.*;
import com.jumhuang.zgyz.permission.requestresult.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import android.support.v7.widget.Toolbar;
import com.jumhuang.zgyz.R;
import java.io.*;
import java.net.*;

public class MainActivity extends BaseActivity 
{
	private RecyclerView recycler;
    private SwipeRefreshLayout swipeview;
    private MyAdapter adapter;

	private List<Article> list=new ArrayList<Article>();
	private Handler hander;
    private Bundle bundle;
    private Message msg;

	public String SIGN="308201fc30820165a003020102020101300d06092a864886f70d01010b05003043310b30090603550406130238363110300e060355040813075369636875616e310f300d060355040713065a69676f6e673111300f060355040313084a756d4875616e673020170d3136303832373030313331365a180f32313136303830333030313331365a3043310b30090603550406130238363110300e060355040813075369636875616e310f300d060355040713065a69676f6e673111300f060355040313084a756d4875616e6730819f300d06092a864886f70d010101050003818d0030818902818100abb296f60f48713010cca7598302776dd716ce051685b3b813389829a80f646f55613e63cf10722d73f131dac2fa021876cc37e56f643de44e6aaabee42ce38099f43c36f8f2187e7a65d6de2ac96639da539308b052daefa1ab859a72f12f2be306ba048372cf7ef082873557d22096aed732be5f24ee440eb14bc0d95179eb0203010001300d06092a864886f70d01010b0500038181002502af8647f2f8ec17c7d080270cb7ed50fe263171d56ec92a11109162279f9daf457e632b677252f1d5a0cc904cc28544863e7467eb5f40d774b4e69a30c1b0bffd76ac3202400665b55ebbbdd941cc92a1d611ff23f79811b429a5c03ae620f422730e5066d874a855fc3d75518d195e6ed9aeb756d7f2b7820e9d2f84e96b";

	private IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    private IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		recycler = (RecyclerView)findViewById(R.id.main_recycler);
        swipeview = (SwipeRefreshLayout)findViewById(R.id.main_swiperefresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

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

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		recycler.setItemAnimator(new DefaultItemAnimator());
		adapter = new MyAdapter(getApplicationContext(), list);
		recycler.setAdapter(adapter);

        recycler.setOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override  
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {  
                    int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();  
                    swipeview.setEnabled(topRowVerticalPosition >= 0);  
                }  

                @Override  
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {  
                    super.onScrollStateChanged(recyclerView, newState);
                }  
			}); 

		try
		{
			if (getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures[0].toCharsString().equals(SIGN))
			{
				if (Build.VERSION.SDK_INT >= 23)
				{
					if (requestPermissions())
					{
						init();
					}
				}
				else
				{
					init();
				}
			}
			else
			{
				finish();
			}
		}
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
        }
    }

	private void init()
	{
		try
		{
			InetAddress address = InetAddress.getByName("www.weibo.com");
			if (!address.isReachable(3000))
			{
				Toast("网络错误！");
				finish();
			}
			Read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void Read()
    {
		list.clear();
		//子线程
        new Thread(new Runnable() {
				@Override
				public void run()
				{
					parseDate();
				}
			}).start();

		hander = new Handler() {
            @Override
            public void handleMessage(Message msg)
			{
                //RecyclerView列表进行UI数据更新
                Article bean = new Article(msg.getData().getString("TITLE"), msg.getData().getString("DESCRIPTION"), "Test", 0, msg.getData().getString("TIME"), msg.getData().getString("URL"));
                list.add(bean);
                adapter.notifyDataSetChanged();
                super.handleMessage(msg);
            }
        };
		adapter.notifyDataSetChanged();
    }

	private List<Article> listData()
	{
        list = new ArrayList<>();

        return list;
    }

    public void parseDate()
	{
        try
		{
			Document doc = Jsoup.connect("http://www.zgyz.net/Item/list.asp?id=1489").get();
			Elements elements = doc.select("div.newslist").select("dl.nl_con1.clearfix");
			for (Element ele:elements)
			{
				//获取名称
				String movieName =ele.select("h4.nlc_tit").select("a").text();
				String description=ele.select("p.nlc_info").text();
				String url=ele.select("p.nlc_info").select("a").attr("abs:href");   //abs:表示绝对路径，也就是包括网址的完整路径
				//Document doc2=Jsoup.connect(url).get();
				String time=ele.select("p.nlc_time").text().substring(0, 10);

				msg = Message.obtain();
				bundle = new Bundle();
				//往Bundle中存放数据
				bundle.putString("TITLE", movieName);
				bundle.putString("DESCRIPTION", description);
				bundle.putString("TIME", time);
				bundle.putString("URL", url);
				msg.setData(bundle);
				hander.sendMessage(msg);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//请求权限
    private boolean requestPermissions()
    {
        //需要请求的权限
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //开始请求权限
        return requestPermissions.requestPermissions(this, permissions, PermissionUtils.ResultCode1);
    }

    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults))
        {
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast("授权成功！", Toast.LENGTH_LONG);
            init();
        }
        else
        {
            //输出授权结果
            Toast("请给APP授权，否则某些功能无法正常使用！", Toast.LENGTH_LONG);
            finish();
        }
    }

	@Override
	protected void onStart()
	{
		//Read();
		super.onStart();
	}

	static boolean isExit; 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (!isExit)
			{
				isExit = true;
				Toast("再按一次退出软件!"); 
				new Thread(new Runnable() 
					{
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(3000);
								isExit = false;
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}).start();
			}
			else
			{
				finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
