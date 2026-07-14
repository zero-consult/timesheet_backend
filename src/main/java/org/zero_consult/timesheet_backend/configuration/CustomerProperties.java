package org.zero_consult.timesheet_backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "zero-consult")
public class CustomerProperties {
    private String peopleBackendHost;

    public String getPeopleBackendHost() {
        return peopleBackendHost;
    }

    public void setPeopleBackendHost(String peopleBackendHost) {
        this.peopleBackendHost = peopleBackendHost;
    }
}
