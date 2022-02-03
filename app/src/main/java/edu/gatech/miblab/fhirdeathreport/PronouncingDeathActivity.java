package edu.gatech.miblab.fhirdeathreport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PronouncingDeathActivity extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronouncing_death);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("patientBundle");

        System.out.println("Pronouncing Death Switched");
        System.out.println(bundle.getString("familyName"));

        Spinner spinnerDeathPlaces = (Spinner) findViewById(R.id.patientpronouncingDeath_deathPlace_spinner);
        ArrayAdapter<String> adapterDeathPlaces = new ArrayAdapter<String>(PronouncingDeathActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.deathPlaces));
        adapterDeathPlaces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeathPlaces.setAdapter(adapterDeathPlaces);

        TextView patientNameTextView = (TextView) findViewById(R.id.PatientBasicInfo_patientNameTextView);

        String fullName = bundle.getString("fullName");
        patientNameTextView.setText(fullName);

        Button basicInfoButton = (Button) findViewById(R.id.PatientBasicInfo_BasicInfoButton);
        Button pronouncingDeathButton = (Button) findViewById(R.id.patientBasicInfo_PronouncingDeathButton);
        Button causalChainButton = (Button) findViewById(R.id.patientBasicInfo_CausalChainButton);
        Button reviewSummitButton = (Button) findViewById(R.id.patientBasicInfo_ReviewSummit);

        /*basicInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on Basic Patient Info Button!");

                EditText deathDateEditText = findViewById(R.id.patientpronouncingDeath_deathDate_editText);
                EditText deathTimeEditText = findViewById(R.id.patientpronouncingDeath_deathTime_editText);
                EditText name_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_name_placeOfDeath_editText);
                EditText street_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_street_placeOfDeath_editText);
                EditText city_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_city_placeOfDeath_editView);
                EditText state_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_state_placeOfDeath_editView);
                EditText zip_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_zip_placeOfDeath_editView);
                EditText signature_documentEditText = findViewById(R.id.patientpronouncingDeath_sign_document_editText);
                EditText license_documentEditText = findViewById(R.id.patientpronouncingDeath_license_editText);

                String deathDate = deathDateEditText.getText().toString();
                String deathTime = deathTimeEditText.getText().toString();
                String deathPlace = spinnerDeathPlaces.getSelectedItem().toString();
                String name_placeOfDeath = name_placeOfDeathEditText.getText().toString();
                String street_placeOfDeath = street_placeOfDeathEditText.getText().toString();
                String city_placeOfDeath = city_placeOfDeathEditText.getText().toString();
                String state_placeOfDeath = state_placeOfDeathEditText.getText().toString();
                String zip_placeOfDeath = zip_placeOfDeathEditText.getText().toString();
                String signature_document = signature_documentEditText.getText().toString();
                String license_document = license_documentEditText.getText().toString();

                if (!bundle.containsKey("deathDate")) bundle.putString("deathDate", deathDate);
                if (!bundle.containsKey("deathTime")) bundle.putString("deathTime", deathTime);
                if (!bundle.containsKey("deathPlace")) bundle.putString("deathPlace", deathPlace);
                if (!bundle.containsKey("name_placeOfDeath")) bundle.putString("name_placeOfDeath", name_placeOfDeath);
                if (!bundle.containsKey("street_placeOfDeath")) bundle.putString("street_placeOfDeath", street_placeOfDeath);
                if (!bundle.containsKey("city_placeOfDeath")) bundle.putString("city_placeOfDeath", city_placeOfDeath);
                if (!bundle.containsKey("state_placeOfDeath")) bundle.putString("state_placeOfDeath", state_placeOfDeath);
                if (!bundle.containsKey("zip_placeOfDeath")) bundle.putString("zip_placeOfDeath", zip_placeOfDeath);
                if (!bundle.containsKey("signature_document")) bundle.putString("signature_document", signature_document);
                if (!bundle.containsKey("license_document")) bundle.putString("license_document", license_document);

                Intent showBasicInfo = new Intent(getApplicationContext(), PatientBasicInfoActivity.class);
                showBasicInfo.putExtra("patientBundle", bundle);
                startActivity(showBasicInfo);
                finish();
            }
        });*/

        pronouncingDeathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText deathDateEditText = findViewById(R.id.patientpronouncingDeath_deathDate_editText);
                EditText deathTimeEditText = findViewById(R.id.patientpronouncingDeath_deathTime_editText);
                EditText name_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_name_placeOfDeath_editText);
                EditText street_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_street_placeOfDeath_editText);
                EditText city_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_city_placeOfDeath_editView);
                EditText state_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_state_placeOfDeath_editView);
                EditText zip_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_zip_placeOfDeath_editView);
                EditText signature_documentEditText = findViewById(R.id.patientpronouncingDeath_sign_document_editText);
                EditText license_documentEditText = findViewById(R.id.patientpronouncingDeath_license_editText);

                String deathDate = deathDateEditText.getText().toString();
                String deathTime = deathTimeEditText.getText().toString();
                String deathPlace = spinnerDeathPlaces.getSelectedItem().toString();
                String name_placeOfDeath = name_placeOfDeathEditText.getText().toString();
                String street_placeOfDeath = street_placeOfDeathEditText.getText().toString();
                String city_placeOfDeath = city_placeOfDeathEditText.getText().toString();
                String state_placeOfDeath = state_placeOfDeathEditText.getText().toString();
                String zip_placeOfDeath = zip_placeOfDeathEditText.getText().toString();
                String signature_document = signature_documentEditText.getText().toString();
                String license_document = license_documentEditText.getText().toString();

                if (!bundle.containsKey("deathDate")) bundle.putString("deathDate", deathDate);
                if (!bundle.containsKey("deathTime")) bundle.putString("deathTime", deathTime);
                if (!bundle.containsKey("deathPlace")) bundle.putString("deathPlace", deathPlace);
                if (!bundle.containsKey("name_placeOfDeath")) bundle.putString("name_placeOfDeath", name_placeOfDeath);
                if (!bundle.containsKey("street_placeOfDeath")) bundle.putString("street_placeOfDeath", street_placeOfDeath);
                if (!bundle.containsKey("city_placeOfDeath")) bundle.putString("city_placeOfDeath", city_placeOfDeath);
                if (!bundle.containsKey("state_placeOfDeath")) bundle.putString("state_placeOfDeath", state_placeOfDeath);
                if (!bundle.containsKey("zip_placeOfDeath")) bundle.putString("zip_placeOfDeath", zip_placeOfDeath);
                if (!bundle.containsKey("signature_document")) bundle.putString("signature_document", signature_document);
                if (!bundle.containsKey("license_document")) bundle.putString("license_document", license_document);

                Intent showPronouncingDeath = new Intent(getApplicationContext(), PronouncingDeathActivity.class);
                showPronouncingDeath.putExtra("patientBundle", bundle);
                startActivity(showPronouncingDeath);
                finish();
            }
        });

        causalChainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on Causal Chain Button");

                EditText deathDateEditText = findViewById(R.id.patientpronouncingDeath_deathDate_editText);
                EditText deathTimeEditText = findViewById(R.id.patientpronouncingDeath_deathTime_editText);
                EditText name_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_name_placeOfDeath_editText);
                EditText street_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_street_placeOfDeath_editText);
                EditText city_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_city_placeOfDeath_editView);
                EditText state_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_state_placeOfDeath_editView);
                EditText zip_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_zip_placeOfDeath_editView);
                EditText signature_documentEditText = findViewById(R.id.patientpronouncingDeath_sign_document_editText);
                EditText license_documentEditText = findViewById(R.id.patientpronouncingDeath_license_editText);

                String deathDate = deathDateEditText.getText().toString();
                String deathTime = deathTimeEditText.getText().toString();
                String deathPlace = spinnerDeathPlaces.getSelectedItem().toString();
                String name_placeOfDeath = name_placeOfDeathEditText.getText().toString();
                String street_placeOfDeath = street_placeOfDeathEditText.getText().toString();
                String city_placeOfDeath = city_placeOfDeathEditText.getText().toString();
                String state_placeOfDeath = state_placeOfDeathEditText.getText().toString();
                String zip_placeOfDeath = zip_placeOfDeathEditText.getText().toString();
                String signature_document = signature_documentEditText.getText().toString();
                String license_document = license_documentEditText.getText().toString();

                if (!bundle.containsKey("deathDate")) bundle.putString("deathDate", deathDate);
                if (!bundle.containsKey("deathTime")) bundle.putString("deathTime", deathTime);
                if (!bundle.containsKey("deathPlace")) bundle.putString("deathPlace", deathPlace);
                if (!bundle.containsKey("name_palceOfDeath")) bundle.putString("name_placeOfDeath", name_placeOfDeath);
                if (!bundle.containsKey("street_placeOfDeath")) bundle.putString("street_placeOfDeath", street_placeOfDeath);
                if (!bundle.containsKey("city_placeOfDeath")) bundle.putString("city_placeOfDeath", city_placeOfDeath);
                if (!bundle.containsKey("state_placeOfDeath")) bundle.putString("state_placeOfDeath", state_placeOfDeath);
                if (!bundle.containsKey("zip_placeOfDeath")) bundle.putString("zip_placeOfDeath", zip_placeOfDeath);
                if (!bundle.containsKey("signature_document")) bundle.putString("signature_document", signature_document);
                if (!bundle.containsKey("license_document")) bundle.putString("license_document", license_document);

                Intent showCausalChain = new Intent(getApplicationContext(), CausalChainActivity.class);
                showCausalChain.putExtra("patientBundle", bundle);
                startActivity(showCausalChain);
                finish();
            }
        });

        reviewSummitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new pronouncingDeathRecordData().execute("Hello");
                //System.out.println("Name of Place of Death is " + bundle.getString("name_placeOfDeath"));

                EditText deathDateEditText = findViewById(R.id.patientpronouncingDeath_deathDate_editText);
                EditText deathTimeEditText = findViewById(R.id.patientpronouncingDeath_deathTime_editText);
                EditText name_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_name_placeOfDeath_editText);
                EditText street_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_street_placeOfDeath_editText);
                EditText city_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_city_placeOfDeath_editView);
                EditText state_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_state_placeOfDeath_editView);
                EditText zip_placeOfDeathEditText = findViewById(R.id.patientpronouncingDeath_zip_placeOfDeath_editView);
                EditText signature_documentEditText = findViewById(R.id.patientpronouncingDeath_sign_document_editText);
                EditText license_documentEditText = findViewById(R.id.patientpronouncingDeath_license_editText);

                String deathDate = deathDateEditText.getText().toString();
                String deathTime = deathTimeEditText.getText().toString();
                String deathPlace = spinnerDeathPlaces.getSelectedItem().toString();
                String name_placeOfDeath = name_placeOfDeathEditText.getText().toString();
                String street_placeOfDeath = street_placeOfDeathEditText.getText().toString();
                String city_placeOfDeath = city_placeOfDeathEditText.getText().toString();
                String state_placeOfDeath = state_placeOfDeathEditText.getText().toString();
                String zip_placeOfDeath = zip_placeOfDeathEditText.getText().toString();
                String signature_document = signature_documentEditText.getText().toString();
                String license_document = license_documentEditText.getText().toString();

                if (!bundle.containsKey("deathDate")) bundle.putString("deathDate", deathDate);
                if (!bundle.containsKey("deathTime")) bundle.putString("deathTime", deathTime);
                if (!bundle.containsKey("deathPlace")) bundle.putString("deathPlace", deathPlace);
                if (!bundle.containsKey("name_placeOfDeath")) bundle.putString("name_placeOfDeath", name_placeOfDeath);
                if (!bundle.containsKey("street_placeOfDeath")) bundle.putString("street_placeOfDeath", street_placeOfDeath);
                if (!bundle.containsKey("city_placeOfDeath")) bundle.putString("city_placeOfDeath", city_placeOfDeath);
                if (!bundle.containsKey("state_placeOfDeath")) bundle.putString("state_placeOfDeath", state_placeOfDeath);
                if (!bundle.containsKey("zip_placeOfDeath")) bundle.putString("zip_placeOfDeath", zip_placeOfDeath);
                if (!bundle.containsKey("signature_document")) bundle.putString("signature_document", signature_document);
                if (!bundle.containsKey("license_document")) bundle.putString("license_document", license_document);

                Intent showReviewSubmit = new Intent(getApplicationContext(), ReviewAndSubmitActivity.class);
                showReviewSubmit.putExtra("patientBundle", bundle);
                startActivity(showReviewSubmit);
                finish();
            }
        });

    }

    /*private class pronouncingDeathRecordData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String string) {

            Spinner spinnerDeathPlaces = (Spinner) findViewById(R.id.patientPronouncingDeath_deathPlace_spinner);
            ArrayAdapter<String> adapterDeathPlaces = new ArrayAdapter<String>(PronouncingDeathActivity.this,
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.deathPlaces));
            adapterDeathPlaces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDeathPlaces.setAdapter(adapterDeathPlaces);

            TextView patientNameTextView = (TextView) findViewById(R.id.PatientBasicInfo_patientNameTextView);

            String fullName = bundle.getString("fullName");
            patientNameTextView.setText(fullName);

            EditText deathDateEditText = findViewById(R.id.patientPronouncingDeath_deathDate_editText);
            EditText deathTimeEditText = findViewById(R.id.patientPronouncingDeath_deathTime_editText);
            EditText name_placeOfDeathEditText = findViewById(R.id.patientPronouncingDeath_name_placeOfDeath_editText);
            EditText street_placeOfDeathEditText = findViewById(R.id.patientPronouncingDeath_street_placeOfDeath_editText);
            EditText city_placeOfDeathEditText = findViewById(R.id.patientPronouncingDeath_city_placeOfDeath_editView);
            EditText state_placeOfDeathEditText = findViewById(R.id.patientPronouncingDeath_state_placeOfDeath_editView);
            EditText zip_placeOfDeathEditText = findViewById(R.id.patientPronouncingDeath_zip_placeOfDeath_editView);
            EditText signature_documentEditText = findViewById(R.id.patientPronouncingDeath_sign_document_editText);
            EditText license_documentEditText = findViewById(R.id.patientPronouncingDeath_license_editText);

            String deathDate = deathDateEditText.getText().toString();
            String deathTime = deathTimeEditText.getText().toString();
            String deathPlace = spinnerDeathPlaces.getSelectedItem().toString();
            String name_placeOfDeath = name_placeOfDeathEditText.getText().toString();
            String street_placeOfDeath = street_placeOfDeathEditText.getText().toString();
            String city_placeOfDeath = city_placeOfDeathEditText.getText().toString();
            String state_placeOfDeath = state_placeOfDeathEditText.getText().toString();
            String zip_placeOfDeath = zip_placeOfDeathEditText.getText().toString();
            String signature_document = signature_documentEditText.getText().toString();
            String license_document = license_documentEditText.getText().toString();

            if (!bundle.containsKey("deathDate")) bundle.putString("deathDate", deathDate);
            if (!bundle.containsKey("deathTime")) bundle.putString("deathTime", deathTime);
            if (!bundle.containsKey("deathPlace")) bundle.putString("deathPlace", deathPlace);
            if (!bundle.containsKey("name_placeOfDeath")) bundle.putString("name_placeOfDeath", name_placeOfDeath);
            if (!bundle.containsKey("street_placeOfDeath")) bundle.putString("street_placeOfDeath", street_placeOfDeath);
            if (!bundle.containsKey("city_placeOfDeath")) bundle.putString("city_placeOfDeath", city_placeOfDeath);
            if (!bundle.containsKey("state_placeOfDeath")) bundle.putString("state_placeOfDeath", state_placeOfDeath);
            if (!bundle.containsKey("zip_placeOfDeath")) bundle.putString("zip_placeOfDeath", zip_placeOfDeath);
            if (!bundle.containsKey("signature_document")) bundle.putString("signature_document", signature_document);
            if (!bundle.containsKey("license_document")) bundle.putString("license_document", license_document);
        }
    }*/

}
