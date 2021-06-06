package com.softwaregiants.careertinder.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JobOpeningsAdapter extends RecyclerView.Adapter<JobOpeningsAdapter.JobOpeningsViewHolder> {

    private List<JobOpeningModel> jobOpenings;
    private OnItemClickListener onItemClickListener;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public JobOpeningsAdapter(List<JobOpeningModel> jobOpeningModelList){
        this.jobOpenings = jobOpeningModelList;
    }

    @NonNull
    @Override
    public JobOpeningsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_openings_list_item, parent, false);
        return new JobOpeningsViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final JobOpeningsViewHolder holder, int position) {
        JobOpeningModel jobOpeningModel = jobOpenings.get(position);
//        holder.jobOpening.setImageResource(R.drawable.baseline_assignment_ind_black_24);
        holder.jobTitle.setText(jobOpeningModel.getJobTitle());
        holder.placeOfWork.setText(jobOpeningModel.getPlaceOfWork());
        holder.preferredSkill.setText(jobOpeningModel.getSkill1());
        if ( null != jobOpeningModel.getImageUrl() && !jobOpeningModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(jobOpeningModel.getImageUrl());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder).into(holder.jobOpening);
                }
            });
        } else {
            holder.jobOpening.setImageResource(R.drawable.image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return jobOpenings.size();
    }

    static class JobOpeningsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        ImageView jobOpening;
        TextView jobTitle;
        TextView placeOfWork;
        TextView preferredSkill;
        ImageButton editJobOpening;
        ImageButton deleteJobOpening;

        JobOpeningsViewHolder(View v, final OnItemClickListener listener) {
            super(v);
            jobOpening = v.findViewById(R.id.IVJobOpening);
            jobTitle = v.findViewById(R.id.TVJobTitle);
            placeOfWork = v.findViewById(R.id.TVPlaceOfWork);
            preferredSkill = v.findViewById(R.id.TVPreferredSkill);
            editJobOpening = v.findViewById(R.id.imageButtonEdit);
            deleteJobOpening = v.findViewById(R.id.imageButtonDelete);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            editJobOpening.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onEditClick(pos);
                        }
                    }
                }
            });

            deleteJobOpening.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(pos);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
