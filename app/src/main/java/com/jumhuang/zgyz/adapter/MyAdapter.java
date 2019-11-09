package com.jumhuang.zgyz.adapter;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.zgyz.*;
import com.jumhuang.zgyz.base.*;
import com.jumhuang.zgyz.ui.activity.*;
import java.io.*;
import java.util.*;
import java.text.*;
import com.jumhuang.zgyz.entity.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>
{
    private Context context;
    private List<Article> list;
    private LayoutInflater mInflater;

    public MyAdapter(Context context, List<Article> list)
    {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount()
    {
        return list != null ?list.size(): 0;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = mInflater.inflate(R.layout.item_project, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, final int position)
    {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        Article data=list.get(position);

		//holder.item_icon.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + data.getName() + "/world_icon.jpeg"));
        holder.item_name.setText(data.getTitle());
		holder.item_date.setText(data.getDescription());
	}

    public class MyHolder extends RecyclerView.ViewHolder
    {
		ImageView item_icon;
        TextView item_name;
		TextView item_date;

        public MyHolder(final View v)
        {
            super(v);

			item_icon = (ImageView)v.findViewById(R.id.item_project_icon);
            item_name = (TextView)v.findViewById(R.id.item_project_name);
			item_date = (TextView)v.findViewById(R.id.item_project_date);

            v.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View p1)
                    {
						Intent intent=new Intent(context, ContentActivity.class);
						 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 intent.putExtra("DATA", list.get(getPosition()).getUrl());
						 context.startActivity(intent);
                    }
                });

			v.setOnLongClickListener(new View.OnLongClickListener()
				{
					@Override
					public boolean onLongClick(View p1)
					{
						/*String name=BaseActivity.ReadFile(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + list[getAdapterPosition()].getName() + "/levelname.txt");

						 BaseActivity.Toast(context, BaseActivity.Zip(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + list[getAdapterPosition()].getName(), Environment.getExternalStorageDirectory() + "/" + name + ".zip", false, null));
						 */
						return false;
					}
				});
        }
    }
}
