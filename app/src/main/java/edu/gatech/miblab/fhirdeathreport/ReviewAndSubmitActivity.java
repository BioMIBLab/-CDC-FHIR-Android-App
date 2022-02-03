package edu.gatech.miblab.fhirdeathreport;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.StringType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewAndSubmitActivity extends AppCompatActivity {

    Bundle bundle;
    Patient patient;
    org.hl7.fhir.dstu3.model.Bundle patientBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("patientBundle");
        //Bundle patientBundle = intent.getBundleExtra("patientBundle");

        System.out.println("Pronouncing Death Switched");
        System.out.println(bundle.getString("familyName"));

        TextView patientNameTextView = (TextView) findViewById(R.id.PatientBasicInfo_patientNameTextView);

        String fullName = bundle.getString("fullName");
        patientNameTextView.setText(fullName);

        Button basicInfoButton = (Button) findViewById(R.id.PatientBasicInfo_BasicInfoButton);
        Button pronouncingDeathButton = (Button) findViewById(R.id.patientBasicInfo_PronouncingDeathButton);
        Button causalChainButton = (Button) findViewById(R.id.patientBasicInfo_CausalChainButton);
        Button reviewSummitButton = (Button) findViewById(R.id.patientBasicInfo_ReviewSummit);

        new reviewSubmitShowData().execute("Hello World");

        Button confirmSubmitButton = findViewById(R.id.patientReviewSubmit_confirmSubmitButton);

        confirmSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //client.transaction().withBundle(patientBundle).execute();

                patient = new Patient();

                for (String key : bundle.keySet()) {
                    System.out.println(key + ": " + bundle.get(key));
                }

                patient.addName().setFamily(bundle.getString("familyName")).addGiven(bundle.getString("givenName"));
                //patient.addName().addGiven(bundle.getString("givenName"));
                patient.addAddress().setText(bundle.getString("address"));
                patient.addAddress().setCity(bundle.getString("addressCity"));
                patient.addAddress().setState(bundle.getString("addressState"));
                patient.addAddress().setState(bundle.getString("addressState"));
                patient.setGender(AdministrativeGender.valueOf(bundle.getString("gender")));
                patient.setDeceased(new BooleanType(true));

                try {
                    String target = bundle.getString("DOB");
                    DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
                    Date result =  df.parse(target);
                    System.out.println("The parsed date is: " + result);
                    patient.setBirthDate(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //patient.addExtension().addChild("deathDate");
                Extension extDeathDate = new Extension();
                extDeathDate.setId("deathDate");
                extDeathDate.setValue(new StringType(bundle.getString("deathDate")));
                patient.addExtension(extDeathDate);

                Extension extDeathTime = new Extension();
                extDeathTime.setId("deathTime");
                extDeathTime.setValue(new StringType(bundle.getString("deathTime")));
                patient.addExtension(extDeathTime);

                Extension extDeathPlaceType = new Extension();
                extDeathPlaceType.setId("deathPlaceType");
                extDeathPlaceType.setValue(new StringType(bundle.getString("deathPlace")));
                patient.addExtension(extDeathPlaceType);

                Extension extName_PlaceOfDeath = new Extension();
                extName_PlaceOfDeath.setId("name_PlaceOfDeath");
                extName_PlaceOfDeath.setValue(new StringType(bundle.getString("name_PlaceOfDeath")));
                patient.addExtension(extName_PlaceOfDeath);

                Extension extStreet_PlaceOfDeath = new Extension();
                extStreet_PlaceOfDeath.setId("street_placeOfDeath");
                extStreet_PlaceOfDeath.setValue(new StringType(bundle.getString("street_placeOfDeath")));
                patient.addExtension(extStreet_PlaceOfDeath);

                Extension extCity_placeOfDeath = new Extension();
                extCity_placeOfDeath.setId("city_placeOfDeath");
                extCity_placeOfDeath.setValue(new StringType(bundle.getString("city_placeOfDeath")));
                patient.addExtension(extCity_placeOfDeath);

                Extension extState_placeOfDeath = new Extension();
                extState_placeOfDeath.setId("state_placeOfDeath");
                extState_placeOfDeath.setValue(new StringType(bundle.getString("state_placeOfDeath")));
                patient.addExtension(extState_placeOfDeath);

                Extension extZip_placeOfDeath = new Extension();
                extZip_placeOfDeath.setId("zip_placeOfDeath");
                extZip_placeOfDeath.setValue(new StringType(bundle.getString("zip_placeOfDeath")));
                patient.addExtension(extZip_placeOfDeath);

                Extension extPerson_pronouncedDeath = new Extension();
                extPerson_pronouncedDeath.setId("person_pronouncedDeath");
                extPerson_pronouncedDeath.setValue(new StringType(bundle.getString("signature_document")));
                patient.addExtension(extPerson_pronouncedDeath);

                Extension extLicense_pronouncedDeath = new Extension();
                extLicense_pronouncedDeath.setId("license_pronouncedDeath");
                extLicense_pronouncedDeath.setValue(new StringType(bundle.getString("license_document")));
                patient.addExtension(extLicense_pronouncedDeath);

                Extension extImmediate_1_causeOfDeath = new Extension();
                extImmediate_1_causeOfDeath.setId("extImmediate_1_causeOfDeath");
                extImmediate_1_causeOfDeath.setValue(new StringType(bundle.getString("immediate_1_causeOfDeath")));
                patient.addExtension(extImmediate_1_causeOfDeath);

                Extension extImmediate_2_causeOfDeath = new Extension();
                extImmediate_2_causeOfDeath.setId("extImmediate_2_causeOfDeath");
                extImmediate_2_causeOfDeath.setValue(new StringType(bundle.getString("immediate_2_causeOfDeath")));
                patient.addExtension(extImmediate_2_causeOfDeath);

                Extension extImmediate_3_causeOfDeath = new Extension();
                extImmediate_3_causeOfDeath.setId("extImmediate_3_causeOfDeath");
                extImmediate_3_causeOfDeath.setValue(new StringType(bundle.getString("immediate_3_causeOfDeath")));
                patient.addExtension(extImmediate_3_causeOfDeath);

                Extension extUnderlying_causeOfDeath = new Extension();
                extUnderlying_causeOfDeath.setId("extUnderlying_causeOfDeath");
                extUnderlying_causeOfDeath.setValue(new StringType(bundle.getString("underlying_causeOfDeath")));
                patient.addExtension(extUnderlying_causeOfDeath);

                Extension extName_certifier = new Extension();
                extName_certifier.setId("name_certifier");
                extName_certifier.setValue(new StringType(bundle.getString("name_certifier")));
                patient.addExtension(extName_certifier);

                Extension extLicense_certifier = new Extension();
                extLicense_certifier.setId("license_certifier");
                extLicense_certifier.setValue(new StringType(bundle.getString("license_certifier")));
                patient.addExtension(extLicense_certifier);

                Extension extStreet_certifier = new Extension();
                extStreet_certifier.setId("street_address_certifier");
                extStreet_certifier.setValue(new StringType(bundle.getString("street_certifier")));
                patient.addExtension(extStreet_certifier);

                Extension extCity_state_zip_certifier = new Extension();
                extCity_state_zip_certifier.setId("city_state_zip_certifier");
                extCity_state_zip_certifier.setValue(new StringType(bundle.getString("city_state_zip_certifier")));
                patient.addExtension(extCity_state_zip_certifier);

                //if (!patient.hasId()) patient.setId("MIBLab_Patient/" + bundle.getString("fullName"));
                //int id = (int) (Math.random()*1000000+1);
                String id = bundle.getString("ID");
                if (!patient.hasId()) patient.setId(id);
                if (!patient.hasIdentifier()) patient.addIdentifier().setSystem("urn:system").setValue("" + id);

                new updateServer().execute("Hello World");
            }
        });

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

    private class updateServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            System.out.println("Submitting patient bundle");
            System.out.println("Certifier is: " + bundle.getString("name_certifier"));

            FhirContext ctx = FhirContext.forDstu3();
            String serverBase = "http://hapi.fhir.org/baseDstu3";
            IGenericClient client = ctx.newRestfulGenericClient(serverBase);
            client.update().resource(patient).execute();
            System.out.println("Finish Updating patient on Server!");
            return strings[0];
        }

        @Override
        protected void onPostExecute(String strings) {

            System.out.println("All Done!");
            RelativeLayout SnackbarMainLayout = findViewById(R.id.SnackbarMainLayout);
            Snackbar snackbar = Snackbar.make(SnackbarMainLayout, "Patient Profile Successfully Updated on Server!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

    private class reviewSubmitShowData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            System.out.println("Review and Submit Show Data: do in background" + strings[0]);


            return strings[0];
        }

        @Override
        protected void onPostExecute(String strings) {

            EditText deathDateEditText = findViewById(R.id.patientReviewSubmit_deathDate_editText);
            EditText deathTimeEditText = findViewById(R.id.patientReviewSubmit_deathTime_editText);
            EditText deathPlaceEditText = findViewById(R.id.patientReviewSubmit_placeOfDeath_editText);
            EditText name_placeOfDeathEditText = findViewById(R.id.patientReviewSubmit_name_placeOfDeath_editText);
            EditText street_placeOfDeathEditText = findViewById(R.id.patientReviewSubmit_street_placeOfDeath_editText);
            EditText city_state_zip_placeOfDeathEditText = findViewById(R.id.patientReviewSubmit_city_state_zip_placeOfDeath_editView);
            EditText person_pronouncedDeathEditText = findViewById(R.id.patientReviewSubmit_sign_document_editText);
            EditText licence_pronouncedDeathEditText = findViewById(R.id.patientReviewSubmit_license_editText);
            EditText immediate_1_causeOfDeathEditText = findViewById(R.id.patientReviewSubmit_immediate_1_causeOfDeath_editText);
            EditText immediate_2_causeOfDeath_EditText = findViewById(R.id.patientReviewSubmit_immediate_2_causeOfDeath_editText);
            EditText immediate_3_causeOfDeath_EditText = findViewById(R.id.patientReviewSubmit_immediate_3_causeOfDeath_editText);
            EditText underlying_causeOfDeath_EditText = findViewById(R.id.patientReviewSubmit_underlying_causeOfDeath_editText);
            EditText name_certifierEditText = findViewById(R.id.patientReviewSubmit_name_certifier_editText);
            EditText license_certifierEditText = findViewById(R.id.patientReviewSubmit_certifier_license_editText);
            EditText street_certifierEditText = findViewById(R.id.patientReviewSubmit_street_certifier_editText);
            EditText city_state_zip_certifierEditText = findViewById(R.id.patientReviewSubmit_city_state_zip_certifier_editView);
            CheckBox certifier_sameAsPronounced_checkBox = findViewById(R.id.patientReviewSubmit_certifier_sameAsPronouncer_checkBox);

            if (bundle.containsKey("deathDate")) {
                String deathDate = bundle.getString("deathDate");
                System.out.println("Death Date is: " + deathDate);
                deathDateEditText.setText(deathDate);
            }
            if (bundle.containsKey("deathTime")) {
                String deathTime = bundle.getString("deathTime");
                deathTimeEditText.setText(deathTime);
            }
            if (bundle.containsKey("deathPlace")) {
                String deathPlace = bundle.getString("deathPlace");
                deathPlaceEditText.setText(deathPlace);
            }
            if (bundle.containsKey("name_placeOfDeath")) {
                String name_placeOfDeath = bundle.getString("name_placeOfDeath");
                name_placeOfDeathEditText.setText(name_placeOfDeath);
            }
            if (bundle.containsKey("street_placeOfDeath")) {
                String street_placeOfDeath = bundle.getString("street_placeOfDeath");
                street_placeOfDeathEditText.setText(street_placeOfDeath);
            }
            String city_state_zip_placeOfDeath = "";
            if (bundle.containsKey("city_placeOfDeath")) city_state_zip_placeOfDeath += bundle.getString("city_placeOfDeath");
            if (bundle.containsKey("state_placeOfDeath")) city_state_zip_placeOfDeath += ", " + bundle.getString("state_placeOfDeath");
            if (bundle.containsKey("zip_placeOfDeath")) city_state_zip_placeOfDeath += ", " + bundle.getString("zip_placeOfDeath");
            city_state_zip_placeOfDeathEditText.setText(city_state_zip_placeOfDeath);
            if (bundle.containsKey("signature_document")) {
                String person_pronouncedDeath = bundle.getString("signature_document");
                person_pronouncedDeathEditText.setText(person_pronouncedDeath);
            }
            if (bundle.containsKey("license_document")) {
                String license_pronouncedDeath = bundle.getString("license_document");
                licence_pronouncedDeathEditText.setText(license_pronouncedDeath);
            }
            if (bundle.containsKey("immediate_1_causeOfDeath")) {
                String immediate_1_causeOfDeath = bundle.getString("immediate_1_causeOfDeath");
                immediate_1_causeOfDeathEditText.setText(immediate_1_causeOfDeath);
            }
            if (bundle.containsKey("immediate_2_causeOfDeath")) {
                String immediate_2_causeOfDeath = bundle.getString("immediate_2_causeOfDeath");
                immediate_2_causeOfDeath_EditText.setText(immediate_2_causeOfDeath);
            }
            if (bundle.containsKey("immediate_3_causeOfDeath")) {
                String immediate_3_causeOfDeath = bundle.getString("immediate_3_causeOfDeath");
                immediate_3_causeOfDeath_EditText.setText(immediate_3_causeOfDeath);
            }
            if (bundle.containsKey("underlying_causeOfDeath")) {
                String underlying_causeOfDeath = bundle.getString("underlying_causeOfDeath");
                underlying_causeOfDeath_EditText.setText(underlying_causeOfDeath);
            }

            String name_certifier = name_certifierEditText.getText().toString();
            bundle.putString("name_certifier", name_certifier);
            String license_certifier = license_certifierEditText.getText().toString();
            bundle.putString("license_certifier", license_certifier);

            certifier_sameAsPronounced_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (certifier_sameAsPronounced_checkBox.isChecked()) {
                        if (bundle.containsKey("signature_document")) {
                            String name_certifier = bundle.getString("signature_document");
                            bundle.putString("name_certifier", name_certifier);
                            name_certifierEditText.setText(name_certifier);
                        }
                        if (bundle.containsKey("license_document")) {
                            String license_certifier = bundle.getString("license_document");
                            bundle.putString("license_certifier", license_certifier);
                            license_certifierEditText.setText(license_certifier);
                        }
                    } else {
                        String name_certifier = name_certifierEditText.getText().toString();
                        bundle.putString("name_certifier", name_certifier);
                        String license_certifier = license_certifierEditText.getText().toString();
                        bundle.putString("license_certifier", license_certifier);
                    }
                }
            });

            String street_certifier = street_certifierEditText.getText().toString();
            String city_state_zip_certifier = city_state_zip_certifierEditText.getText().toString();
            bundle.putString("street_certifier", street_certifier);
            bundle.putString("city_state_zip_certifier", city_state_zip_certifier);

        }
    }
}
