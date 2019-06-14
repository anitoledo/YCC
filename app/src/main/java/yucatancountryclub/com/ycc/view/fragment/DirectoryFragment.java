package yucatancountryclub.com.ycc.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yucatancountryclub.com.ycc.Adapter.ContactAdapterRecyclerView;
import yucatancountryclub.com.ycc.EndPoints;
import yucatancountryclub.com.ycc.Model.Contact;
import yucatancountryclub.com.ycc.MySingleton;
import yucatancountryclub.com.ycc.PrefManager;
import yucatancountryclub.com.ycc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectoryFragment extends Fragment {

    ArrayList<Contact> contacts = new ArrayList<>();
    private ProgressDialog pDialog;
    Toolbar toolbar;

    public DirectoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_directory, container, false);
        View rootView = inflater.inflate(R.layout.actionbar_toolbar, container, false);

        final RecyclerView contactsRecycler = (RecyclerView) view.findViewById(R.id.contactRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contactsRecycler.setLayoutManager(linearLayoutManager);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_from_right);
        contactsRecycler.setLayoutAnimation(animation);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando...");
        pDialog.show();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        showToolbar(false, "Directorio");

        /*ContactAdapterRecyclerView contactAdapterRecyclerView = new ContactAdapterRecyclerView(buildContacts(),R.layout.cardview_contact,getActivity());
        contactsRecycler.setAdapter(contactAdapterRecyclerView);*/
        buildContacts(new CallBack() {
            @Override
            public void onSuccess(ArrayList<Contact> contacts) {
                ContactAdapterRecyclerView contactAdapterRecyclerView = new ContactAdapterRecyclerView(contacts,R.layout.cardview_contact,getActivity());
                contactsRecycler.setAdapter(contactAdapterRecyclerView);
            }

            @Override
            public void onFail(String msg) {
                // Do Stuff
            }
        });

        return view;

    }

    public void showToolbar(boolean upButton, String title){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    /*public ArrayList<Contact> buildContacts(){
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < EndPoints.directory.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = EndPoints.directory.getJSONObject(i);
                contacts.add(new Contact(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("location"),
                        jsonObject.getString("hours"),
                        jsonObject.getString("phone1"),
                        jsonObject.getString("email")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }*/
    public void buildContacts(final CallBack onCallBack) {
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        String url = "https://ycc.punklabs.ninja/api/v1/directory/";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("RESPONSE "+response.toString());
                        contacts.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                contacts.add(new Contact(
                                        jsonObject.getString("name"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("hours"),
                                        jsonObject.getString("phone1"),
                                        jsonObject.getString("email")));
                            }
                            hidePDialog();
                            onCallBack.onSuccess(contacts);
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

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
        System.out.println("RESPONSE holi"+contacts.toString());
    }


    public interface CallBack {
        void onSuccess(ArrayList<Contact> contacts);

        void onFail(String msg);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
