package org.zero_consult.timesheet_backend.mappers;

import org.zero_consult.timesheet_backend.entities.TimesheetType;

public class TimesheetTypeMapper {
    public static org.zero_consult.idl.model.TimesheetType toIdl(TimesheetType type) {
        return org.zero_consult.idl.model.TimesheetType.valueOf(type.name());
    }

    public static TimesheetType toEntity(org.zero_consult.idl.model.TimesheetType type) {
        return TimesheetType.valueOf(type.name());
    }
}
