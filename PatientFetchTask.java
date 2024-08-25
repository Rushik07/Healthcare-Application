package com.example.projectwork;

import java.sql.SQLException;
import java.util.List;

public class PatientFetchTask implements Runnable {
    private PatientDBObject patientDB;
    private List<Patients.Patient> patients;

    public PatientFetchTask(PatientDBObject patientDB) {
        this.patientDB = patientDB;
    }

    @Override
    public void run() {
        try {
            patients = patientDB.getAllPatients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patients.Patient> getPatients() {
        return patients;
    }
}

