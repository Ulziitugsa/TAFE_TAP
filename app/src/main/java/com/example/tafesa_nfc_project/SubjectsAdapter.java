package com.example.tafesa_nfc_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.VersionVH> {

    List<Subjects> subjectsList;
    public SubjectsAdapter(List<Subjects> subjectsList) {this.subjectsList = subjectsList;}

    @NonNull
    @Override
    public VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_view, parent,false);
        return new VersionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionVH holder, int position) {
        Subjects subjects = subjectsList.get(position);
        holder.subjectNameTxt.setText(subjects.getSubjectName());
        holder.dateTxt.setText(subjects.getDate());
        holder.hoursTxt.setText(subjects.getHours());
        holder.descriptionTxt.setText(subjects.getDescription());

        boolean isExpandable = subjectsList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public class VersionVH extends RecyclerView.ViewHolder {
        TextView subjectNameTxt, dateTxt, hoursTxt, descriptionTxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public VersionVH(@NonNull View itemView) {
            super(itemView);
            subjectNameTxt = itemView.findViewById(R.id.subjectName);
            dateTxt = itemView.findViewById(R.id.date);
            hoursTxt = itemView.findViewById(R.id.hours);
            descriptionTxt = itemView.findViewById(R.id.description);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subjects subjects = subjectsList.get(getAdapterPosition());
                    subjects.setExpandable(!subjects.isExpandable());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
