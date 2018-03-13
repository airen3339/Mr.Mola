package net.yaiba.money.utils;

/**
 * Created by yang_lifeng on 2018/03/13.
 * listView 样式设置 重写SimpleAdapter
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.yaiba.money.R;

import java.util.List;
import java.util.Map;

public class SpecialAdapter extends SimpleAdapter {
    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

    public SpecialAdapter(Context context, List<? extends Map<String, ?>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        //int colorPos = position % colors.length;
        //view.setBackgroundColor(colors[colorPos]);

        TextView t_category_child_name=(TextView) view.findViewById(R.id.category_child_name);
        TextView t_amounts=(TextView) view.findViewById(R.id.amounts);
        TextView t_create_time=(TextView) view.findViewById(R.id.create_time);
        TextView t_remark=(TextView) view.findViewById(R.id.remark);
        TextView t_type_id=(TextView) view.findViewById(R.id.type_id);



        if("0".equals(t_type_id.getText().toString())){////支出 0,收入1
            t_amounts.setTextColor(Color.parseColor("#EE2428"));
            t_amounts.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if("1".equals(t_type_id.getText().toString())) {
            t_amounts.setTextColor(Color.parseColor("#228B22"));
            t_amounts.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }


        return view;
    }
}