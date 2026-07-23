package org.zero_consult.timesheet_backend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.zero_consult.idl.model.TimesheetStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimesheetStatusMapperTests {

    @Test
    public void testToEntity() {
        assertEquals(
                org.zero_consult.timesheet_backend.entities.TimesheetStatus.APPROVED,
                TimesheetStatusMapper.toEntity(TimesheetStatus.APPROVED)
        );
        assertEquals(
                org.zero_consult.timesheet_backend.entities.TimesheetStatus.IN_PROGRESS,
                TimesheetStatusMapper.toEntity(TimesheetStatus.IN_PROGRESS)
        );
        assertEquals(
                org.zero_consult.timesheet_backend.entities.TimesheetStatus.REJECTED,
                TimesheetStatusMapper.toEntity(TimesheetStatus.REJECTED)
        );
    }

    @Test
    public void testToIdl() {
        assertEquals(
                TimesheetStatus.APPROVED,
                TimesheetStatusMapper.toIdl(org.zero_consult.timesheet_backend.entities.TimesheetStatus.APPROVED)
        );
        assertEquals(
                TimesheetStatus.IN_PROGRESS,
                TimesheetStatusMapper.toIdl(org.zero_consult.timesheet_backend.entities.TimesheetStatus.IN_PROGRESS)
        );
        assertEquals(
                TimesheetStatus.REJECTED,
                TimesheetStatusMapper.toIdl(org.zero_consult.timesheet_backend.entities.TimesheetStatus.REJECTED)
        );
    }
}
