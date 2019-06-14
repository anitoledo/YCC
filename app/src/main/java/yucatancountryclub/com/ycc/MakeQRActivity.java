package yucatancountryclub.com.ycc;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MakeQRActivity extends AppCompatActivity {
    static EditText setDate;
    static EditText setTime;
    public EditText visitorName;
    public EditText carModel;
    public EditText carColor;
    public EditText placas;
    public static int hour_int;
    public static int minute_int;
    public static int day_int;
    public static int month_int;
    public static int year_int;
    private ProgressDialog pDialog;
    public Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_qr);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);
        showToolbar(true, "Accesos");

        carModel = (EditText) findViewById(R.id.carModel);
        carColor = (EditText) findViewById(R.id.carColor);
        placas = (EditText) findViewById(R.id.placas);
        visitorName = (EditText) findViewById(R.id.visitorName);
        setDate = (EditText) findViewById(R.id.setDate);
        setTime = (EditText) findViewById(R.id.setTime);
        RelativeLayout makeQR = (RelativeLayout) findViewById(R.id.makeQR);

        makeQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQR();
            }
        });

        dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"1 hora", "2 horas", "3 horas", "4 horas", "5 horas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        setDate.setKeyListener(null);
        setTime.setKeyListener(null);


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
                overridePendingTransition(R.anim.noanimation, R.anim.slideout);

                break;
        }

        return true;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // create date object using date set by user
            setDate.setText(day + " / " + (month + 1) + " / " + year);

            year_int = year-1900;
            month_int = month;
            day_int = day;
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            setTime.setText("  "+hourOfDay + "  :  " + minute + "  :  00");
            //time = new Time(hourOfDay, minute, 0);
            //DateFormat.format("hh-mm-ss", time);
            hour_int = hourOfDay;
            minute_int = minute;
        }
    }

    public JSONObject dataPost(){
        String hour = dropdown.getSelectedItem().toString().substring(0,1);

        Date date_checkin = new Date(year_int,month_int,day_int);
        date_checkin.setHours(hour_int);
        date_checkin.setMinutes(minute_int);
        date_checkin.setSeconds(0);

        Calendar calendar_in = Calendar.getInstance();
        calendar_in.setTime(date_checkin);

        Calendar calendar_out = Calendar.getInstance();
        calendar_out.setTime(date_checkin);
        calendar_out.add(Calendar.HOUR, Integer.parseInt(hour));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        String checkout = dateFormat.format(calendar_out.getTime()) + "T" + hourFormat.format(calendar_out.getTime());
        String checkin = dateFormat.format(calendar_in.getTime())  + "T" + hourFormat.format(calendar_in.getTime());

        //Date date_checkout = new Date(date_checkin.getTime() + 2 * Integer.parseInt(hour));

        //String checkin = date_checkin.getYear()+"-"+date_checkin.getMonth()+"-"+date_checkin.getDay()+"T"+date_checkin.getHours()+":"+date_checkin.getMinutes()+":00";
        //String checkout = date_checkout.getYear()+"-"+date_checkout.getMonth()+"-"+date_checkout.getDay()+"T"+date_checkout.getHours()+":"+date_checkout.getMinutes()+":00";

        System.out.println("DATETIME CHECKIN "+date_checkin);
        System.out.println("TIME CHECKIN "+checkin);
        System.out.println("TIME CHECKout "+checkout);

        Map<String, String> params = new HashMap<String, String>();
        params.put("visitor_name", visitorName.getText().toString());
        params.put("license_plate", placas.getText().toString());
        params.put("car_model", carModel.getText().toString());
        params.put("car_color", carColor.getText().toString());
        params.put("check_in", checkin.toString());
        params.put("check_out", checkout.toString());
        JSONObject data = new JSONObject(params);
        return data;
    }

    private void postQR(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, EndPoints.QR_URL, dataPost(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE "+response.toString());
                        pDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), QRDetailActivity.class);
                        try {
                            intent.putExtra("folioDetail", response.getString("id"));
                            intent.putExtra("userDetail", response.getString("user"));
                            intent.putExtra("visitorNameDetail", response.getString("visitor_name"));
                            intent.putExtra("checkInDetail", response.getString("check_in"));
                            intent.putExtra("checkOutDetail", response.getString("check_out"));
                            intent.putExtra("codeDetail", response.getString("code"));
                            intent.putExtra("car", response.getString("car_model"));
                            intent.putExtra("car_color", response.getString("car_color"));
                            intent.putExtra("license_plate", response.getString("license_plate"));
                            intent.putExtra("statusDetail", response.getString("status"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getApplicationContext().startActivity(intent);
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
