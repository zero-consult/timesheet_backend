package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Service;
import org.zero_consult.idl.client.api.InvoicingMonthApi;
import org.zero_consult.timesheet_backend.configuration.CustomProperties;

@Service
public class InvoicingMonthApiService {

    private final CustomProperties customProperties;
    private final InvoicingMonthApi invoicingMonthApi;

    public InvoicingMonthApiService(CustomProperties customProperties) {
        this.customProperties = customProperties;
        this.invoicingMonthApi = new InvoicingMonthApi();
        this.invoicingMonthApi.setCustomBaseUrl(customProperties.getInvoicesBackendHost());
    }

    public InvoicingMonthApi getInvoicingMonthApi() {
        return invoicingMonthApi;
    }
}
