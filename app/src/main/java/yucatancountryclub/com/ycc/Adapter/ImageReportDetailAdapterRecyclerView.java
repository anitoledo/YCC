package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import yucatancountryclub.com.ycc.Model.ImageReport;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class ImageReportDetailAdapterRecyclerView extends RecyclerView.Adapter<ImageReportDetailAdapterRecyclerView.ImageReportViewHolder>{
    private ArrayList<String> imagesReport;
    private int resource;
    private Activity activity;

    public ImageReportDetailAdapterRecyclerView(ArrayList<String> imagesReport, int resource, Activity activity) {
        this.imagesReport = imagesReport;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ImageReportDetailAdapterRecyclerView.ImageReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ImageReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageReportDetailAdapterRecyclerView.ImageReportViewHolder holder, final int position) {
        final String imageReport = imagesReport.get(position);
        System.out.println("IMAGEN "+imageReport);
        //Picasso.with(activity).load(imageReport).into(holder.report_image);
        Glide.with(activity).load(imageReport).into(holder.report_image);
        holder.report_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(activity, imagesReport)
                        .setStartPosition(position)
                        .allowSwipeToDismiss(true)
                        .show();
            }
        });
    //    holder.delete_image.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return imagesReport.size();
    }

    public class ImageReportViewHolder extends RecyclerView.ViewHolder{
        private ImageView report_image;
        //private ImageView delete_image;

        public ImageReportViewHolder(View itemView){
            super(itemView);
            report_image = (ImageView) itemView.findViewById(R.id.report_image);
           // delete_image = (ImageView) itemView.findViewById(R.id.delete_image);
        }
    }
}

