package org.zero_consult.timesheet_backend.mappers;

import org.zero_consult.idl.model.TimesheetEntry;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TimesheetEntryMapper {
    public static TimesheetEntry toIdl(org.zero_consult.timesheet_backend.entities.TimesheetEntry TimesheetEntry) {
        TimesheetEntry mappedTimesheetEntry = new TimesheetEntry();
        mappedTimesheetEntry.setId(TimesheetEntry.getId() != null ? Optional.of(TimesheetEntry.getId()) : Optional.empty());
        mappedTimesheetEntry.setCustomerId(TimesheetEntry.getCustomerId());
        mappedTimesheetEntry.setEmployeeId(TimesheetEntry.getEmployeeId());
        mappedTimesheetEntry.setDate(TimesheetEntry.getDate());
        mappedTimesheetEntry.setStartTime(TimesheetEntry.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        mappedTimesheetEntry.setEndTime(TimesheetEntry.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        mappedTimesheetEntry.setDescription(TimesheetEntry.getDescription() != null ? Optional.of(TimesheetEntry.getDescription()) : Optional.empty());
        mappedTimesheetEntry.setCreatedAt(TimesheetEntry.getCreatedAt() != null ? Optional.of(TimesheetEntry.getCreatedAt().toInstant(ZoneId.systemDefault().getRules().getOffset(TimesheetEntry.getCreatedAt())).toEpochMilli()) : Optional.empty());
        mappedTimesheetEntry.setStatus(TimesheetEntry.getStatus() != null ? Optional.of(TimesheetStatusMapper.toIdl(TimesheetEntry.getStatus())) : Optional.empty());
        return mappedTimesheetEntry;
    }

    public static org.zero_consult.timesheet_backend.entities.TimesheetEntry toEntity(TimesheetEntry timesheetEntry) {
        org.zero_consult.timesheet_backend.entities.TimesheetEntry mappedTimesheetEntry = new org.zero_consult.timesheet_backend.entities.TimesheetEntry();
        timesheetEntry.getId().ifPresent(mappedTimesheetEntry::setId);
        mappedTimesheetEntry.setEmployeeId(timesheetEntry.getEmployeeId());
        mappedTimesheetEntry.setCustomerId(timesheetEntry.getCustomerId());
        mappedTimesheetEntry.setDate(timesheetEntry.getDate());
        mappedTimesheetEntry.setStartTime(LocalTime.parse(timesheetEntry.getStartTime(), DateTimeFormatter.ofPattern("HH:mm")));
        mappedTimesheetEntry.setEndTime(LocalTime.parse(timesheetEntry.getEndTime(), DateTimeFormatter.ofPattern("HH:mm")));
        timesheetEntry.getDescription().ifPresent(mappedTimesheetEntry::setDescription);
        timesheetEntry.getStatus().ifPresent((status) -> mappedTimesheetEntry.setStatus(TimesheetStatusMapper.toEntity(status)));
        return mappedTimesheetEntry;
    }
}
