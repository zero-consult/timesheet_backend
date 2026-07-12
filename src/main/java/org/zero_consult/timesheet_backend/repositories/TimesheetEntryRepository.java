package org.zero_consult.timesheet_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero_consult.timesheet_backend.entities.TimesheetEntry;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, String> {
}
