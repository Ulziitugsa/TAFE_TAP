package com.example.tafesa_nfc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceVH> {
    private Context mContext;
    ArrayList<ArrayList> mlist;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    ImageView iv;
    CardView cv;
    //OnItemClickListener
   // private OnItemClickListener mListener;
    /*
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }*/
    public AttendanceAdapter(Context context, ArrayList<ArrayList> list) {
        this.mContext = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public AttendanceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.settings_view, parent, false);
        //Prolly for the Array?
        cv = (CardView) view.findViewById(R.id.cardView);
        tv1 = (TextView) view.findViewById(R.id.textView1);
        tv2 = (TextView) view.findViewById(R.id.textView2);
        iv = (ImageView) view.findViewById(R.id.image1);
        AttendanceVH viewHolder = new AttendanceVH(view) {
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceVH holder, int position) {
        tv1.setText((String) mlist.get(position).get(0));
        tv2.setText((String) mlist.get(position).get(1));
        iv.setImageResource((int) mlist.get(position).get(2));


    }

    @Override
    public int getItemCount() {
        int limit = 6;
        if(mlist.size() > limit){
            return limit;
        }
        else
        {
            return mlist.size();
        }

    }

    public class AttendanceVH extends RecyclerView.ViewHolder {
        //May change
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public AttendanceVH(@NonNull View view) {
            super(view);

        }
    }
}

