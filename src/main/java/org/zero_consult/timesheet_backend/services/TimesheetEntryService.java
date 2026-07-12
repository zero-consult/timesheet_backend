package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Controller;
import org.zero_consult.idl.client.ApiException;
import org.zero_consult.idl.client.api.CustomerApi;
import org.zero_consult.idl.client.api.EmployeeApi;
import org.zero_consult.idl.client.model.Customer;
import org.zero_consult.idl.client.model.Employee;
import org.zero_consult.timesheet_backend.entities.TimesheetEntry;
import org.zero_consult.timesheet_backend.entities.TimesheetStatus;
import org.zero_consult.timesheet_backend.exceptions.EntityNotFoundException;
import org.zero_consult.timesheet_backend.exceptions.InvalidTimesheetEntryStatusUpdateException;
import org.zero_consult.timesheet_backend.repositories.TimesheetEntryRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class TimesheetEntryService {
    private final TimesheetEntryRepository timesheetEntryRepository;

    public TimesheetEntryService(TimesheetEntryRepository timesheetEntryRepository) {
        this.timesheetEntryRepository = timesheetEntryRepository;
    }

    public List<TimesheetEntry> getAllTimesheetEntrys() {
        return timesheetEntryRepository.findAll();
    }

    public TimesheetEntry addTimesheetEntry(TimesheetEntry entity) throws EntityNotFoundException {
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setStatus(TimesheetStatus.IN_PROGRESS);
        CustomerApi customerApi = new CustomerApi();
        try {
            customerApi.getCustomer(entity.getCustomerId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found");
        }
        EmployeeApi employeeApi = new EmployeeApi();
        try {
            employeeApi.getEmployee(entity.getEmployeeId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Employee not found");
        }
        return timesheetEntryRepository.save(entity);
    }

    public TimesheetEntry updateTimesheetEntry(String id, TimesheetEntry entity) throws EntityNotFoundException, InvalidTimesheetEntryStatusUpdateException {
        Optional<TimesheetEntry> timesheetEntryById = timesheetEntryRepository.findById(id);
        if (timesheetEntryById.isEmpty()) {
            throw new EntityNotFoundException("TimesheetEntry not found");
        }
        TimesheetEntry timesheetEntry = timesheetEntryById.get();
        timesheetEntry.setDate(entity.getDate());
        timesheetEntry.setStartTime(entity.getStartTime());
        timesheetEntry.setEndTime(entity.getEndTime());
        switch(timesheetEntry.getStatus()) {
            case ACCEPTED:
                if(entity.getStatus().equals(TimesheetStatus.REJECTED)) {
                    throw new InvalidTimesheetEntryStatusUpdateException("TimesheetEntry status cannot be updated to REJECTED");
                }
                if(entity.getStatus().equals(TimesheetStatus.IN_PROGRESS)) {
                    throw new InvalidTimesheetEntryStatusUpdateException("TimesheetEntry status cannot be updated to IN_PROGRESS");
                }
                break;
            case REJECTED:
                if(entity.getStatus().equals(TimesheetStatus.ACCEPTED)) {
                    throw new InvalidTimesheetEntryStatusUpdateException("TimesheetEntry status cannot be updated to REJECTED");
                }
                if(entity.getStatus().equals(TimesheetStatus.IN_PROGRESS)) {
                    throw new InvalidTimesheetEntryStatusUpdateException("TimesheetEntry status cannot be updated to IN_PROGRESS");
                }
                break;
        }
        timesheetEntry.setStatus(entity.getStatus());
        timesheetEntry.setEmployeeId(entity.getEmployeeId());
        timesheetEntry.setCustomerId(entity.getCustomerId());
        timesheetEntry.setDescription(entity.getDescription());
        // don't update createdAt
        return timesheetEntryRepository.save(timesheetEntry);
    }

    public TimesheetEntry getTimesheetEntry(String id) throws EntityNotFoundException {
        return timesheetEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimesheetEntry not found"));
    }

}
