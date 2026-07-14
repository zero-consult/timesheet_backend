package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.idl.client.ApiException;
import org.zero_consult.idl.client.api.CustomerApi;
import org.zero_consult.idl.client.api.EmployeeApi;
import org.zero_consult.timesheet_backend.configuration.CustomerProperties;
import org.zero_consult.timesheet_backend.entities.TimesheetEntry;
import org.zero_consult.timesheet_backend.entities.TimesheetStatus;
import org.zero_consult.timesheet_backend.exceptions.EntityNotFoundException;
import org.zero_consult.timesheet_backend.exceptions.InvalidTimesheetEntryStatusUpdateException;
import org.zero_consult.timesheet_backend.repositories.TimesheetEntryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@Transactional
public class TimesheetEntryService {
    private final CustomerProperties customerProperties;
    private final TimesheetEntryRepository timesheetEntryRepository;

    public TimesheetEntryService(TimesheetEntryRepository timesheetEntryRepository, CustomerProperties customerProperties) {
        this.timesheetEntryRepository = timesheetEntryRepository;
        this.customerProperties = customerProperties;
    }

    public List<TimesheetEntry> getAllTimesheetEntries(LocalDate from, LocalDate until) {
        return timesheetEntryRepository.findByDateBetween(from, until);
    }

    public TimesheetEntry addTimesheetEntry(TimesheetEntry entity) throws EntityNotFoundException {
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setStatus(TimesheetStatus.IN_PROGRESS);
        CustomerApi customerApi = new CustomerApi();
        System.out.println("################################");
        System.out.println("Customer API: " + customerProperties.getPeopleBackendHost());
        System.out.println("################################");
        customerApi.setCustomBaseUrl(customerProperties.getPeopleBackendHost());
        try {
            customerApi.getCustomer(entity.getCustomerId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found", e);
        }
        EmployeeApi employeeApi = new EmployeeApi();
        employeeApi.setCustomBaseUrl(customerProperties.getPeopleBackendHost());
        try {
            employeeApi.getEmployee(entity.getEmployeeId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Employee not found", e);
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
        switch (timesheetEntry.getStatus()) {
            case APPROVED:
                throw new InvalidTimesheetEntryStatusUpdateException("TimesheetEntry in status ACCEPTED cannot be updated");
            case REJECTED:
                timesheetEntry.setStatus(TimesheetStatus.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                timesheetEntry.setStatus(entity.getStatus());
        }
        CustomerApi customerApi = new CustomerApi();
        customerApi.setCustomBaseUrl(customerProperties.getPeopleBackendHost());
        try {
            customerApi.getCustomer(entity.getCustomerId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found", e);
        }
        EmployeeApi employeeApi = new EmployeeApi();
        employeeApi.setCustomBaseUrl(customerProperties.getPeopleBackendHost());
        try {
            employeeApi.getEmployee(entity.getEmployeeId());
        } catch (ApiException e) {
            throw new EntityNotFoundException("Employee not found", e);
        }
        timesheetEntry.setEmployeeId(entity.getEmployeeId());
        timesheetEntry.setCustomerId(entity.getCustomerId());
        timesheetEntry.setDescription(entity.getDescription());
        // don't update createdAt
        return timesheetEntryRepository.save(timesheetEntry);
    }

    public TimesheetEntry getTimesheetEntry(String id) throws EntityNotFoundException {
        return timesheetEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimesheetEntry not found"));
    }

    public void deleteTimesheetEntry(String id) throws EntityNotFoundException, InvalidTimesheetEntryStatusUpdateException {
        TimesheetEntry timesheetEntry = timesheetEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimesheetEntry not found"));
        if (timesheetEntry.getStatus() == TimesheetStatus.APPROVED) {
            throw new InvalidTimesheetEntryStatusUpdateException("Timesheet entry may not be accepted to be deleted");
        }
        timesheetEntryRepository.deleteById(id);
    }
}
