package edu.scu.gsgapp.adapter.propose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.scu.gsgapp.R;

/**
 * Created by Blood on 2016/5/20.
 */
public class ProposeEventAddMemberAdapter extends BaseAdapter {

    private Context context;
    public List<String> personNames;
    private TextView personName;
    private ImageView personImage;

    public ProposeEventAddMemberAdapter(Context context, List<String> personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public int getCount() {
        return personNames.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View grid = convertView;
        if (convertView == null) {
            grid = inflater.inflate(R.layout.person_portrait_grid, parent, false);
        }


        personName = (TextView) grid.findViewById(R.id.text_person_name);
        personName.setText(personNames.get(position));
        personImage = (ImageView) grid.findViewById(R.id.imageView_person_portrait);

        int selectImage = position % 4 + 1;
        switch (selectImage) {
            case 1:
                personImage.setImageResource(R.mipmap.portrait_1 );
                break;
            case 2:
                personImage.setImageResource(R.mipmap.portrait_2 );
                break;
            case 3:
                personImage.setImageResource(R.mipmap.portrait_3 );
                break;
            case 4:
                personImage.setImageResource(R.mipmap.portrait_4 );
                break;
        }


        return grid;
    }

    @Override
    public Object getItem(int position) {
        return personNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
