package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Service;
import org.zero_consult.idl.client.api.InvoicingMonthApi;
import org.zero_consult.idl.client.api.InvoicingMonthApi;
import org.zero_consult.timesheet_backend.configuration.CustomerProperties;

@Service
public class InvoicingMonthApiService {

    private final CustomerProperties customerProperties;
    private final InvoicingMonthApi invoicingMonthApi;

    public InvoicingMonthApiService(CustomerProperties customerProperties) {
        this.customerProperties = customerProperties;
        this.invoicingMonthApi = new InvoicingMonthApi();
        this.invoicingMonthApi.setCustomBaseUrl(customerProperties.getInvoicesBackendHost());
    }

    public InvoicingMonthApi getInvoicingMonthApi() {
        return invoicingMonthApi;
    }
}
