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

import yucatancountryclub.com.ycc.Adapter.QRAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.ReportAdapterRecyclerView;
import yucatancountryclub.com.ycc.Model.QR;
import yucatancountryclub.com.ycc.Model.Report;

public class QRRecordActivity extends AppCompatActivity {
    ArrayList<QR> QRs = new ArrayList<>();
    private ProgressDialog pDialog;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrrecord);
        showToolbar(true, "Historial");

        final RecyclerView qrsRecycler = (RecyclerView) findViewById(R.id.qrRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        qrsRecycler.setLayoutManager(linearLayoutManager);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();

        buildQRs(new CallBack() {
            @Override
            public void onSuccess(ArrayList<QR> QRs) {
                QRAdapterRecyclerView qrAdapterRecyclerView = new QRAdapterRecyclerView(QRs, R.layout.cardview_qr, activity);
                qrsRecycler.setAdapter(qrAdapterRecyclerView);
                qrsRecycler.scheduleLayoutAnimation();
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

    public void buildQRs(final CallBack onCallBack) {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, EndPoints.QR_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        QRs.clear();
                        System.out.println("QR "+response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                QRs.add(new QR(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("user"),
                                            jsonObject.getString("visitor_name"),
                                            jsonObject.getString("check_in"),
                                            jsonObject.getString("check_out"),
                                            jsonObject.getString("code"),
                                            jsonObject.getString("status"),
                                            jsonObject.getString("car_model"),
                                            jsonObject.getString("license_plate"),
                                            jsonObject.getString("car_color")
                                        )
                                );
                            }
                            hidePDialog();
                            onCallBack.onSuccess(QRs);
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
        void onSuccess(ArrayList<QR> QRs);
        void onFail(String msg);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
