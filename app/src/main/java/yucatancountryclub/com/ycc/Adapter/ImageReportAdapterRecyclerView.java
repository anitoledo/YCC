package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import yucatancountryclub.com.ycc.DirectoryDetailActivity;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.Model.ImageReport;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class ImageReportAdapterRecyclerView extends RecyclerView.Adapter<ImageReportAdapterRecyclerView.ImageReportViewHolder>{
    private ArrayList<ImageReport> imagesReport;
    private int resource;
    private Activity activity;

    public ImageReportAdapterRecyclerView(ArrayList<ImageReport> imagesReport, int resource, Activity activity) {
        this.imagesReport = imagesReport;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ImageReportAdapterRecyclerView.ImageReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ImageReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageReportAdapterRecyclerView.ImageReportViewHolder holder, final int position) {
        final ImageReport imageReport = imagesReport.get(position);
        Picasso.with(activity).load(imageReport.getUri()).into(holder.report_image);
        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imagesReport.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imagesReport.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesReport.size();
    }

    public class ImageReportViewHolder extends RecyclerView.ViewHolder{
        private ImageView report_image;
        private ImageView delete_image;

        public ImageReportViewHolder(View itemView){
            super(itemView);
            report_image = (ImageView) itemView.findViewById(R.id.report_image);
            delete_image = (ImageView) itemView.findViewById(R.id.delete_image);
        }
    }
}

