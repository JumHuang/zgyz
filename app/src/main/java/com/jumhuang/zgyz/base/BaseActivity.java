package com.jumhuang.zgyz.base;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.pgyersdk.feedback.*;
import java.io.*;

import android.support.v7.app.AlertDialog;

public class BaseActivity extends AppCompatActivity
{
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		data = getSharedPreferences("AppData", Activity.MODE_PRIVATE);
		
		new PgyerFeedbackManager.PgyerFeedbackBuilder()    
			.setDisplayType(PgyerFeedbackManager.TYPE.DIALOG_TYPE)  
			.builder()
			.register();
	}
	
	public void Snackbar(View view, String message)
	{
		Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
	}

	public void Snackbar(View view, String message, int time)
	{
		Snackbar.make(view, message, time).show();
	}

	public void Toast(String message) 
	{
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void Toast(String message, int time)
	{
        Toast.makeText(getApplicationContext(), message, time).show();
    }

	public static void Toast(Context context, String message) 
	{
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context context, String message, int time)
	{
        Toast.makeText(context, message, time).show();
    }

	public static void Dialog(Context context, String title, String msg)
	{
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(msg)
			.setCancelable(true)
			.setPositiveButton("关闭", null)
			.show();
	}

	public boolean putString(String name, String value)
	{
		return data.edit().putString(name, value).commit();
	}

	public boolean putBoolean(String name, boolean value)
	{
		return data.edit().putBoolean(name, value).commit();
	}

	public boolean putInt(String name, int value)
	{
		return data.edit().putInt(name, value).commit();
	}

	public String getString(String name, String formatvalue)
	{
		return data.getString(name, formatvalue);
	}

	public boolean getBoolean(String name, boolean formatvalue)
	{
		return data.getBoolean(name, formatvalue);
	}

	public int getInt(String name, int formatvalue)
	{
		return data.getInt(name, formatvalue);
	}

	public void remove(String name)
	{
		data.edit().remove(name);
	}
	
	//写入数据
	public static boolean WriteFile(String path, String data)
	{
		try
		{
			File file=new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream fout = new FileOutputStream(path); 
			byte[] bytes =data.getBytes(); 
			fout.write(bytes); 
			fout.close(); 
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	//读取数据
	public static String ReadFile(String path)
	{
		try
		{
			FileInputStream is =new FileInputStream(path);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//读取Assets文件数据
	public static String ReadAssetsFile(Context context, String path)
    {
		try
		{ 
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(path)); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line=null,result=null;
			while ((line = bufReader.readLine()) != null)
				result += line + "\n";
			return result;
		}
		catch (Exception e)
		{ 
			e.printStackTrace(); 
		}
		return null;
    }

	//导出assets里的文件
	public static void CopyAssets(Context context, String path, String name)
	{
		try
		{  
            InputStream in = context.getResources().getAssets().open(name);  
            System.err.println("");  
            File outFile = new File(path, name);  
            OutputStream out = new FileOutputStream(outFile);  
            byte[] buf = new byte[1024];  
            int len;  
            while ((len = in.read(buf)) > 0)
			{  
                out.write(buf, 0, len);  
            }  
            in.close();  
            out.close();  
        }
		catch (IOException e)
		{  
            e.printStackTrace();  
        }  
    }  

	//复制文件
	public static void CopyFile(String fromFile, String toFile, Boolean rewrite)
	{
		File input=new File(fromFile);
		File output=new File(toFile);
		if (!input.exists())
		{
			return;
		}
		if (!input.isFile())
		{
			return ;
		}
		if (!input.canRead())
		{
			return ;
		}
		if (!output.getParentFile().exists())
		{
			output.getParentFile().mkdirs();
		}
		if (output.exists() && rewrite)
		{
			output.delete();
		}
		try
		{
			FileInputStream fosfrom = new FileInputStream(input);
			FileOutputStream fosto = new FileOutputStream(output);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0)
			{
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//删除文件夹
	public static void DeleteFile(File oldPath, boolean deleteDir)
	{
		if (oldPath.isDirectory())
		{
			File[] files = oldPath.listFiles();
			for (File file : files)
			{
				DeleteFile(file, deleteDir);
				file.delete();
			}

			if (deleteDir)
				oldPath.delete();
		}
		else
		{
			oldPath.delete();
		}
	}
}
