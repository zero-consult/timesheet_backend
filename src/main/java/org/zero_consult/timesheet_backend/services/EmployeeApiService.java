package org.zero_consult.timesheet_backend.services;

import org.springframework.stereotype.Service;
import org.zero_consult.idl.client.api.EmployeeApi;
import org.zero_consult.timesheet_backend.configuration.CustomProperties;

@Service
public class EmployeeApiService {

    private final CustomProperties customProperties;
    private final EmployeeApi employeeApi;

    public EmployeeApiService(CustomProperties customProperties) {
        this.customProperties = customProperties;
        this.employeeApi = new EmployeeApi();
        this.employeeApi.setCustomBaseUrl(customProperties.getPeopleBackendHost());
    }

    public EmployeeApi getEmployeeApi() {
        return employeeApi;
    }
}
