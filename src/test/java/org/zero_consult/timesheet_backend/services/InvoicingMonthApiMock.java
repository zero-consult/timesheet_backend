package org.zero_consult.timesheet_backend.services;

import org.jspecify.annotations.NonNull;
import org.zero_consult.idl.client.ApiException;
import org.zero_consult.idl.client.api.InvoicingMonthApi;
import org.zero_consult.idl.client.model.Employee;
import org.zero_consult.idl.client.model.UpdateCustomerHasTimesheetEntriesRequest;

public class InvoicingMonthApiMock extends InvoicingMonthApi {
    @Override
    public String closeCurrentPayslipMonth() throws ApiException {
        return "2026-07-01";
    }

    @Override
    public String getCurrentPayslipMonth() throws ApiException {
        return "2026-06-01";
    }
}
