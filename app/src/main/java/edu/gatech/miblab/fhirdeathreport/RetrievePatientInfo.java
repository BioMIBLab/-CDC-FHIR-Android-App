package edu.gatech.miblab.fhirdeathreport;

import org.hl7.fhir.dstu3.model.Patient;

public class RetrievePatientInfo {
/*
    System.out.println(results.getEntry().get(0).getFullUrl());
    String url = results.getEntry().get(0).getFullUrl();
    Patient patient = client.read().resource(Patient.class).withUrl(url).execute();

            System.out.println(patient.getGender().toString());

    // add values to myPatient object
    String givenName = patient.getName().get(0).getGivenAsSingleString();
            myPatient.addName().addGiven(givenName);

    String familyName = patient.getName().get(0).getFamily();
            System.out.println(familyName);
            myPatient.addName().setFamily(familyName);

            myPatient.setBirthDate(patient.getBirthDate());
            myPatient.setGender(patient.getGender());
            myPatient.setDeceased(patient.getDeceased());
    //myPatient.getAddress().get(0).setText(patient.getAddress().get(0).getText());
            myPatient.addAddress(patient.getAddressFirstRep());
            System.out.println("This address is: " + patient.getAddress().get(0).getText());
            System.out.println(patient.getDeceasedDateTimeType());

            System.out.println("Found " + results.getEntry().size() + " patients");

*/
}
