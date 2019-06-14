package yucatancountryclub.com.ycc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import yucatancountryclub.com.ycc.Adapter.DocumentAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.ReportAdapterRecyclerView;
import yucatancountryclub.com.ycc.Model.Document;
import yucatancountryclub.com.ycc.Model.Report;

public class DocumentsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_detail);
        overridePendingTransition(R.anim.slidein, R.anim.noanimation);
        Bundle extras = getIntent().getExtras();

        showToolbar(true, extras.getString("title"));

        final RecyclerView documentsRecycler = (RecyclerView) findViewById(R.id.documentsRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentsRecycler.setLayoutManager(linearLayoutManager);

        ArrayList<Document> documents = extras.getParcelableArrayList("documents");

        DocumentAdapterRecyclerView documentAdapterRecyclerView = new DocumentAdapterRecyclerView(documents, R.layout.cardview_document, this);
        documentsRecycler.setAdapter(documentAdapterRecyclerView);
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
}
