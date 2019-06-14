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

import java.util.ArrayList;

import yucatancountryclub.com.ycc.DirectoryDetailActivity;
import yucatancountryclub.com.ycc.DocumentsDetailActivity;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.Model.DocumentCategory;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class DocumentCategoryAdapterRecyclerView extends RecyclerView.Adapter<DocumentCategoryAdapterRecyclerView.DocumentCategoryViewHolder>{
    private ArrayList<DocumentCategory> categories;
    private int resource;
    private Activity activity;

    public DocumentCategoryAdapterRecyclerView(ArrayList<DocumentCategory> categories, int resource, Activity activity) {
        this.categories = categories;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public DocumentCategoryAdapterRecyclerView.DocumentCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DocumentCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocumentCategoryAdapterRecyclerView.DocumentCategoryViewHolder holder, int position) {
        final DocumentCategory category = categories.get(position);

        holder.name.setText(category.getName());
        holder.description.setText(category.getDescription());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DocumentsDetailActivity.class);
                intent.putExtra("title", category.getName());
                intent.putParcelableArrayListExtra("documents", category.getDocuments());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class DocumentCategoryViewHolder extends RecyclerView.ViewHolder{
        private CardView container;
        private TextView name;
        private TextView description;

        public DocumentCategoryViewHolder(View itemView){
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.container);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

