package yucatancountryclub.com.ycc;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import yucatancountryclub.com.ycc.Adapter.ImageReportDetailAdapterRecyclerView;

public class ReportDetailActivity extends AppCompatActivity {
    EditText commentText;
    Bundle extras;
    private ProgressDialog pDialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> postsArray;
    ArrayList<String> imagesArray;
    ArrayList<String> imagesReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_report_detail);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);

        showToolbar(true, "Historial");

        commentText = (EditText) findViewById(R.id.commentText);
        TextView titleDetail = (TextView) findViewById(R.id.reportTitle);
        TextView status = (TextView) findViewById(R.id.status);
        TextView reportDescription = (TextView) findViewById(R.id.reportDescription);
        TextView folioDetail = (TextView) findViewById(R.id.reportFolio);
        TextView dateDetail = (TextView) findViewById(R.id.reportDate);
        TextView timeDetail = (TextView) findViewById(R.id.reportTime);
        //ImageView imageReportDetail = (ImageView) findViewById(R.id.imageReportDetail);
        CircleImageView thumbnail = (CircleImageView) findViewById(R.id.thumbnail);
        ListView postsDetail = (ListView) findViewById(R.id.postsList);

        extras = getIntent().getExtras();

        switch (extras.getString("statusDetail")){
            case "pending":
                thumbnail.setImageResource(R.drawable.status_rojo);
                status.setText("Pendiente");
                break;
            case "processing":
                thumbnail.setImageResource(R.drawable.status_amarillo);
                status.setText("En proceso");
                break;
            case "completed":
                thumbnail.setImageResource(R.drawable.ok);
                status.setText("Completado");
                break;
        }
        reportDescription.setText(extras.getString("descriptionDetail"));
        titleDetail.setText(extras.getString("titleDetail"));
        folioDetail.setText(extras.getString("folioDetail"));
        dateDetail.setText(extras.getString("dateDetail"));
        timeDetail.setText(extras.getString("timeDetail"));
        //Picasso.with(this).load(extras.getString("imageDetail")).resize(0,800).into(imageReportDetail);

        //System.out.println(extras.getString("imageDetail"));


        /*imagesArray = extras.getStringArrayList("imageDetail");
        System.out.println("IMAGES SIZE "+imagesArray.size());
        if(imagesArray.size() > 0){
            String imageURL = imagesArray.get(0);
            System.out.println("IMAGE URL "+imageURL);
            //Picasso.with(this).load(imageURL).into(imageReportDetail);
            Glide.with(this).load(imageURL).into(imageReportDetail);
        }*/

        postsArray = extras.getStringArrayList("postsDetail");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, postsArray);
        postsDetail.setAdapter(adapter);


        imagesReport = extras.getStringArrayList("imageDetail");
        System.out.println("IMAGES SIZE "+imagesReport.size());
        /*if(imagesArray.size() > 0){
            String imageURL = imagesArray.get(0);
            System.out.println("IMAGE URL "+imageURL);
            //Picasso.with(this).load(imageURL).into(imageReportDetail);
            Glide.with(this).load(imageURL).into(imageReportDetail);
        }*/

        RecyclerView imagesRecycler = (RecyclerView) findViewById(R.id.imagesRecycler);
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imagesRecycler.setLayoutManager(linearLayoutManager);*/

        imagesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        ImageReportDetailAdapterRecyclerView imagesAdapterRecyclerView = new ImageReportDetailAdapterRecyclerView(imagesReport,R.layout.cardview_report_detail,this);
        imagesRecycler.setAdapter(imagesAdapterRecyclerView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.noanimation, R.anim.slideout);

                break;
        }

        return true;
    }

    public void showToolbar(boolean upButton, String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public JSONObject dataPost(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("report", extras.getString("folioDetail"));
        params.put("user", "1");
        params.put("description", commentText.getText().toString());
        JSONObject data = new JSONObject(params);
        return data;
    }

    public void sendComment(View view){
        pDialog = new ProgressDialog(   this);
        pDialog.setMessage("Enviando...");
        pDialog.show();

        String url = "https://ycc.punklabs.ninja/api/v1/reports/"+extras.getString("folioDetail")+"/posts/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, dataPost(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE "+response.toString());
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
                        /*Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);*/
                        postsArray.add(commentText.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("ERROR "+error.toString());
                        Toast.makeText(getApplicationContext(), "Error al enviar mensaje.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "token "+EndPoints.TOKEN);
                return params;
            }


        };

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}
