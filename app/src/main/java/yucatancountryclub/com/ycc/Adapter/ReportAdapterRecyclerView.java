package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import yucatancountryclub.com.ycc.Model.Report;
import yucatancountryclub.com.ycc.R;
import yucatancountryclub.com.ycc.ReportDetailActivity;

/**
 * Created by Zazu on 15/02/18.
 */

public class ReportAdapterRecyclerView extends RecyclerView.Adapter<ReportAdapterRecyclerView.ReportViewHolder>{
    private ArrayList<Report> reports;
    private int resource;
    private Activity activity;

    public ReportAdapterRecyclerView(ArrayList<Report> reports, int resource, Activity activity) {
        this.reports = reports;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ReportAdapterRecyclerView.ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportAdapterRecyclerView.ReportViewHolder holder, int position) {
        final Report report = reports.get(position);

        System.out.println(report.getStatus());

        holder.reportTitle.setText(report.getTitle());
        holder.reportFolio.setText(report.getPk());
        holder.reportTime.setText(report.getTime());
        /*holder.reportDate.setText(report.getDate());
        holder.reportTime.setText(report.getTime());*/
        switch (report.getStatus()){
            case "pending":
                holder.thumbnail.setImageResource(R.drawable.status_rojo);
                break;
            case "processing":
                holder.thumbnail.setImageResource(R.drawable.status_amarillo);
                break;
            case "completed":
                holder.thumbnail.setImageResource(R.drawable.ok);
                break;
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReportDetailActivity.class);
                intent.putExtra("folioDetail", report.getPk());
                intent.putExtra("userDetail", report.getUser());
                intent.putExtra("titleDetail", report.getTitle());
                intent.putExtra("descriptionDetail", report.getDescription());
                intent.putExtra("statusDetail", report.getStatus());
                intent.putExtra("dateDetail", report.getDate());
                intent.putExtra("timeDetail", report.getTime());
                intent.putStringArrayListExtra("imageDetail", report.getImages());
                intent.putStringArrayListExtra("postsDetail", report.getPosts());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder{
        private TextView reportTitle;
        private TextView reportFolio;
        private TextView reportDate;
        private TextView reportTime;
        private CardView container;
        private CircleImageView thumbnail;

        public ReportViewHolder(View itemView){
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.cardview_report_container);
            reportTitle = (TextView) itemView.findViewById(R.id.reportTitle);
            reportFolio = (TextView) itemView.findViewById(R.id.reportFolio);
            reportDate = (TextView) itemView.findViewById(R.id.reportDate);
            reportTime = (TextView) itemView.findViewById(R.id.reportTime);
            thumbnail = (CircleImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}

