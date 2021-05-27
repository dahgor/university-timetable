package controllers;

import dao.entities.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.ProfessorService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfessorsControllerTest {
    private static final int PROFESSOR_ID = 2;
    private static final String PROFESSOR_FIRST_NAME = "Mike";
    private static final String PROFESSOR_FIRST_NAME_2 = "Mike2";
    private static final String PROFESSOR_LAST_NAME = "McWort";
    private static final String PROFESSOR_LAST_NAME_2 = "McWort2";

    @Mock
    private ProfessorService professorService;

    private MockMvc mockMvc;

    private List<Professor> professors;
    private Professor professor = new Professor(PROFESSOR_ID, PROFESSOR_FIRST_NAME, PROFESSOR_LAST_NAME);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProfessorsController(professorService)).build();
    }

    @BeforeEach
    void prepareProfessors() {
        professors = new LinkedList<>();
        professors.add(new Professor(1, "John", "Smith"));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(professorService.getAllItems()).thenReturn(professors);

        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(view().name("professors/show-all"))
                .andExpect(model().attribute("professors", professors));
        verify(professorService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);

        mockMvc.perform(get("/professors/" + PROFESSOR_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("professors/show-by-id"))
                .andExpect(model().attribute("professor", professor));
        verify(professorService).findById(PROFESSOR_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/professors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("professors/add-new"))
                .andExpect(model().attributeExists("professor"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/professors").flashAttr("professor", professor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professors"));
        verify(professorService).save(professor);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);

        mockMvc.perform(get("/professors/" + PROFESSOR_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("professors/edit"))
                .andExpect(model().attribute("professor", professor));
        verify(professorService).findById(PROFESSOR_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        Professor changedProfessor = new Professor(PROFESSOR_ID, PROFESSOR_FIRST_NAME_2, PROFESSOR_LAST_NAME_2);

        mockMvc.perform(patch("/professors/" + PROFESSOR_ID).flashAttr("professor", changedProfessor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professors"));
        verify(professorService).findById(PROFESSOR_ID);
        verify(professorService).changeFirstName(changedProfessor, changedProfessor.getFirstName());
        verify(professorService).changeLastName(changedProfessor, changedProfessor.getLastName());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(delete("/professors/" + PROFESSOR_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professors"));
        verify(professorService).delete(any());
    }

}