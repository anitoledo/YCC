package yucatancountryclub.com.ycc;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class QRDetailActivity extends AppCompatActivity {
    private RelativeLayout share;
    Bitmap bitmap ;
    public final static int QRcodeWidth = 150 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdetail);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);
        showToolbar(true, "Accesos");

        TextView visitorNameDetail = (TextView) findViewById(R.id.visitorNameDetail);
        TextView checkInDetail = (TextView) findViewById(R.id.checkInDetail);
        TextView car = (TextView) findViewById(R.id.car);
        TextView car_color = (TextView) findViewById(R.id.car_color);
        TextView license_plate = (TextView) findViewById(R.id.license_plate);
        TextView checkOutDetail = (TextView) findViewById(R.id.checkOutDetail);
        ImageView QRImage = (ImageView) findViewById(R.id.QRImage);
        share = (RelativeLayout) findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"qr", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                share.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                startActivity(Intent.createChooser(share, "Compartir"));*/

                /*try{
                    File path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File imageFile = File.createTempFile("image", ".png", path);
                    FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutPutStream);
                    fileOutPutStream.flush();
                    fileOutPutStream.close();
                    Uri uri = MyFileProvider.getUriForFile(getBaseContext(),"${applicationId}.com.vansuita.pickimage.provider", imageFile);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/jpeg");
                    getBaseContext().startActivity(Intent.createChooser(shareIntent, "Share"));

                }catch (IOException e) {e.printStackTrace();}*/


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));

            }
        });

        Bundle extras = getIntent().getExtras();

        try {
            bitmap = TextToImageEncode(extras.getString("codeDetail"));
            QRImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        visitorNameDetail.setText(extras.getString("visitorNameDetail"));
        car.setText(extras.getString("car"));
        license_plate.setText(extras.getString("license_plate"));
        car_color.setText(extras.getString("car_color"));

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(extras.getString("checkInDetail"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Check in: "+date);

        String time = String.valueOf(date.getHours()) + ':' + String.valueOf(date.getMinutes());
        String day = String.valueOf(date.getDay()) + '/' + String.valueOf(date.getMonth()+1) + '/' + String.valueOf(date.getYear()+1900);
        checkInDetail.setText(time);
        checkOutDetail.setText(day);
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

    public Bitmap TextToImageEncode(String Value) throws WriterException {
        Bitmap bmp = null;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(Value, BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeWidth, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x , y, bitMatrix.get(x,y) ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.white));
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
