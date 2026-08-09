package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Service;
import org.zero_consult.idl.client.api.PayslipApi;
import org.zero_consult.timesheet_backend.configuration.CustomProperties;

@Service
public class PayslipApiService {
    private final CustomProperties customProperties;
    private final PayslipApi payslipApi;

    public PayslipApiService(CustomProperties customProperties) {
        this.customProperties = customProperties;
        this.payslipApi = new PayslipApi();
        this.payslipApi.setCustomBaseUrl(customProperties.getInvoicesBackendHost());
    }

    public PayslipApi getPayslipApi() {
        return payslipApi;
    }
}
