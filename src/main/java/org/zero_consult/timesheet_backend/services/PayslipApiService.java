package org.zero_consult.timesheet_backend.services;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.zero_consult.idl.client.ApiClient;
import org.zero_consult.idl.client.Configuration;
import org.zero_consult.idl.client.api.PayslipApi;
import org.zero_consult.timesheet_backend.configuration.CustomProperties;
import org.zero_consult.timesheet_backend.security.JwtUtil;
import org.zero_consult.timesheet_backend.utils.RequestHelper;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayslipApiService {
    private final PayslipApi payslipApi;

    public PayslipApiService(CustomProperties customProperties, JwtUtil jwtUtil) {
        String jwtToken = jwtUtil.resolveToken(RequestHelper.getCurrentHttpRequest());
        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setBearerToken(jwtToken);
        this.payslipApi = new PayslipApi();
        this.payslipApi.setCustomBaseUrl(customProperties.getInvoicesBackendHost());
    }

    public PayslipApi getPayslipApi() {
        return payslipApi;
    }
}
