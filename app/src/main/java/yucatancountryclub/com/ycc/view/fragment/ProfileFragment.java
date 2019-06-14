package yucatancountryclub.com.ycc.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import yucatancountryclub.com.ycc.Adapter.ContactAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.DocumentCategoryAdapterRecyclerView;
import yucatancountryclub.com.ycc.Adapter.ImageReportAdapterRecyclerView;
import yucatancountryclub.com.ycc.EndPoints;
import yucatancountryclub.com.ycc.MainActivity;
import yucatancountryclub.com.ycc.MakeQRActivity;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.Model.Document;
import yucatancountryclub.com.ycc.Model.DocumentCategory;
import yucatancountryclub.com.ycc.Model.ImageReport;
import yucatancountryclub.com.ycc.MySingleton;
import yucatancountryclub.com.ycc.R;


public class ProfileFragment extends Fragment {
    private ProgressDialog pDialog;
    Toolbar toolbar;
    BottomSheetDialog dialog;
    TextView profileName;
    ArrayList<DocumentCategory> categories = new ArrayList<>();
    DocumentCategoryAdapterRecyclerView documentCategoryAdapterRecyclerView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setUserData();

        /*TextView logout = (TextView) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });*/

        dialog = new BottomSheetDialog(getActivity());

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ImageView more_menu = (ImageView) view.findViewById(R.id.more_menu);
        profileName = (TextView) view.findViewById(R.id.profileName);



        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.more_menu_layout, null);
        dialog.setContentView(sheetView);

        more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog();
            }
        });

        showToolbar(false, "Perfil");

        RecyclerView documentsRecycler = (RecyclerView) view.findViewById(R.id.documentsRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentsRecycler.setLayoutManager(linearLayoutManager);

        documentCategoryAdapterRecyclerView = new DocumentCategoryAdapterRecyclerView(categories,R.layout.cardview_document_category,getActivity());
        documentsRecycler.setAdapter(documentCategoryAdapterRecyclerView);

        return view;
    }

    public void showToolbar(boolean upButton, String title){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void menuDialog(){
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.more_menu_layout, null);
        dialog.setContentView(sheetView);

        LinearLayout logout = (LinearLayout) sheetView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        TextView cancel = (TextView) sheetView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void logout(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cerrando sesión...");
        pDialog.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, EndPoints.LOGOUT_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("LOGOUT "+response);
                        pDialog.dismiss();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error "+error.toString());
                        error.printStackTrace();

                        Toast.makeText(getActivity(), "Error al cerrar sesión.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
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

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    private void setUserData(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando...");
        pDialog.show();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, EndPoints.USER_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("USER "+response);
                        try {
                            profileName.setText(response.getString("first_name")+" "+response.getString("last_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        buildDocuments();
                        //pDialog.dismiss();

                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error "+error.toString());
                        error.printStackTrace();

                        Toast.makeText(getActivity().getApplicationContext(), "Error al cargar datos.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
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

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void buildDocuments(){
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, EndPoints.DOCUMENT_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("DOCUMENTS "+response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);


                                ArrayList<Document> documents = new ArrayList<Document>();
                                if(jsonObject.getJSONArray("documents").length() > 0) {
                                    JSONArray documentsArray = jsonObject.getJSONArray("documents");
                                    for (int j = 0; j < documentsArray.length(); j++) {
                                        JSONObject document = documentsArray.getJSONObject(j);
                                        Document documentModel = new Document(
                                                document.getString("name"),
                                                document.getString("description"),
                                                document.getString("file")
                                        );
                                        //String description = post.getString("description");
                                        documents.add(documentModel);
                                    }
                                }

                                categories.add(new DocumentCategory(
                                    jsonObject.getString("name"),
                                    jsonObject.getString("description"),
                                    documents
                                ));
                            }
                            documentCategoryAdapterRecyclerView.notifyDataSetChanged();
                            pDialog.dismiss();
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
                        pDialog.dismiss();
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

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }


}
