package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import yucatancountryclub.com.ycc.DocumentsDetailActivity;
import yucatancountryclub.com.ycc.Model.Document;
import yucatancountryclub.com.ycc.Model.DocumentCategory;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class DocumentAdapterRecyclerView extends RecyclerView.Adapter<DocumentAdapterRecyclerView.DocumentViewHolder>{
    private ArrayList<Document> documents;
    private int resource;
    private Activity activity;

    public DocumentAdapterRecyclerView(ArrayList<Document> documents, int resource, Activity activity) {
        this.documents = documents;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public DocumentAdapterRecyclerView.DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocumentAdapterRecyclerView.DocumentViewHolder holder, int position) {
        final Document document = documents.get(position);

        holder.title.setText(document.getTitle());
        holder.description.setText(document.getDescription());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(document.getDocument());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder{
        private CardView container;
        private TextView title;
        private TextView description;

        public DocumentViewHolder(View itemView){
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.container);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

