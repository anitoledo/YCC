package yucatancountryclub.com.ycc.post.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yucatancountryclub.com.ycc.Adapter.ContactAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.ImageReportAdapterRecyclerView;
import yucatancountryclub.com.ycc.EndPoints;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.Model.ImageReport;
import yucatancountryclub.com.ycc.MySingleton;
import yucatancountryclub.com.ycc.R;
import yucatancountryclub.com.ycc.RealPathUtil;

public class NewPostActivity extends AppCompatActivity  {

    private static final int REQUEST_CAMERA = 1;
    private static int PICK_IMAGE_MULTIPLE = 2;
    private ImageView imgPhoto;
    private ProgressDialog pDialog;
    private EditText subjectReport;
    private EditText textReport;
    private ImageView delete;
    Dialog confirmDialog;
    ImageView okButton;
    ImageView closeButton;
    LinearLayout tookPhoto;
    LinearLayout chooseGallery;
    LinearLayout focusEditText;
    TextView cancel;
    Activity activity;
    HttpEntity resEntity;
    ArrayList<String> imagesEncodedList;
    private String imgPath;
    Uri imgUri;
    ArrayList<ImageReport> imagesReport = new ArrayList<>();
    ImageReportAdapterRecyclerView imagesAdapterRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);
        showToolbar(true);

        subjectReport = (EditText) findViewById(R.id.subjectReport);
        textReport = (EditText) findViewById(R.id.textReport);
        //imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        //delete = (ImageView) findViewById(R.id.delete);
        focusEditText = (LinearLayout) findViewById(R.id.focusEditText);

        confirmDialog = new Dialog(this);

        imagesEncodedList = new ArrayList<String>();

        ImageView addPhoto = (ImageView) findViewById(R.id.addImage);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addPicture();
                chooseCamera();
            }
        });

        activity = this;

        TextView sendReport = (TextView) findViewById(R.id.sendReport);
        focusEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textReport.setFocusableInTouchMode(true);
                textReport.requestFocus();
            }
        });
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!subjectReport.getText().toString().isEmpty() && !textReport.getText().toString().isEmpty()){
                    //showDialog();
                    sendReport();
                    //uploadPhoto(null, null);
                } else{
                    Toast.makeText(getApplicationContext(), "Falta asunto o descripción del reporte.", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });*/

        RecyclerView imagesRecycler = (RecyclerView) findViewById(R.id.imagesRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imagesRecycler.setLayoutManager(linearLayoutManager);

        imagesAdapterRecyclerView = new ImageReportAdapterRecyclerView(imagesReport,R.layout.cardview_images_report,this);
        imagesRecycler.setAdapter(imagesAdapterRecyclerView);

    }

    public void showDialog(){
        confirmDialog.setContentView(R.layout.confirm_report);
        closeButton = (ImageView) confirmDialog.findViewById(R.id.closeButton);
        okButton = (ImageView) confirmDialog.findViewById(R.id.okButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });

        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.show();
    }

    public void chooseCamera(){
        confirmDialog.setContentView(R.layout.choose_image);
        cancel = (TextView) confirmDialog.findViewById(R.id.cancel);
        tookPhoto = (LinearLayout) confirmDialog.findViewById(R.id.tookPhoto);
        chooseGallery = (LinearLayout) confirmDialog.findViewById(R.id.chooseGallery);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        tookPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Multiple permissions
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                Permissions.check(getApplicationContext(), permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        addPicture();
                    }
                });

            }
        });


        chooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions.check(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        chooseFromGallery();
                    }
                });
            }
        });


        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.show();

    }

    public void showToolbar(boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    private void addPicture(){

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, REQUEST_CAMERA);

    }
    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);

    }


    public void delete(){
        imgPhoto.setImageDrawable(null);
        delete.setVisibility(View.GONE);
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".jpg");
        imgUri = Uri.fromFile(file);
        imgPath = file.getAbsolutePath();
        return imgUri;
    }
    private String getPathFromUri(Uri uri) {
        if (Build.VERSION.SDK_INT < 11)
            return RealPathUtil.getRealPathFromURI_BelowAPI11(getApplicationContext(), uri);
        else if (Build.VERSION.SDK_INT < 19)
            return RealPathUtil.getRealPathFromURI_API11to18(getApplicationContext(), uri);
        else
            return RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAMERA && resultCode == this.RESULT_OK){
            if((imagesReport.size() + 1) <= 10) {
                //System.out.println("URI PATH " + imgPath);
                //Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imgPhoto.setImageBitmap(photo);
                //Picasso.with(this).load(imgUri).into(imgPhoto);
                //delete.setVisibility(Button.VISIBLE);
                //imagesEncodedList.add(imgPath);
                imagesReport.add(new ImageReport(imgUri, imgPath));
                imagesAdapterRecyclerView.notifyDataSetChanged();
            } else{
                Toast.makeText(getApplicationContext(), "Máximo 10 fotos.", Toast.LENGTH_LONG).show();
            }

        }

        if (requestCode==PICK_IMAGE_MULTIPLE && resultCode == this.RESULT_OK) {
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                if((imagesReport.size() + count) <= 10) {
                    Uri imageUri = null;
                    String imagePath = null;
                    for (int i = 0; i < count; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        imagePath = getPathFromUri(imageUri);
                        //imagesEncodedList.add(imagePath);
                        imagesReport.add(new ImageReport(imageUri, imagePath));
                        imagesAdapterRecyclerView.notifyDataSetChanged();
                    }
                    //Picasso.with(this).load(imageUri).into(imgPhoto);
                    //delete.setVisibility(View.VISIBLE);
                } else{
                    Toast.makeText(getApplicationContext(), "Máximo 10 fotos.", Toast.LENGTH_LONG).show();
                }

            } else if(data.getData() != null) {
                if((imagesReport.size() + 1) <= 10) {
                    String imagePath = getPathFromUri(data.getData());
                    Uri imageData = data.getData();
                    //imagesEncodedList.add(imagePath);
                    //Picasso.with(this).load(imageData).into(imgPhoto);
                    //delete.setVisibility(View.VISIBLE);
                    imagesReport.add(new ImageReport(imageData, imagePath));
                    imagesAdapterRecyclerView.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(), "Máximo 10 fotos.", Toast.LENGTH_LONG).show();
                }
            }
        }
        System.out.println("IMAGES ARRAY SIZE  "+imagesReport.size());
        confirmDialog.dismiss();
    }



    /*private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File photo = File.createTempFile(imageFileName, ".jpg", storageDir);

        photoPathTemp = "file:" + photo.getAbsolutePath();

        return photo;

    }*/

    private void sendReport(){
        confirmDialog.dismiss();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        System.out.println("TOKEN "+EndPoints.TOKEN);

        String url = "https://ycc.punklabs.ninja/api/v1/reports/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, dataPost(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE "+response.toString());
                        try {
                            String pk = response.getString("id");
                            if(!pk.isEmpty() && imagesReport.size() > 0){
                                //imgPhoto.buildDrawingCache();
                                //Bitmap bitmap = ((BitmapDrawable) imgPhoto.getDrawable()).getBitmap();

                                uploadPhoto(pk);
                            } else{
                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
                                finish();
                            }
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

    public JSONObject dataPost(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("subject", subjectReport.getText().toString());
        params.put("description", textReport.getText().toString());
        JSONObject data = new JSONObject(params);
        return data;
    }

    public void uploadPhoto(final String pk) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {


                    //File file1 = new File(pathPhoto);
                   // File file2 = new File(pathPhoto);

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(EndPoints.UPLOAD_URL);
                    //FileBody bin1 = new FileBody(file1);
                    //FileBody bin2 = new FileBody(file2);
                    MultipartEntity reqEntity = new MultipartEntity();
                    post.addHeader("Authorization", "token " + EndPoints.TOKEN);
                    //reqEntity.addPart("image", bin1);
                    //reqEntity.addPart("image", bin2);
                    //reqEntity.addPart("image",  new FileBody(new File(pathPhoto)));
                    for(int i = 0; i < imagesReport.size(); i++){
                        ImageReport imageReport = imagesReport.get(i);
                        reqEntity.addPart("image",  new FileBody(new File(imageReport.getPath())));
                    }
                    reqEntity.addPart("report", new StringBody(pk));
                    post.setEntity(reqEntity);
                    HttpResponse response = client.execute(post);
                    resEntity = response.getEntity();
                    final String response_str = EntityUtils.toString(resEntity);
                    if (resEntity != null) {
                        Log.i("RESPONSE", response_str);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
                                    finish();
                                } catch (Exception e) {
                                    pDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error al enviar mensaje.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        });

        thread.start();

    }





}
