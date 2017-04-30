package banq.smartline;

/**
 * Created by macbookpro on 30/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AndroidListAdapter extends ArrayAdapter {
    String[] androidListViewStrings;
    Integer[] imagesId;
    String[] temps;
    Context context;
    String[] token;

    public AndroidListAdapter(Activity context, Integer[] imagesId, String[] textListView,String[] temps) {
        super(context, R.layout.custom_list, textListView);
        this.androidListViewStrings = textListView;
        this.imagesId = imagesId;
        this.context = context;
        this.temps = temps;
    }


    public AndroidListAdapter(Activity context, Integer[] imagesId, String[] textListView,String[] temps,String[] token) {
        super(context, R.layout.custom_list, textListView);
        this.androidListViewStrings = textListView;
        this.imagesId = imagesId;
        this.context = context;
        this.temps = temps;
        this.token = token;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow = layoutInflater.inflate(R.layout.custom_list, null,true);
        TextView mtextView = (TextView) viewRow.findViewById(R.id.text_view);
        TextView text_temps = (TextView) viewRow.findViewById(R.id.text_view_temps);
        ImageView mimageView = (ImageView) viewRow.findViewById(R.id.image_view);
        mtextView.setText(androidListViewStrings[i]);
        mimageView.setImageResource(imagesId[i]);
        text_temps.setText(temps[i]);
        if(token[i]!=null) {
            viewRow.setBackgroundColor(R.color.moi_fil);
        }
        return viewRow;
    }
}