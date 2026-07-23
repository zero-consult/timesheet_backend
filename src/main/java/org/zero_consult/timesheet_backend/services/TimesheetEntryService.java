package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.idl.client.ApiException;
import org.zero_consult.idl.client.model.UpdateCustomerHasTimesheetEntriesRequest;
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
    private final TimesheetEntryRepository timesheetEntryRepository;
    private final CustomerApiService customerApiService;
    private final EmployeeApiService employeeApiService;

    public TimesheetEntryService(TimesheetEntryRepository timesheetEntryRepository, CustomerApiService customerApiService, EmployeeApiService employeeApiService) {
        this.timesheetEntryRepository = timesheetEntryRepository;
        this.customerApiService = customerApiService;
        this.employeeApiService = employeeApiService;
    }

    public List<TimesheetEntry> getAllTimesheetEntries(LocalDate from, LocalDate until) {
        return timesheetEntryRepository.findByDateBetween(from, until);
    }

    public TimesheetEntry addTimesheetEntry(TimesheetEntry entity) throws EntityNotFoundException {
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setStatus(TimesheetStatus.IN_PROGRESS);
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(true);
            customerApiService.getCustomerApi().updateCustomerHasTimesheetEntries(entity.getCustomerId(), updateCustomerHasTimesheetEntriesRequest);
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found", e);
        }
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(true);
            employeeApiService.getEmployeeApi().updateEmployeeHasTimesheetEntries(entity.getEmployeeId(), updateCustomerHasTimesheetEntriesRequest);
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
        String originalCustomerId = timesheetEntry.getCustomerId();
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(true);
            customerApiService.getCustomerApi().updateCustomerHasTimesheetEntries(entity.getCustomerId(), updateCustomerHasTimesheetEntriesRequest);
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found", e);
        }
        String originalEmployeeId = timesheetEntry.getEmployeeId();
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(true);
            employeeApiService.getEmployeeApi().updateEmployeeHasTimesheetEntries(entity.getEmployeeId(), updateCustomerHasTimesheetEntriesRequest);
        } catch (ApiException e) {
            throw new EntityNotFoundException("Employee not found", e);
        }
        timesheetEntry.setEmployeeId(entity.getEmployeeId());
        timesheetEntry.setCustomerId(entity.getCustomerId());
        timesheetEntry.setDescription(entity.getDescription());
        // don't update createdAt
        TimesheetEntry savedTimesheetEntry = timesheetEntryRepository.save(timesheetEntry);
        if (!originalCustomerId.equals(entity.getCustomerId())) {
            List<TimesheetEntry> timesheetEntriesForCustomer = timesheetEntryRepository.findByCustomerId(originalCustomerId);
            try {
                UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
                updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(!timesheetEntriesForCustomer.isEmpty());
                customerApiService.getCustomerApi().updateCustomerHasTimesheetEntries(entity.getCustomerId(), updateCustomerHasTimesheetEntriesRequest);
            } catch (ApiException e) {
                throw new EntityNotFoundException("Customer not found", e);
            }
        }
        if (!originalEmployeeId.equals(entity.getEmployeeId())) {
            List<TimesheetEntry> timesheetEntriesForEmployee = timesheetEntryRepository.findByCustomerId(originalEmployeeId);
            try {
                UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
                updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(!timesheetEntriesForEmployee.isEmpty());
                employeeApiService.getEmployeeApi().updateEmployeeHasTimesheetEntries(entity.getEmployeeId(), updateCustomerHasTimesheetEntriesRequest);
            } catch (ApiException e) {
                throw new EntityNotFoundException("Employee not found", e);
            }
        }
        return savedTimesheetEntry;
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
        List<TimesheetEntry> timesheetEntriesForCustomer = timesheetEntryRepository.findByCustomerId(timesheetEntry.getCustomerId());
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(!timesheetEntriesForCustomer.isEmpty());
            customerApiService.getCustomerApi().updateCustomerHasTimesheetEntries(timesheetEntry.getCustomerId(), updateCustomerHasTimesheetEntriesRequest);
        } catch (ApiException e) {
            throw new EntityNotFoundException("Customer not found", e);
        }
        List<TimesheetEntry> timesheetEntriesForEmployee = timesheetEntryRepository.findByCustomerId(timesheetEntry.getEmployeeId());
        try {
            UpdateCustomerHasTimesheetEntriesRequest updateCustomerHasTimesheetEntriesRequest = new UpdateCustomerHasTimesheetEntriesRequest();
            updateCustomerHasTimesheetEntriesRequest.setHasTimesheetEntries(!timesheetEntriesForEmployee.isEmpty());
            employeeApiService.getEmployeeApi().updateEmployeeHasTimesheetEntries(timesheetEntry.getEmployeeId(), updateCustomerHasTimesheetEntriesRequest);
        } catch (ApiException e) {
            throw new EntityNotFoundException("Employee not found", e);
        }
    }
}
