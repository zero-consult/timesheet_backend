package org.zero_consult.timesheet_backend.mappers;

import org.zero_consult.idl.model.TimesheetEntry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TimesheetEntryMapper {
    public static TimesheetEntry toIdl(org.zero_consult.timesheet_backend.entities.TimesheetEntry entity) {
        TimesheetEntry idl = new TimesheetEntry();
        idl.setId(entity.getId() != null ? Optional.of(entity.getId()) : Optional.empty());
        idl.setCustomerId(entity.getCustomerId() != null ? Optional.of(entity.getCustomerId()) : Optional.empty());
        idl.setEmployeeId(entity.getEmployeeId());
        idl.setDate(entity.getDate());
        idl.setStartTime(entity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        idl.setEndTime(entity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        idl.setDescription(entity.getDescription() != null ? Optional.of(entity.getDescription()) : Optional.empty());
        idl.setCreatedAt(entity.getCreatedAt() != null ? Optional.of(entity.getCreatedAt().toInstant(ZoneId.systemDefault().getRules().getOffset(entity.getCreatedAt())).toEpochMilli()) : Optional.empty());
        idl.setStatus(TimesheetStatusMapper.toIdl(entity.getStatus()));
        idl.setType(TimesheetTypeMapper.toIdl(entity.getType()));
        return idl;
    }

    public static org.zero_consult.timesheet_backend.entities.TimesheetEntry toEntity(TimesheetEntry timesheetEntry) {
        org.zero_consult.timesheet_backend.entities.TimesheetEntry mappedTimesheetEntry = new org.zero_consult.timesheet_backend.entities.TimesheetEntry();
        timesheetEntry.getId().ifPresent(mappedTimesheetEntry::setId);
        mappedTimesheetEntry.setEmployeeId(timesheetEntry.getEmployeeId());
        timesheetEntry.getCustomerId().ifPresent(mappedTimesheetEntry::setCustomerId);
        mappedTimesheetEntry.setDate(timesheetEntry.getDate());
        mappedTimesheetEntry.setStartTime(LocalTime.parse(timesheetEntry.getStartTime(), DateTimeFormatter.ofPattern("HH:mm")));
        mappedTimesheetEntry.setEndTime(LocalTime.parse(timesheetEntry.getEndTime(), DateTimeFormatter.ofPattern("HH:mm")));
        timesheetEntry.getDescription().ifPresent(mappedTimesheetEntry::setDescription);
        mappedTimesheetEntry.setStatus(TimesheetStatusMapper.toEntity(timesheetEntry.getStatus()));
        timesheetEntry.getCreatedAt().ifPresent((createdAt) ->mappedTimesheetEntry.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault())));
        mappedTimesheetEntry.setType(TimesheetTypeMapper.toEntity(timesheetEntry.getType()));
        return mappedTimesheetEntry;
    }
}
