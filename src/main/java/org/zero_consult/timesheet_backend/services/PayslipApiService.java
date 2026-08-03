package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Service;
import org.zero_consult.idl.client.api.InvoicingMonthApi;
import org.zero_consult.idl.client.api.PayslipApi;
import org.zero_consult.timesheet_backend.configuration.CustomerProperties;

@Service
public class PayslipApiService {
    private final CustomerProperties customerProperties;
    private final PayslipApi payslipApi;

    public PayslipApiService(CustomerProperties customerProperties) {
        this.customerProperties = customerProperties;
        this.payslipApi = new PayslipApi();
        this.payslipApi.setCustomBaseUrl(customerProperties.getInvoicesBackendHost());
    }

    public PayslipApi getPayslipApi() {
        return payslipApi;
    }
}
