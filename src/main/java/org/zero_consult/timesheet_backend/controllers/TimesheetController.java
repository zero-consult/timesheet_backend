package org.zero_consult.timesheet_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.TimesheetsApi;
import org.zero_consult.idl.model.TimesheetEntry;
import org.zero_consult.timesheet_backend.exceptions.EntityNotFoundException;
import org.zero_consult.timesheet_backend.exceptions.InvalidTimesheetEntryStatusUpdateException;
import org.zero_consult.timesheet_backend.mappers.TimesheetEntryMapper;
import org.zero_consult.timesheet_backend.services.TimesheetEntryService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5101"})
@RestController
public class TimesheetController implements TimesheetsApi {
    private final TimesheetEntryService timesheetEntryService;

    public TimesheetController(TimesheetEntryService timesheetEntryService) {
        this.timesheetEntryService = timesheetEntryService;
    }

    @Override
    public ResponseEntity<TimesheetEntry> addTimesheetEntry(TimesheetEntry timesheetEntry) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(TimesheetEntryMapper.toIdl(timesheetEntryService.addTimesheetEntry(TimesheetEntryMapper.toEntity(timesheetEntry))));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public ResponseEntity<List<TimesheetEntry>> timesheetsList(LocalDate from, LocalDate until) {
        return ResponseEntity.ok(
                timesheetEntryService
                        .getAllTimesheetEntries(from, until)
                        .stream()
                        .map(TimesheetEntryMapper::toIdl)
                        .toList());
    }

    @Override
    public ResponseEntity<TimesheetEntry> getTimesheetEntry(String id) {
        try {
            return ResponseEntity.ok(TimesheetEntryMapper.toIdl(timesheetEntryService.getTimesheetEntry(id)));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public ResponseEntity<TimesheetEntry> updateTimesheetEntry(String id, TimesheetEntry timesheetEntry) {
        try {
            return ResponseEntity.ok(TimesheetEntryMapper.toIdl(timesheetEntryService.updateTimesheetEntry(id, TimesheetEntryMapper.toEntity(timesheetEntry))));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvalidTimesheetEntryStatusUpdateException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public ResponseEntity<Void> deleteTimesheetEntry(String id) {
        try {
            timesheetEntryService.deleteTimesheetEntry(id);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        } catch (InvalidTimesheetEntryStatusUpdateException e) {
            throw new RuntimeException(e); // TODO
        }
        return ResponseEntity.noContent().build();
    }
}
