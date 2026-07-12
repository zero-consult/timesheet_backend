package org.zero_consult.timesheet_backend.mappers;

import org.zero_consult.timesheet_backend.entities.TimesheetStatus;

public class TimesheetStatusMapper {
    public static org.zero_consult.idl.model.TimesheetStatus toIdl(TimesheetStatus status) {
        return org.zero_consult.idl.model.TimesheetStatus.valueOf(status.name());
    }

    public static TimesheetStatus toEntity(org.zero_consult.idl.model.TimesheetStatus status) {
        return TimesheetStatus.valueOf(status.name());
    }
}
