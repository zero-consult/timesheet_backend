package org.zero_consult.timesheet_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero_consult.timesheet_backend.entities.TimesheetEntry;

import java.time.LocalDate;
import java.util.List;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, String> {
    public List<TimesheetEntry> findByDateBetween(LocalDate from, LocalDate until);

    public List<TimesheetEntry> findByCustomerId(String customerId);

    public List<TimesheetEntry> findByEmployeeId(String employeeId);
}
