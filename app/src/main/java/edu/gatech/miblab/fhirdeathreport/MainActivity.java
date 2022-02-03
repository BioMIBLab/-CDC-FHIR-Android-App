package edu.gatech.miblab.fhirdeathreport;

import java.util.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;

public class MainActivity extends AppCompatActivity {

    private Button searchPatientButton;
    private EditText inputPatientNameText;
    private ListView patientNameListView;

    String[] patientNames;
    String[] birthDates;
    String[] URLs;

    //Patient myPatient = new Patient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPatientNameText = findViewById(R.id.inputPatientName);
        patientNameListView = findViewById(R.id.list_of_patient_names);

        searchPatientButton = findViewById(R.id.searchPatientButton);
        searchPatientButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                System.out.println("Searching");
                String inputPatientName = inputPatientNameText.getText().toString();
                System.out.println(inputPatientName);

                GetData retrievePatient = new GetData();
                retrievePatient.execute(inputPatientName);

            }
        });
    }

    private class GetData extends AsyncTask<String, String, Integer> {

        //private ArrayList<String> patientNamesArr = new ArrayList<>();
        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://hapi.fhir.org/baseDstu3";

        @Override
        protected Integer doInBackground(String... strings) {

            // Start client
            //System.out.println(strings[0]);

            System.out.println("FhirContext finished");

            IGenericClient client = ctx.newRestfulGenericClient(serverBase);

            System.out.println("client finished");

            // search for patients
            org.hl7.fhir.dstu3.model.Bundle results = client.search().forResource(Patient.class)
                    .where(Patient.FAMILY.matches().value(strings[0]))
                    //.and(Patient.DEATH_DATE.beforeOrEquals().now())
                    .sort().ascending(Patient.NAME)
                    .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                    .execute();

            System.out.println("search finished");
            System.out.println("Found " + results.getEntry().size() + " patients");

            ArrayList<String> patientNamesArr = new ArrayList<>();
            ArrayList<String> birthDatesArr = new ArrayList<>();
            ArrayList<String> URLsArr = new ArrayList<>();

            for (int i = 0; i < results.getEntry().size(); i++) {
                String url = results.getEntry().get(i).getFullUrl();
                URLsArr.add(url);
                System.out.println(url);
                Patient thisPatient = client.read().resource(Patient.class).withUrl(url).execute();
                String fullName = thisPatient.getName().get(0).getGiven().toString() + " " + thisPatient.getName().get(0).getFamily().toString();
                //String fullName = thisPatient.getName().get(0).getNameAsSingleString();
                String DOB = "N/A";
                if (! (thisPatient.getBirthDate() == null)) {
                    DOB = thisPatient.getBirthDate().toString();
                }
                patientNamesArr.add(fullName);
                birthDatesArr.add(DOB);
                System.out.println(fullName);
                System.out.println("Value is " + i);
            }

            patientNames = new String[patientNamesArr.size()];
            for (int i=0; i<patientNamesArr.size(); i++) {
                patientNames[i] = patientNamesArr.get(i);
            }

            birthDates = new String[birthDatesArr.size()];
            for (int i=0; i<birthDatesArr.size(); i++) {
                birthDates[i] = birthDatesArr.get(i);
            }

            URLs = new String[URLsArr.size()];
            for (int i=0; i<URLsArr.size(); i++) {
                URLs[i] = URLsArr.get(i);
            }

            return results.getEntry().size();

        }

        @Override
        protected void onPostExecute(Integer size) {

            if (size>0) {
                ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this, patientNames, birthDates);
                patientNameListView.setAdapter(itemAdapter);

                patientNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, patientNames[position], Toast.LENGTH_SHORT).show();
                        Intent showPatientBasicInfo = new Intent(getApplicationContext(), PatientBasicInfoActivity.class);
                        showPatientBasicInfo.putExtra("This URL: ", URLs[position]);
                        startActivity(showPatientBasicInfo);
                    }
                });


            }

        }
    }
}
