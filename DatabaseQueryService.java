package com.example.projectwork;

import com.example.projectwork.AppointmentDBObject;
import com.example.projectwork.AppointmentFetchTask;
import com.example.projectwork.Appointments;
import com.example.projectwork.*;

import java.util.List;

public class DatabaseQueryService {
    public void fetchAllData() {
        PatientDBObject patientDB = new PatientDBObject();
        com.example.projectwork.AppointmentDBObject appointmentDB = new AppointmentDBObject();

        com.example.projectwork.PatientFetchTask patientTask = new com.example.projectwork.PatientFetchTask(patientDB);
        com.example.projectwork.AppointmentFetchTask appointmentTask = new AppointmentFetchTask(appointmentDB);

        Thread patientThread = new Thread(patientTask);
        Thread appointmentThread = new Thread(appointmentTask);

        // Start the threads
        patientThread.start();
        appointmentThread.start();

        try {
            // Wait for both threads to complete
            patientThread.join();
            appointmentThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Retrieve the results
        List<Patients.Patient> patients = patientTask.getPatients();
        List<Appointments.Appointment> appointments = appointmentTask.getAppointments();

        // Print or process the results
        System.out.println("Patients: " + patients);
        System.out.println("Appointments: " + appointments);
    }

    public static void main(String[] args) {
        DatabaseQueryService service = new DatabaseQueryService();
        service.fetchAllData();
    }
}

