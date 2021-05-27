package controllers;

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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TimePeriodsControllerTest {
    private static final int TIME_PERIOD_ID = 2;
    private static final Timestamp START = Timestamp.valueOf(LocalDateTime.of(
            LocalDate.of(1970, 1, 1), LocalTime.of(12, 0)));
    private static final Timestamp START_2 = Timestamp.valueOf(LocalDateTime.of(
            LocalDate.of(1970, 1, 1), LocalTime.of(13, 0)));
    private static final Timestamp END = Timestamp.valueOf(LocalDateTime.of(
            LocalDate.of(1970, 1, 1), LocalTime.of(14, 0)));
    private static final Timestamp END_2 = Timestamp.valueOf(LocalDateTime.of(
            LocalDate.of(1970, 1, 1), LocalTime.of(15, 0)));

    @Mock
    private TimePeriodService timePeriodService;

    private MockMvc mockMvc;

    private List<TimePeriod> timePeriods;
    private TimePeriod timePeriod = new TimePeriod(TIME_PERIOD_ID, START, END);


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TimePeriodsController(timePeriodService)).build();
    }

    @BeforeEach
    void prepareTimePeriods() {

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)));
        timePeriods = new LinkedList<>();
        timePeriods.add(new TimePeriod(1, start, end));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(timePeriodService.getAllItems()).thenReturn(timePeriods);

        mockMvc.perform(get("/timePeriods"))
                .andExpect(status().isOk())
                .andExpect(view().name("timePeriods/show-all"))
                .andExpect(model().attribute("timePeriods", timePeriods));
        verify(timePeriodService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(timePeriodService.findById(TIME_PERIOD_ID)).thenReturn(timePeriod);

        mockMvc.perform(get("/timePeriods/" + TIME_PERIOD_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("timePeriods/show-by-id"))
                .andExpect(model().attribute("timePeriod", timePeriod));
        verify(timePeriodService).findById(TIME_PERIOD_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/timePeriods/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("timePeriods/add-new"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/timePeriods").param("start", "12:00")
                .param("end", "14:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/timePeriods"));
        verify(timePeriodService).save(new TimePeriod(START, END));
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(timePeriodService.findById(TIME_PERIOD_ID)).thenReturn(timePeriod);

        mockMvc.perform(get("/timePeriods/" + TIME_PERIOD_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("timePeriods/edit"))
                .andExpect(model().attribute("timePeriod", timePeriod));
        verify(timePeriodService).findById(TIME_PERIOD_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(timePeriodService.findById(TIME_PERIOD_ID)).thenReturn(timePeriod);
        TimePeriod changedTimePeriod = new TimePeriod(TIME_PERIOD_ID, START_2, END_2);
        ArgumentCaptor<TimePeriod> timePeriodCaptor = ArgumentCaptor.forClass(TimePeriod.class);
        ArgumentCaptor<Timestamp> timestampCaptor = ArgumentCaptor.forClass(Timestamp.class);

        mockMvc.perform(patch("/timePeriods/" + TIME_PERIOD_ID).param("start", "13:00")
                .param("end", "15:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/timePeriods"));
        verify(timePeriodService).findById(TIME_PERIOD_ID);
        verify(timePeriodService).changeStartHour(timePeriodCaptor.capture(), timestampCaptor.capture());
        assertEquals(changedTimePeriod.getId(), timePeriodCaptor.getValue().getId());
        assertEquals(changedTimePeriod.getStartHour(), timestampCaptor.getValue());
        verify(timePeriodService).changeEndHour(timePeriodCaptor.capture(), timestampCaptor.capture());
        assertEquals(changedTimePeriod.getId(), timePeriodCaptor.getValue().getId());
        assertEquals(changedTimePeriod.getEndHour(), timestampCaptor.getValue());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        ArgumentCaptor<TimePeriod> timePeriodCaptor = ArgumentCaptor.forClass(TimePeriod.class);

        mockMvc.perform(delete("/timePeriods/" + TIME_PERIOD_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/timePeriods"));
        verify(timePeriodService).delete(timePeriodCaptor.capture());
        assertEquals(TIME_PERIOD_ID, timePeriodCaptor.getValue().getId());
    }

}