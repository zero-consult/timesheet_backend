package org.zero_consult.timesheet_backend.mappers;

import org.junit.jupiter.api.Test;
import org.zero_consult.idl.model.TimesheetEntry;
import org.zero_consult.idl.model.TimesheetStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimesheetEntryMapperTests {
    @Test
    public void testToEntity() {
        TimesheetEntry idlTimesheetEntry = new TimesheetEntry();
        idlTimesheetEntry.setId(Optional.of("1"));
        idlTimesheetEntry.setCustomerId("2");
        idlTimesheetEntry.setEmployeeId("3");
        idlTimesheetEntry.setDate(LocalDate.of(2026, 7, 20));
        idlTimesheetEntry.setStartTime("10:00");
        idlTimesheetEntry.setEndTime("11:00");
        idlTimesheetEntry.setDescription(Optional.of("description"));
        idlTimesheetEntry.setCreatedAt(Optional.of(123456789L));
        idlTimesheetEntry.setStatus(Optional.of(TimesheetStatus.APPROVED));
        org.zero_consult.timesheet_backend.entities.TimesheetEntry entityTimesheetEntry = TimesheetEntryMapper.toEntity(idlTimesheetEntry);
        assertEquals("1", entityTimesheetEntry.getId());
        assertEquals("2", entityTimesheetEntry.getCustomerId());
        assertEquals("3", entityTimesheetEntry.getEmployeeId());
        assertEquals(LocalDate.of(2026, 7, 20), entityTimesheetEntry.getDate());
        assertEquals(LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm")), entityTimesheetEntry.getStartTime());
        assertEquals(LocalTime.parse("11:00", DateTimeFormatter.ofPattern("HH:mm")), entityTimesheetEntry.getEndTime());
        assertEquals("description", entityTimesheetEntry.getDescription());
        assertEquals(LocalDateTime.ofInstant(Instant.ofEpochMilli(123456789L), ZoneId.systemDefault()), entityTimesheetEntry.getCreatedAt());
        assertEquals(org.zero_consult.timesheet_backend.entities.TimesheetStatus.APPROVED, entityTimesheetEntry.getStatus());
    }

    @Test
    public void testToIdl() {
        org.zero_consult.timesheet_backend.entities.TimesheetEntry entityTimesheetEntry = new org.zero_consult.timesheet_backend.entities.TimesheetEntry();
        entityTimesheetEntry.setId("1");
        entityTimesheetEntry.setCustomerId("2");
        entityTimesheetEntry.setEmployeeId("3");
        entityTimesheetEntry.setDate(LocalDate.of(2026, 7, 20));
        entityTimesheetEntry.setStartTime(LocalTime.parse("10:00", DateTimeFormatter.ofPattern("HH:mm")));
        entityTimesheetEntry.setEndTime(LocalTime.parse("11:00", DateTimeFormatter.ofPattern("HH:mm")));
        entityTimesheetEntry.setDescription("description");
        entityTimesheetEntry.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(123456789L), ZoneId.systemDefault()));
        entityTimesheetEntry.setStatus(org.zero_consult.timesheet_backend.entities.TimesheetStatus.APPROVED);
        TimesheetEntry idlTimesheetEntry = TimesheetEntryMapper.toIdl(entityTimesheetEntry);
        assertEquals(Optional.of("1"), idlTimesheetEntry.getId());
        assertEquals("2", idlTimesheetEntry.getCustomerId());
        assertEquals("3", idlTimesheetEntry.getEmployeeId());
        assertEquals(LocalDate.of(2026, 7, 20), idlTimesheetEntry.getDate());
        assertEquals("10:00", idlTimesheetEntry.getStartTime());
        assertEquals("11:00", idlTimesheetEntry.getEndTime());
        assertEquals(Optional.of("description"), idlTimesheetEntry.getDescription());
        assertEquals(Optional.of(123456789L), idlTimesheetEntry.getCreatedAt());
        assertEquals(Optional.of(TimesheetStatus.APPROVED), idlTimesheetEntry.getStatus());
    }
}
