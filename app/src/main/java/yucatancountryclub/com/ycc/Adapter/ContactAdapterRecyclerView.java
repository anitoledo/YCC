package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yucatancountryclub.com.ycc.DirectoryDetailActivity;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class ContactAdapterRecyclerView extends RecyclerView.Adapter<ContactAdapterRecyclerView.ContactViewHolder>{
    private ArrayList<Contact> contacts;
    private int resource;
    private Activity activity;

    public ContactAdapterRecyclerView(ArrayList<Contact> contacts, int resource, Activity activity) {
        this.contacts = contacts;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ContactAdapterRecyclerView.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactAdapterRecyclerView.ContactViewHolder holder, int position) {
        final Contact contact = contacts.get(position);
        holder.title.setText(contact.getTitle());
        holder.schedule.setText(contact.getSchedule());
        switch (contact.getTitle()){
            case "Pórtico":
                holder.thumbnail.setImageResource(R.drawable.portico);
                break;
            case "Mantenimiento":
                holder.thumbnail.setImageResource(R.drawable.mantenimiento);
                break;
            case "Seguridad":
                holder.thumbnail.setImageResource(R.drawable.seguridad);
                break;
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DirectoryDetailActivity.class);
                intent.putExtra("titleDetail", contact.getTitle());
                intent.putExtra("descriptionDetail", contact.getDescription());
                intent.putExtra("locationDetail", contact.getLocation());
                intent.putExtra("scheduleDetail", contact.getSchedule());
                intent.putExtra("phoneDetail", contact.getPhone());
                intent.putExtra("emailDetail", contact.getEmail());


                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                            Pair.create((View)holder.title, "title"),
                            Pair.create((View)holder.schedule, "schedule")
                    );
                    activity.startActivity(intent, options.toBundle());
                    activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    activity.startActivity(intent);
                }*/

                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView schedule;
        private CardView container;
        private ImageView thumbnail;

        public ContactViewHolder(View itemView){
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.cardview_container);
            title = (TextView) itemView.findViewById(R.id.contactName);
            schedule = (TextView) itemView.findViewById(R.id.schedule);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}

