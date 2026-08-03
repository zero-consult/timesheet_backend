package org.zero_consult.timesheet_backend.services;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.zero_consult.idl.client.ApiException;
import org.zero_consult.idl.client.api.PayslipApi;
import org.zero_consult.idl.client.model.Payslip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PayslipApiMock extends PayslipApi {
    @Override
    public List<Payslip> payslipsList(@NonNull LocalDate from, @NonNull LocalDate until, @Nullable String employeeId) throws ApiException {
        if(LocalDate.parse("2026-06-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")).isEqual(from)) {
            List<Payslip> payslips = new ArrayList<>();
            Payslip payslip = new Payslip();
            payslip.setAllowances(new ArrayList<>());
            payslip.setEmployeeId(employeeId != null ? employeeId : "1");
            payslip.setGrossSalary(3000d);
            payslip.setTaxRate(37.5);
            payslip.setMonth("2026-06-01");
            payslips.add(payslip);
            return payslips;
        } else {
            return new ArrayList<>();
        }
    }
}
