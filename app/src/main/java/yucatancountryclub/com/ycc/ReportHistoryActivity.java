package yucatancountryclub.com.ycc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yucatancountryclub.com.ycc.Adapter.ContactAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.ReportAdapterRecyclerView;
import yucatancountryclub.com.ycc.Model.Report;

public class ReportHistoryActivity extends AppCompatActivity {
    ArrayList<Report> reports = new ArrayList<>();
    private ProgressDialog pDialog;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);
        showToolbar(true, "Historial");

        final RecyclerView reportsRecycler = (RecyclerView) findViewById(R.id.reportRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportsRecycler.setLayoutManager(linearLayoutManager);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();

        buildReports(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Report> reports) {
                ReportAdapterRecyclerView reportAdapterRecyclerView = new ReportAdapterRecyclerView(reports, R.layout.cardview_report, activity);
                reportsRecycler.setAdapter(reportAdapterRecyclerView);
                reportsRecycler.scheduleLayoutAnimation();
            }

            @Override
            public void onFail(String msg) {
                // Do Stuff
            }
        });

    }

    public void showToolbar(boolean upButton, String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void buildReports(final CallBack onCallBack) {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, EndPoints.REPORT_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        reports.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                System.out.println("HISTORIAL "+jsonObject.toString());
                                String date = jsonObject.getString("created_at").substring(0 , 10);
                                String time = jsonObject.getString("created_at").substring(11 , 19);

                                /*String image = null;
                                if(!jsonObject.isNull("image_report")) {
                                    JSONObject image_report = jsonObject.getJSONObject("image_report");
                                    image = image_report.getString("image");
                                }*/
                                ArrayList<String> imagesArray =  new ArrayList<String>();
                                if(jsonObject.getJSONArray("image_report").length() > 0) {
                                    JSONArray images = jsonObject.getJSONArray("image_report");
                                    for (int j = 0; j < images.length(); j++) {
                                        JSONObject image = images.getJSONObject(j);
                                        String imageURL = image.getString("image");
                                        imagesArray.add(imageURL);
                                    }
                                }

                                ArrayList<String> posts =  new ArrayList<String>();
                                if(jsonObject.getJSONArray("posts").length() > 0) {
                                    System.out.println("Post" + jsonObject.getJSONArray("posts"));
                                    JSONArray jsonArray = jsonObject.getJSONArray("posts");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject post = jsonArray.getJSONObject(j);
                                        String description = post.getString("description");
                                        posts.add(description);
                                    }
                                }

                                reports.add(new Report(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user"),
                                        jsonObject.getString("subject"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("status"),
                                        date,
                                        time,
                                        imagesArray,
                                        posts));
                            }
                            hidePDialog();
                            onCallBack.onSuccess(reports);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("ERROR "+error.toString());
                        error.printStackTrace();
                        hidePDialog();
                        onCallBack.onFail(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "token "+ EndPoints.TOKEN);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }


    public interface CallBack {
        void onSuccess(ArrayList<Report> reports);
        void onFail(String msg);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
