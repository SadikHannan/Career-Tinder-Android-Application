package com.softwaregiants.careertinder.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.callback.OnItemClickListener;
import com.softwaregiants.careertinder.models.CandidateProfileModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompanyMatchViewerAdapter extends RecyclerView.Adapter<CompanyMatchViewerAdapter.CompanyMatchViewerViewHolder> {

    private List<CandidateProfileModel> candidateProfileModels;
    private OnItemClickListener listener;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public CompanyMatchViewerAdapter(List<CandidateProfileModel> candidateProfileModels, OnItemClickListener listener){
        this.candidateProfileModels = candidateProfileModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompanyMatchViewerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidate_matchviewer_item, parent, false);
        return new CompanyMatchViewerViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyMatchViewerAdapter.CompanyMatchViewerViewHolder holder, int position) {
        CandidateProfileModel candidateProfileModel = candidateProfileModels.get(position);
        holder.jobTitle.setText(candidateProfileModel.getName());
        holder.placeOfWork.setText(candidateProfileModel.getPlace());
        holder.preferredSkill.setText(candidateProfileModel.getSkill_one());
        if ( null != candidateProfileModel.getImageUrl() && !candidateProfileModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(candidateProfileModel.getImageUrl());
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
        return candidateProfileModels.size();
    }

    static class CompanyMatchViewerViewHolder extends RecyclerView.ViewHolder{

        ImageView jobOpening;
        TextView jobTitle;
        TextView placeOfWork;
        TextView preferredSkill;

        CompanyMatchViewerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            jobOpening = itemView.findViewById(R.id.IVJobOpening);
            jobTitle = itemView.findViewById(R.id.TVJobTitle);
            placeOfWork = itemView.findViewById(R.id.TVPlaceOfWork);
            preferredSkill = itemView.findViewById(R.id.TVPreferredSkill);

            itemView.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}
