package controllers;

import dao.entities.Time;
import dao.entities.TimePeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.TimePeriodService;
import services.interfaces.TimeService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TimesControllerTest {
    private static final int TIME_ID = 2;
    private static final Date DATE = Date.valueOf(LocalDate.ofEpochDay(1));
    private static final Date DATE_2 = Date.valueOf(LocalDate.ofEpochDay(2));
    private static final int TIME_PERIOD_ID = 3;
    private static final int TIME_PERIOD_ID_2 = 4;

    @Mock
    private TimeService timeService;
    @Mock
    private TimePeriodService timePeriodService;

    private MockMvc mockMvc;

    private List<Time> times;
    private Time time = new Time(TIME_ID, DATE, TIME_PERIOD_ID);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TimesController(timeService, timePeriodService))
                .build();
    }

    @BeforeEach
    void prepareTimes() {
        times = new LinkedList<>();
        times.add(new Time(1, Date.valueOf(LocalDate.ofEpochDay(0)), 1));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(timeService.getAllItems()).thenReturn(times);

        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(view().name("times/show-all"))
                .andExpect(model().attribute("times", times));
        verify(timeService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(timeService.findById(TIME_ID)).thenReturn(time);

        mockMvc.perform(get("/times/" + TIME_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("times/show-by-id"))
                .andExpect(model().attribute("time", time));
        verify(timeService).findById(TIME_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        when(timePeriodService.getAllItems()).thenReturn(new LinkedList<TimePeriod>());

        mockMvc.perform(get("/times/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("times/add-new"))
                .andExpect(model().attributeExists("time", "timePeriods"));
        verify(timePeriodService).getAllItems();
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/times").flashAttr("time", time))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/times"));
        verify(timeService).save(time);
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        ArgumentCaptor<Time> timeCaptor = ArgumentCaptor.forClass(Time.class);

        mockMvc.perform(delete("/times/" + TIME_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/times"));
        verify(timeService).delete(timeCaptor.capture());
        assertEquals(TIME_ID, timeCaptor.getValue().getId());
    }

}