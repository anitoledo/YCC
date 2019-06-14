package yucatancountryclub.com.ycc;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import yucatancountryclub.com.ycc.view.fragment.AccessFragment;
import yucatancountryclub.com.ycc.view.fragment.DirectoryFragment;
import yucatancountryclub.com.ycc.view.fragment.ReportsFragment;

public class DirectoryDetailActivity extends AppCompatActivity {

    String phoneNumber;
    String email;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_detail);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);

        showToolbar(true, "Directorio");

        TextView titleDetail = (TextView) findViewById(R.id.contactDetail_title);
        TextView descriptionDetail = (TextView) findViewById(R.id.contactDetail_description);
        TextView locationDetail = (TextView) findViewById(R.id.contactDetail_location);
        TextView scheduleDetail = (TextView) findViewById(R.id.contactDetail_schedule);

        Bundle extras = getIntent().getExtras();

        titleDetail.setText(extras.getString("titleDetail"));
        descriptionDetail.setText(extras.getString("descriptionDetail"));
        locationDetail.setText(extras.getString("locationDetail"));
        scheduleDetail.setText(extras.getString("scheduleDetail"));
        phoneNumber = extras.getString("phoneDetail");
        email = extras.getString("emailDetail");

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

    public void makeCall(View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNumber));

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

    public void sendEmail(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email desde App");
        try {
            startActivity(Intent.createChooser(intent, "Enviar email con..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Aplicación desconocida.", Toast.LENGTH_SHORT).show();
        }
    }
}
