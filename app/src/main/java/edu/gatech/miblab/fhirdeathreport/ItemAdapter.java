package edu.gatech.miblab.fhirdeathreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    //private ArrayList<String> patientNamesArr = new ArrayList<>();
    String[] patientNames;
    String[] birthDates;
    LayoutInflater mInflater;

    public ItemAdapter(Context c, String[] i, String[] j) {
        patientNames = i;
        birthDates = j;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return patientNames.length;
    }

    @Override
    public Object getItem(int position) {
        return patientNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.patient_name_display_page, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.patientNameOutputTextView);
        TextView DOBTextView = (TextView) v.findViewById(R.id.patientDOBOutputTextView);

        String name = patientNames[position];
        String DOB = birthDates[position];
        nameTextView.setText(name);
        DOBTextView.setText(DOB);
        return v;
    }
}
