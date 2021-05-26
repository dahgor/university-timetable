package controllers;

import dao.entities.Auditory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.AuditoryService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuditoriesControllerTest {
    private static final int AUDITORY_ID = 2;
    private static final String AUDITORY_LOCATION = "2nd floor, room#6";
    private static final String AUDITORY_LOCATION_2 = "1st floor, room#4";

    @Mock
    private AuditoryService auditoryService;

    private MockMvc mockMvc;

    private List<Auditory> auditories;
    private Auditory auditory = new Auditory(AUDITORY_ID, AUDITORY_LOCATION);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuditoriesController(auditoryService)).build();
    }

    @BeforeEach
    void preparingAuditories() {
        auditories = new LinkedList<>();
        auditories.add(new Auditory(1, "1st floor, room#2"));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(auditoryService.getAllItems()).thenReturn(auditories);

        mockMvc.perform(get("/auditories"))
                .andExpect(status().isOk())
                .andExpect(view().name("auditories/show-all"))
                .andExpect(model().attribute("auditories", auditories));
        verify(auditoryService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(auditoryService.findById(AUDITORY_ID)).thenReturn(auditory);

        mockMvc.perform(get("/auditories/" + AUDITORY_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("auditories/show-by-id"))
                .andExpect(model().attribute("auditory", auditory));
        verify(auditoryService).findById(AUDITORY_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/auditories/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("auditories/add-new"))
                .andExpect(model().attributeExists("auditory"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/auditories").flashAttr("auditory", auditory))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditories"));
        verify(auditoryService).save(auditory);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(auditoryService.findById(AUDITORY_ID)).thenReturn(auditory);

        mockMvc.perform(get("/auditories/" + AUDITORY_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("auditories/edit"))
                .andExpect(model().attribute("auditory", auditory));
        verify(auditoryService).findById(AUDITORY_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(auditoryService.findById(AUDITORY_ID)).thenReturn(auditory);
        Auditory changedAuditory = new Auditory(AUDITORY_ID, AUDITORY_LOCATION_2);

        mockMvc.perform(patch("/auditories/" + AUDITORY_ID).flashAttr("auditory", changedAuditory))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditories"));
        verify(auditoryService).findById(AUDITORY_ID);
        verify(auditoryService).changeLocation(changedAuditory, changedAuditory.getLocation());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(delete("/auditories/" + AUDITORY_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditories"));
        verify(auditoryService).delete(any());
    }

}