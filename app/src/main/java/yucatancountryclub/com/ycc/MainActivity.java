package yucatancountryclub.com.ycc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ProgressDialog pDialog;
    private TextInputEditText username;
    private TextInputEditText password;
    private PrefManager prefManager;
    String registr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);

        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences("ANI", MODE_PRIVATE);
        registr = prefs.getString("registration_id", null);
        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
    }

    public JSONObject dataPost(){


        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        System.out.println("H FIREBASE ID "+prefManager.getRegistrationId());
        params.put("registration_id", registr);
        System.out.println("H FIREBASE ID "+prefManager.getRegistrationId());
        params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        params.put("device_type", "android");
        JSONObject data = new JSONObject(params);
        return data;
    }

    public void login(View view){
        /*Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);*/
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando...");
        pDialog.show();

        System.out.println("URL "+EndPoints.LOGIN_URL);
        System.out.println("DATA "+dataPost().toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, EndPoints.LOGIN_URL, dataPost(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE "+response.toString());
                        try {
                            //pDialog.dismiss();
                                System.out.println("H FIREBASE ID "+registr);
                                //prefManager.setToken(response.getString("key"));
                                EndPoints.TOKEN = response.getString("key");
                                device();
                                //setDirectory();
                                //pDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al iniciar sesión.", Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("ERROR "+error.toString());
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al iniciar sesión.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }


        };

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    public void device(){
        /*Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);*/
        //System.out.println("URL "+EndPoints.LOGIN_URL);
        //System.out.println("DATA "+dataPost().toString());
        System.out.println("FIREBASE TOKEN "+prefManager.getRegistrationId());
        System.out.println("H FIREBASE ID "+registr);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, EndPoints.DEVICE_URL, dataPost(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        prefManager.setToken(EndPoints.TOKEN);
                        System.out.println("RESPONSE "+response.toString());

                        pDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                        startActivity(intent);
                        System.out.println("H FIREBASE ID "+registr);
                        System.out.println("ANI "+prefManager.isFirstTimeLaunch());
                        System.out.println("ANI "+prefManager.getLogged());
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("ERROR "+error.toString());
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al iniciar sesión.", Toast.LENGTH_LONG).show();
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

    public void forgot(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set title
                .setTitle("Contraseña olvidada")
                //set message
                .setMessage("Profavor marca al 000 000 0000 para obtener una nueva.")
                //set positive button
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                    }
                })
                //set negative button
                .setNegativeButton("Llamar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        makeCall();
                    }
                })
                .show();
    }

    public void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: 000 000 0000"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        }else {
            try{
                startActivity(intent);
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"Llamada no permitida.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  boolean checkAndRequestPermissions() {
        int makeCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (makeCallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("PERMISSIONS", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("PERMISSIONS", "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("PERMISSIONS", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void setDirectory(){
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, EndPoints.DIRECTORY_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        EndPoints.directory = response;
                        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "Error al cargar directorio.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        System.out.println("ERROR "+error.toString());
                        error.printStackTrace();
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

}
