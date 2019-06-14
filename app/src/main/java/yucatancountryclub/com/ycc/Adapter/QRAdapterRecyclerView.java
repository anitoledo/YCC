package yucatancountryclub.com.ycc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import yucatancountryclub.com.ycc.DirectoryDetailActivity;
import yucatancountryclub.com.ycc.Model.QR;
import yucatancountryclub.com.ycc.QRDetailActivity;
import yucatancountryclub.com.ycc.R;

/**
 * Created by Zazu on 15/02/18.
 */

public class QRAdapterRecyclerView extends RecyclerView.Adapter<QRAdapterRecyclerView.QRViewHolder>{
    private ArrayList<QR> QRs;
    private int resource;
    private Activity activity;

    public QRAdapterRecyclerView(ArrayList<QR> QRs, int resource, Activity activity) {
        this.QRs = QRs;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public QRAdapterRecyclerView.QRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QRAdapterRecyclerView.QRViewHolder holder, int position) {
        final QR QR = QRs.get(position);
        holder.visitorName.setText(QR.getVisitor_name());
        holder.license_plate.setText(QR.getLicense_plate());

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(QR.getCheck_in());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(String.valueOf(date.getDay()) + '/' + String.valueOf(date.getMonth()+1) + '/' + String.valueOf(date.getYear()+1900));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QRDetailActivity.class);
                intent.putExtra("folioDetail", QR.getPk());
                intent.putExtra("userDetail", QR.getUser());
                intent.putExtra("car", QR.getCar());
                intent.putExtra("car_color", QR.getCar_color());
                intent.putExtra("license_plate", QR.getLicense_plate());
                intent.putExtra("visitorNameDetail", QR.getVisitor_name());
                intent.putExtra("checkInDetail", QR.getCheck_in());
                intent.putExtra("checkOutDetail", QR.getCheck_out());
                intent.putExtra("codeDetail", QR.getCode());
                intent.putExtra("statusDetail", QR.getStatus());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return QRs.size();
    }

    public class QRViewHolder extends RecyclerView.ViewHolder{
        private TextView visitorName;
        private TextView license_plate;
        private TextView date;
        private CardView container;

        public QRViewHolder(View itemView){
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.cardview_qr_container);
            visitorName = (TextView) itemView.findViewById(R.id.visitorName);
            license_plate = (TextView) itemView.findViewById(R.id.license_plate);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}

