package com.example.projectwork;

import java.sql.SQLException;
import java.util.List;

public class AppointmentFetchTask implements Runnable {
    private AppointmentDBObject appointmentDB;
    private List<Appointments.Appointment> appointments;

    public AppointmentFetchTask(AppointmentDBObject appointmentDB) {
        this.appointmentDB = appointmentDB;
    }

    @Override
    public void run() {
        try {
            appointments = appointmentDB.getAllAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointments.Appointment> getAppointments() {
        return appointments;
    }
}

