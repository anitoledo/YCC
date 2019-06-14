package yucatancountryclub.com.ycc.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import yucatancountryclub.com.ycc.R;
import yucatancountryclub.com.ycc.ReportHistoryActivity;
import yucatancountryclub.com.ycc.post.view.NewPostActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {
    Toolbar toolbar;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        RelativeLayout report = (RelativeLayout) view .findViewById(R.id.report);
        RelativeLayout history = (RelativeLayout) view.findViewById(R.id.history);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportHistoryActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        showToolbar(false, "Reportes");
        return view;
    }
    public void showToolbar(boolean upButton, String title){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
