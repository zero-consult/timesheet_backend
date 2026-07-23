package org.zero_consult.timesheet_backend.controllers;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zero_consult.timesheet_backend.TimesheetBackendApplication;
import org.zero_consult.timesheet_backend.entities.TimesheetEntry;
import org.zero_consult.timesheet_backend.entities.TimesheetStatus;
import org.zero_consult.timesheet_backend.repositories.TimesheetEntryRepository;
import org.zero_consult.timesheet_backend.services.CustomerApiMock;
import org.zero_consult.timesheet_backend.services.CustomerApiService;
import org.zero_consult.timesheet_backend.services.EmployeeApiMock;
import org.zero_consult.timesheet_backend.services.EmployeeApiService;

import java.io.File;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TimesheetBackendApplication.class)
@AutoConfigureMockMvc
public class TimesheetControllerTests {
    private final MockMvc mvc;
    private final TimesheetEntryRepository timesheetEntryRepository;
    @MockitoBean
    private CustomerApiService customerApiService;
    @MockitoBean
    private EmployeeApiService employeeApiService;

    public TimesheetControllerTests(@Autowired MockMvc mvc, @Autowired TimesheetEntryRepository timesheetEntryRepository) {
        this.mvc = mvc;
        this.timesheetEntryRepository = timesheetEntryRepository;
    }

    @Test
    public void testTimesheetsList() throws Exception {
        initData();
        mvc.perform(MockMvcRequestBuilders.get("/timesheets?from=2026-07-20&until=2026-07-27")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("description")));
    }

    @Test
    public void testAddTimesheet() throws Exception {
        initData();
        Mockito.when(customerApiService.getCustomerApi()).thenReturn(new CustomerApiMock());
        Mockito.when(employeeApiService.getEmployeeApi()).thenReturn(new EmployeeApiMock());
        mvc.perform(MockMvcRequestBuilders.post("/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/AddTImesheetEntry.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testGetTimesheetEntry() throws Exception {
        TimesheetEntry data = initData();
        mvc.perform(MockMvcRequestBuilders.get("/timesheets/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testUpdateTimesheetEntry() throws Exception {
        TimesheetEntry data = initData();
        Mockito.when(customerApiService.getCustomerApi()).thenReturn(new CustomerApiMock());
        Mockito.when(employeeApiService.getEmployeeApi()).thenReturn(new EmployeeApiMock());
        mvc.perform(MockMvcRequestBuilders.put("/timesheets/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/UpdateTimesheetEntry.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateTimesheetEntryInvalidTimesheetEntryStatusUpdateException() throws Exception {
        TimesheetEntry data = initData(TimesheetStatus.APPROVED);
        Mockito.when(customerApiService.getCustomerApi()).thenReturn(new CustomerApiMock());
        Mockito.when(employeeApiService.getEmployeeApi()).thenReturn(new EmployeeApiMock());
        mvc.perform(MockMvcRequestBuilders.put("/timesheets/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/UpdateTimesheetEntry.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testDeleteTimesheetEntry() throws Exception {
        TimesheetEntry data = initData();
        Mockito.when(customerApiService.getCustomerApi()).thenReturn(new CustomerApiMock());
        Mockito.when(employeeApiService.getEmployeeApi()).thenReturn(new EmployeeApiMock());
        mvc.perform(MockMvcRequestBuilders.delete("/timesheets/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testDeleteTimesheetEntryEntityNotFoundException() throws Exception {
        TimesheetEntry data = initData();
        Mockito.when(customerApiService.getCustomerApi()).thenReturn(new CustomerApiMock());
        Mockito.when(employeeApiService.getEmployeeApi()).thenReturn(new EmployeeApiMock());
        mvc.perform(MockMvcRequestBuilders.delete("/timesheets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    private TimesheetEntry initData() {
        return initData(TimesheetStatus.IN_PROGRESS);
    }

    private TimesheetEntry initData(TimesheetStatus status) {
        TimesheetEntry entity = new TimesheetEntry();
        entity.setCustomerId("1");
        entity.setEmployeeId("2");
        entity.setDate(java.time.LocalDate.of(2026, 7, 20));
        entity.setStartTime(java.time.LocalTime.of(10, 0));
        entity.setEndTime(java.time.LocalTime.of(18, 0));
        entity.setDescription("description");
        entity.setStatus(status);
        entity.setCreatedAt(java.time.LocalDateTime.of(2026, 7, 20, 10, 0));
        return timesheetEntryRepository.save(entity);
    }

}
