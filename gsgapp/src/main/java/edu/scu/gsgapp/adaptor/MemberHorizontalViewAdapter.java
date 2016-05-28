package edu.scu.gsgapp.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.scu.gsgapp.R;

/**
 * Created by Hairong on 5/28/16.
 */
public class MemberHorizontalViewAdapter extends RecyclerView.Adapter<MemberHorizontalViewAdapter.SimpleViewHolder>{

    private Context context;
    private List<String> memberList;

    public MemberHorizontalViewAdapter(Context context, List<String> memberList){
        this.context = context;
        this.memberList = memberList;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final ImageView imageView;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView_person_portrait);
            textView = (TextView) view.findViewById(R.id.text_person_name);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.person_portrait_grid, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.textView.setText(memberList.get(position));
        int selectImage = position % 4 + 1;
        switch (selectImage) {
            case 1:
                holder.imageView.setImageResource(R.mipmap.portrait_1 );
                break;
            case 2:
                holder.imageView.setImageResource(R.mipmap.portrait_2 );
                break;
            case 3:
                holder.imageView.setImageResource(R.mipmap.portrait_3 );
                break;
            case 4:
                holder.imageView.setImageResource(R.mipmap.portrait_4 );
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.memberList.size();
    }
}

