package edu.gatech.miblab.fhirdeathreport;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
//import org.hl7.fhir.dstu3.model.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class PatientBasicInfoActivity extends AppCompatActivity {

    FhirContext ctx = FhirContext.forDstu3();
    String serverBase = "http://hapi.fhir.org/baseDstu3";
    Patient patient = new Patient();
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_basic_info);

        Intent intent = getIntent();
        String url = intent.getStringExtra("This URL: ");
        System.out.println("Intent onCreate showing: " + url);

        new showData().execute(url);

    }

    private class showData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("Intent doInBackground showing: " + strings[0]);
            //return strings[0];

            String url = strings[0];

            IGenericClient client = ctx.newRestfulGenericClient(serverBase);
            Patient currentPatient = client.read().resource(Patient.class).withUrl(url).execute();

            String[] ids = currentPatient.getId().split("/");
            int k = 0;
            for (String item: ids) {
                if (k==5) {
                    String id = item;
                    bundle.putString("ID", item);
                }
                k++;
                System.out.println("ID is: " + item);
            }


            String familyName = currentPatient.getName().get(0).getFamily();
            String givenName = currentPatient.getName().get(0).getGivenAsSingleString();

            bundle.putString("familyName", familyName);
            bundle.putString("givenName", givenName);

            patient.addName(currentPatient.getName().get(0));

            // Upwrap extensions

            String deathDateString = new String();

            for (ListIterator<Extension> it = currentPatient.getExtension().listIterator(); it.hasNext(); ) {
                Extension thisExtension = it.next();
//                System.out.println(thisExtension.getId());
                if (thisExtension.getId().contains("deathDate")) {
                    deathDateString = thisExtension.getValue().toString();
                    System.out.println("Death date string is: " + deathDateString);
                }
//                if (thisExtension.getId().contains("address")) {
//                    String deathDateString = thisExtension.getValue().toString();
//                    System.out.println("Address is: " + deathDateString);
//                }
            }

            String DOB = "N/A";
            if (currentPatient.hasBirthDate()) {
                DOB = currentPatient.getBirthDate().toString();
                patient.setBirthDate(currentPatient.getBirthDate());
            } else {
                patient.setBirthDate(null);
            }
            bundle.putString("DOB", DOB);

            String gender = "Unknown";
            if (currentPatient.hasGender()) {
                gender = currentPatient.getGender().toString();
                patient.setGender(currentPatient.getGender());
            }
            bundle.putString("gender", gender);




//            System.out.println("Address is: " + address.getLine());
            if (currentPatient.hasAddress()) {

                Address address = currentPatient.getAddress().get(0);
                System.out.println("Address is " + address.getText());
                bundle.putString("address", address.getText());
                patient.addAddress(address);
            } else {
//                patient.addAddress().addLine(address);
            }
//            bundle.putString("address", address);

            if (currentPatient.hasDeceased()) {
                System.out.println("Death Status is: " + currentPatient.getDeceasedBooleanType().booleanValue());
                if (currentPatient.getDeceasedBooleanType().booleanValue()) {
                    String deathStatus = "Yes";
//                    String deathDate = currentPatient.getDeceased();
//
                    java.util.Date deathDate = currentPatient.getDeceased() instanceof DateType ? ((DateType) patient.getDeceased()).getValue() : null;
                    if (deathDate == null) {
                        System.out.println("deathDate is null");
//                        currentPatient.getExtension().get(0).getId().contains("deathDate");
                        if (currentPatient.getExtension().get(0).getId().contains("deathDate")) {
//                            currentPatient.getExtension().get(0).getNamedProperty("deathDate");
                            System.out.println("Find deathDate in Extension");
//                            for (ListIterator<Extension> it = currentPatient.getExtension().listIterator(); it.hasNext(); ) {
//                                Extension thisExtension = it.next();
//                                System.out.println(thisExtension.getId());
//                                if (thisExtension.getId().contains("deathDate")) {
//                                    String deathDateString = thisExtension.getValue().toString();
//                                    System.out.println("Death date string is: " + deathDateString);
//                                }
//                            }
                        }
                    }

                    if (deathDate != null) {
                        bundle.putString("deathDate", deathDate.toString());
                    }
                    if (!deathDateString.isEmpty()) {
                        bundle.putString("deathDateString", deathDateString);
                    }
                    System.out.println("death date is: " + deathDate);
                    patient.setDeceased(new BooleanType(true));
                    bundle.putBoolean("hasDeceased", true);
                    bundle.putBoolean("isDead", true);

                    //patient.addExtension();
                    //Extension ext = new Extension();
                    //ext.setValue()
                    //deceasedBooleanTextView.setText("Deceased? Yes");
                    //deceasedDateTextView.setText("Deceased Date: " + deathDate);
                } else {
                    System.out.println("Death is false");
                    patient.setDeceased(new BooleanType(false));
                    bundle.putBoolean("hasDeceased", true);
                    bundle.putBoolean("isDead", false);
                    //deceasedBooleanTextView.setText("Deceased? No");
                    //deceasedDateTextView.setText("Deceased Date: N/A");
                }
            } else {
                System.out.println("Unknown Death");
                bundle.putBoolean("hasDeceased", false);
                //patient.setDeceased(new BooleanType(false));
                //deceasedBooleanTextView.setText("Deceased? Unknown");
                //deceasedDateTextView.setText("Deceased Date: N/A");
            }

            bundle.putString("url", url);

            return url;
        }

        @Override
        protected void onPostExecute(String url) {

            TextView patientNameTextView = (TextView) findViewById(R.id.PatientBasicInfo_patientNameTextView);

            TextView dobTextView = (TextView) findViewById(R.id.patientBasicInfo_birthDateTextView);
            TextView genderTextView = (TextView) findViewById(R.id.patientBasicInfo_genderTextView);
            TextView addressTextView = (TextView) findViewById(R.id.patientBasicInfo_addressTextView);
            TextView deceasedBooleanTextView = (TextView) findViewById(R.id.patientBasicInfo_deceasedBooleanTextView);
            TextView deceasedDateTextView = (TextView) findViewById(R.id.patientBasicInfo_deceasedDateTextView);

            //System.out.println("Intent onPostExecute showing: " + url);
            String fullName = patient.getName().get(0).getNameAsSingleString();
            bundle.putString("fullName", fullName);
            System.out.println(fullName);

            patientNameTextView.setText(fullName);
            String DOB = "N/A";
            if (patient.hasBirthDate()) {
                DOB = patient.getBirthDate().toString();
            }
            dobTextView.setText("Birth Date: " + DOB);

            String gender = "Unknown";
            if (patient.hasGender()) {
                gender = patient.getGender().toString();
            }

            String address = "Unknown";
            String city = "";
            String state = "";
            String zip = "";
            if (patient.hasAddress()) {

                address = bundle.getString("address");

//                address = patient.getAddress().get(0).getLine().toString();
//                city = patient.getAddress().get(0).getCity();
//                state = patient.getAddress().get(0).getState();
//                zip = patient.getAddress().get(0).getPostalCode();
//                bundle.putString("addressCity", city);
//                bundle.putString("addressState", state);
//                bundle.putString("addressZip", zip);
                //address = patient.getAddress().get(0).getText();
            }

            genderTextView.setText("Gender: " + gender);
            addressTextView.setText("Address : " + address);
//            addressTextView.setText("Address : " + address + ", " + city + ", " + state + ", " + zip);

            if (patient.hasDeceased()) {
                System.out.println("Death is " + patient.getDeceased().toString());
                if (patient.getDeceasedBooleanType().booleanValue()) {
                    deceasedBooleanTextView.setText("Deceased? True");
                    deceasedDateTextView.setText("Deceased Date: " + bundle.getString("deathDateString"));
//                    System.out.println("Death Date is: " + patient.getExtension().get(0).getChildByName("deathDate"));
                } else {
                    deceasedBooleanTextView.setText("Deceased? False");
                    deceasedDateTextView.setText("Deceased Date: N/A");
                }
            } else {
                deceasedBooleanTextView.setText("Deceased? Unknown");
                deceasedDateTextView.setText("Deceased Date: N/A");
            }

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

            /*pronouncingDeathButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Clicked on pronouncing Death Button!");
                    String thisString = "hello!";
                    pronouncingDeathTask switchToPronouncingDeath = new pronouncingDeathTask();
                    switchToPronouncingDeath.execute(thisString);
                }
            });*/

        }
    }

    private class pronouncingDeathTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            System.out.println("Pronouncing Death Async Task: do in background" + strings[0]);

            return strings[0];
        }

        @Override
        protected void onPostExecute(String strings) {

            Intent showPronouncingDeath = new Intent(getApplicationContext(), PronouncingDeathActivity.class);

            showPronouncingDeath.putExtra("patientBundle", bundle);

            startActivity(showPronouncingDeath);
        }
    }
}
