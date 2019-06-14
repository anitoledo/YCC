package yucatancountryclub.com.ycc.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import yucatancountryclub.com.ycc.MakeQRActivity;
import yucatancountryclub.com.ycc.QRRecordActivity;
import yucatancountryclub.com.ycc.R;
import yucatancountryclub.com.ycc.ReportHistoryActivity;
import yucatancountryclub.com.ycc.post.view.NewPostActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessFragment extends Fragment {
    Toolbar toolbar;

    public AccessFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_access, container, false);

        RelativeLayout makeQR = (RelativeLayout) view .findViewById(R.id.makeQR);
        RelativeLayout QRRecord = (RelativeLayout) view.findViewById(R.id.QRRecord);
        makeQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MakeQRActivity.class);
                startActivity(intent);
            }
        });
        QRRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QRRecordActivity.class);
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        showToolbar(false, "Accesos");
        return view;
    }

    public void showToolbar(boolean upButton, String title){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
