package chauhuynh.info.clock_library.clock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chauhuynh.info.clock_library.R;


/**
 * Created by Chau Huynh on 3/14/2018.
 */

public class AdapterClock extends BaseAdapter {

    private Context myContext;
    private List<NumberClock> list;

    public AdapterClock(Context myContext, List<NumberClock> list) {
        this.myContext = myContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private TextView txtName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;
        ViewHolder viewHolder = new ViewHolder();

        if (rowView == null && inflater != null) {
            rowView = inflater.inflate(R.layout.item_clock_from, null);
            //Init
            viewHolder.txtName = rowView.findViewById(R.id.tvNumber);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (list.get(i).isEnable())
            viewHolder.txtName.setTextColor(myContext.getResources().getColor(R.color.wh));
        else
            viewHolder.txtName.setTextColor(myContext.getResources().getColor(R.color.black));
        viewHolder.txtName.setText(list.get(i).getNumber());

        return rowView;
    }
}
