package edu.gatech.miblab.fhirdeathreport;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.hl7.fhir.dstu3.model.Condition;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;

public class CausalChainActivity extends AppCompatActivity {

    Bundle bundle;
    String[] ICD_codes;
    String[] underlying_ICD_codes;
    String[] immediate_1_ICD_codes;
    String[] immediate_2_ICD_codes;
    String[] immediate_3_ICD_codes;
    String TAG = "Causal Chain Activity: ";

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            System.out.println("Write Permission is Granted.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_causal_chain);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("patientBundle");

        TextView patientNameTextView = (TextView) findViewById(R.id.PatientBasicInfo_patientNameTextView);

        String fullName = bundle.getString("fullName");
        patientNameTextView.setText(fullName);

        String id = bundle.getString("ID");
        System.out.println("id is: "+ id);

        new getConditions().execute(id);
        //new getConditions().execute("gtp101");
        System.out.println(ICD_codes);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

//        if (isStoragePermissionGranted()) {
//            System.out.println("Permission is Granted.");
//
//            Python py = Python.getInstance();
//            final PyObject pyobj = py.getModule("main");
//
//            PyObject obj = pyobj.callAttr("main", "data");
//        }

    }

    private class getConditions extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {

            // Create a client (only needed once)
            FhirContext ctx = FhirContext.forDstu3();
            IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseDstu3");

            // Invoke the client
            org.hl7.fhir.dstu3.model.Bundle conditionBundle = client.search().forResource(Condition.class)
                    .where(new ReferenceClientParam("patient").hasId(strings[0]))
                    .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                    .execute();

            ArrayList<String> URLsArr = new ArrayList<>();
            ArrayList<String> codesArr = new ArrayList<>();

            for (int i=0; i<conditionBundle.getEntry().size(); i++) {
                String url = conditionBundle.getEntry().get(i).getFullUrl();
                Condition condition = client.read().resource(Condition.class).withUrl(url).execute();
                String code = condition.getCode().getCoding().get(0).getDisplay();
                System.out.println("Condition code: " + code);
                codesArr.add(code);
            }

            ICD_codes = new String[codesArr.size()+1];
            underlying_ICD_codes = new String[codesArr.size()+1];
            immediate_1_ICD_codes = new String[codesArr.size()+1];
            immediate_2_ICD_codes = new String[codesArr.size()+1];
            immediate_3_ICD_codes = new String[codesArr.size()+1];
            underlying_ICD_codes[0] = "N/A";
            immediate_1_ICD_codes[0] = "N/A";
            immediate_2_ICD_codes[0] = "N/A";
            immediate_3_ICD_codes[0] = "N/A";
            for (int i=0; i<codesArr.size(); i++) {
                ICD_codes[i] = codesArr.get(i);
                underlying_ICD_codes[i+1] = codesArr.get(i);
                immediate_1_ICD_codes[i+1] = codesArr.get(i);
                immediate_2_ICD_codes[i+1] = codesArr.get(i);
                immediate_3_ICD_codes[i+1] = codesArr.get(i);
            }

//            System.out.println(ICD_codes);
            System.out.println("Fist data element of new underlying ICD codes " + underlying_ICD_codes[0]);
//            System.out.println(Arrays.copyOfRange(ICD_codes, 1, ICD_codes.length));



            // Write to text file

//            String file_name_ICD_codes = "original_ICD_codes.txt";
//
//            try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file_name_ICD_codes, Context.MODE_PRIVATE));
//                outputStreamWriter.write(String.valueOf(ICD_codes[0]));
//                outputStreamWriter.close();
//            } catch (IOException e) {
//                Log.e("Exception", "File write failed: " + e.toString());
//            }

            return 0;
        }
        @Override
        protected void onPostExecute(Integer size) {

            String ICD_strings = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ICD_strings = String.join("; ", Arrays.copyOfRange(ICD_codes, 1, ICD_codes.length));
            }

            Python py = Python.getInstance();
            final PyObject pyobj = py.getModule("main");

//            String ICD_10_human_readables = null;
            String[] ICD_10_codes;
            if (isStoragePermissionGranted()) {
                System.out.println("Permission is Granted.");
                PyObject obj = pyobj.callAttr("main", ICD_strings);
                System.out.println(obj.toString());
                ICD_10_codes = obj.toString().split(";");
                System.out.println(ICD_10_codes.length);
                for (int i=0; i<ICD_10_codes.length; i++) {
                    System.out.println(ICD_10_codes[i]);
                }
                if (ICD_10_codes.length>=4) {
                    underlying_ICD_codes[0] = ICD_10_codes[0]+" ";
                    immediate_1_ICD_codes[0] = ICD_10_codes[1]+" ";
                    immediate_2_ICD_codes[0] = ICD_10_codes[2]+" ";
                    immediate_3_ICD_codes[0] = ICD_10_codes[3]+" ";
                } else if (ICD_10_codes.length==3) {
                    underlying_ICD_codes[0] = ICD_10_codes[0]+" ";
                    immediate_1_ICD_codes[0] = ICD_10_codes[1]+" ";
                    immediate_2_ICD_codes[0] = ICD_10_codes[2]+" ";
                    immediate_3_ICD_codes[0] = "N/A";
                } else if (ICD_10_codes.length==2) {
                    underlying_ICD_codes[0] = ICD_10_codes[0]+" ";
                    immediate_1_ICD_codes[0] = ICD_10_codes[1]+" ";
                    immediate_2_ICD_codes[0] = "N/A";
                    immediate_3_ICD_codes[0] = "N/A";
                } else if (ICD_10_codes.length==1) {
                    underlying_ICD_codes[0] = ICD_10_codes[0]+" ";
//                    immediate_1_ICD_codes[0] = "N/A";
//                    immediate_2_ICD_codes[0] = "N/A";
//                    immediate_3_ICD_codes[0] = "N/A";
                } else {
                    underlying_ICD_codes[0] = "N/A";
                    immediate_1_ICD_codes[0] = "N/A";
                    immediate_2_ICD_codes[0] = "N/A";
                    immediate_3_ICD_codes[0] = "N/A";
                }
            }

            System.out.println("After running python, " + underlying_ICD_codes[0]);

            //String ICD_codes[] = {"G40", "R56", "401.9", "518.81", "780.39", "786.05", "780.79", "780.97", "786.50", "486", "285.9"};
            //String ICD_codes[] = {"Epilepsy, unspecified", "Unspecified convulsions", "Hypertension", "Acute respiratory failure", "Unspecified convulsions", "Shortness of breath", "Postviral fatigue syndrome", "Altered mental status, unspecified", "Chest pain", "Pneumonia", "Convert to ICD-10-CM: 285.9 converts directly to: 2015/16 ICD-10-CM D64.9 Anemia"};

            Spinner spinner_underlying_causeOfDeath = (Spinner) findViewById(R.id.patientCausalChain_underlying_causeOfDeath_spinner);
            ArrayAdapter<String> adapter_underlying_causeOfDeath = new ArrayAdapter<String>(CausalChainActivity.this,
                    android.R.layout.simple_list_item_1, underlying_ICD_codes);
            adapter_underlying_causeOfDeath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_underlying_causeOfDeath.setAdapter(adapter_underlying_causeOfDeath);

            Spinner spinner_immediate_1_causeOfDeath = (Spinner) findViewById(R.id.patientCausalChain_immediate_1_causeOfDeath_spinner);
            ArrayAdapter<String> adapter_immediate_1_causeOfDeath = new ArrayAdapter<String>(CausalChainActivity.this,
                    android.R.layout.simple_list_item_1, immediate_1_ICD_codes);
            adapter_immediate_1_causeOfDeath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_immediate_1_causeOfDeath.setAdapter(adapter_immediate_1_causeOfDeath);

            Spinner spinner_immediate_2_causeOfDeath = (Spinner) findViewById(R.id.patientCausalChain_immediate_2_causeOfDeath_spinner);
            ArrayAdapter<String> adapter_immediate_2_causeOfDeath = new ArrayAdapter<String>(CausalChainActivity.this,
                    android.R.layout.simple_list_item_1, immediate_2_ICD_codes);
            adapter_immediate_2_causeOfDeath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_immediate_2_causeOfDeath.setAdapter(adapter_immediate_2_causeOfDeath);

            Spinner spinner_immediate_3_causeOfDeath = (Spinner) findViewById(R.id.patientCausalChain_immediate_3_causeOfDeath_spinner);
            ArrayAdapter<String> adapter_immediate_3_causeOfDeath = new ArrayAdapter<String>(CausalChainActivity.this,
                    android.R.layout.simple_list_item_1, immediate_3_ICD_codes);
            adapter_immediate_3_causeOfDeath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_immediate_3_causeOfDeath.setAdapter(adapter_immediate_3_causeOfDeath);

//            int randomInt;
//            randomInt = (int) (Math.random()*ICD_codes.length);
//            spinner_underlying_causeOfDeath.setSelection(randomInt);
//            randomInt = (int) (Math.random()*ICD_codes.length);
//            spinner_immediate_1_causeOfDeath.setSelection(randomInt);
//            randomInt = (int) (Math.random()*ICD_codes.length);
//            spinner_immediate_2_causeOfDeath.setSelection(randomInt);
//            randomInt = (int) (Math.random()*ICD_codes.length);
//            spinner_immediate_3_causeOfDeath.setSelection(randomInt);

            spinner_underlying_causeOfDeath.setSelection(0);
            spinner_immediate_1_causeOfDeath.setSelection(0);
            spinner_immediate_2_causeOfDeath.setSelection(0);
            spinner_immediate_3_causeOfDeath.setSelection(0);

            String underlying_causeOfDeath = spinner_underlying_causeOfDeath.getSelectedItem().toString();
            String immediate_1_causeOfDeath = spinner_immediate_1_causeOfDeath.getSelectedItem().toString();
            String immediate_2_causeOfDeath = spinner_immediate_2_causeOfDeath.getSelectedItem().toString();
            String immediate_3_causeOfDeath = spinner_immediate_3_causeOfDeath.getSelectedItem().toString();
            if (!bundle.containsKey("underlying_causeOfDeath")) {
                bundle.putString("underlying_causeOfDeath", underlying_causeOfDeath);
                System.out.println("Underlying saved");
            } else {
                bundle.putString("underlying_causeOfDeath", underlying_causeOfDeath);
                System.out.println("Underlying updated");
            }
            if (!bundle.containsKey("immediate_1_causeOfDeath")) {
                bundle.putString("immediate_1_causeOfDeath", immediate_1_causeOfDeath);
                System.out.println("Immediate 1 saved");
            } else {
                bundle.putString("immediate_1_causeOfDeath", immediate_1_causeOfDeath);
                System.out.println("Immediate 1 updated");
            }
            if (!bundle.containsKey("immediate_2_causeOfDeath")) {
                bundle.putString("immediate_2_causeOfDeath", immediate_2_causeOfDeath);
                System.out.println("Immediate 2 saved");
            } else {
                bundle.putString("immediate_2_causeOfDeath", immediate_2_causeOfDeath);
                System.out.println("Immediate 2 updated");
            }
            if (!bundle.containsKey("immediate_3_causeOfDeath")) {
                bundle.putString("immediate_3_causeOfDeath", immediate_3_causeOfDeath);
                System.out.println("Immediate 3 saved");
            } else {
                bundle.putString("immediate_3_causeOfDeath", immediate_3_causeOfDeath);
                System.out.println("Immediate 3 updated");
            }

        /*Button saveChangesButton = (Button) findViewById(R.id.patientCausalChain_saveChangesButton);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String underlying_causeOfDeath = spinner_underlying_causeOfDeath.getSelectedItem().toString();
                String immediate_1_causeOfDeath = spinner_immediate_1_causeOfDeath.getSelectedItem().toString();
                String immediate_2_causeOfDeath = spinner_immediate_2_causeOfDeath.getSelectedItem().toString();
                String immediate_3_causeOfDeath = spinner_immediate_3_causeOfDeath.getSelectedItem().toString();
                if (!bundle.containsKey("underlying_causeOfDeath")) bundle.putString("underlying_causeOfDeath", underlying_causeOfDeath);
                if (!bundle.containsKey("immediate_1_causeOfDeath")) bundle.putString("immediate_1_causeOfDeath", immediate_1_causeOfDeath);
                if (!bundle.containsKey("immediate_2_causeOfDeath")) bundle.putString("immediate_2_causeOfDeath", immediate_2_causeOfDeath);
                if (!bundle.containsKey("immediate_3_causeOfDeath")) bundle.putString("immediate_3_causeOfDeath", immediate_3_causeOfDeath);
            }
        });*/

            Button basicInfoButton = (Button) findViewById(R.id.PatientBasicInfo_BasicInfoButton);
            Button pronouncingDeathButton = (Button) findViewById(R.id.patientBasicInfo_PronouncingDeathButton);
            Button causalChainButton = (Button) findViewById(R.id.patientBasicInfo_CausalChainButton);
            Button reviewSummitButton = (Button) findViewById(R.id.patientBasicInfo_ReviewSummit);

        /*basicInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on Basic Patient Info Button!");

                Intent showBasicInfo = new Intent(getApplicationContext(), PatientBasicInfoActivity.class);
                showBasicInfo.putExtra("patientBundle", bundle);
                startActivity(showBasicInfo);
                finish();
            }
        });*/

            pronouncingDeathButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                    Intent showCausalChain = new Intent(getApplicationContext(), CausalChainActivity.class);
                    showCausalChain.putExtra("patientBundle", bundle);
                    startActivity(showCausalChain);
                    finish();
                }
            });

            reviewSummitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent showReviewSubmit = new Intent(getApplicationContext(), ReviewAndSubmitActivity.class);
                    showReviewSubmit.putExtra("patientBundle", bundle);
                    startActivity(showReviewSubmit);
                    finish();
                }
            });

        }

    }
}
