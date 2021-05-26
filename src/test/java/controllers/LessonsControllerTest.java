package controllers;

import dao.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LessonsControllerTest {
    private static final int LESSON_ID = 2;
    private static final int COURSE_ID = 3;
    private static final int PROFESSOR_ID = 4;
    private static final int GROUP_ID = 5;
    private static final int AUDITORY_ID = 6;
    private static final int COURSE_ID_2 = 7;
    private static final int PROFESSOR_ID_2 = 8;
    private static final int GROUP_ID_2 = 9;
    private static final int AUDITORY_ID_2 = 10;

    @Mock
    private LessonService lessonService;
    @Mock
    private CourseService courseService;
    @Mock
    private GroupService groupService;
    @Mock
    private ProfessorService professorService;
    @Mock
    private AuditoryService auditoryService;

    private MockMvc mockMvc;

    private List<Lesson> lessons;
    private Lesson lesson = new Lesson(LESSON_ID, COURSE_ID, PROFESSOR_ID, GROUP_ID, AUDITORY_ID);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LessonsController(lessonService, courseService,
                professorService, groupService, auditoryService)).build();
    }

    @BeforeEach
    void prepareLessons() {
        lessons = new LinkedList<>();
        lessons.add(new Lesson(1, 1, 1, 1, 1));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(lessonService.getAllItems()).thenReturn(lessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/show-all"))
                .andExpect(model().attribute("lessons", lessons));
        verify(lessonService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(lessonService.findById(LESSON_ID)).thenReturn(lesson);

        mockMvc.perform(get("/lessons/" + LESSON_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/show-by-id"))
                .andExpect(model().attribute("lesson", lesson));
        verify(lessonService).findById(LESSON_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/lessons/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/add-new"))
                .andExpect(model().attributeExists("lesson", "courses", "professors", "groups", "auditories"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/lessons").flashAttr("lesson", lesson))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));
        verify(lessonService).save(lesson);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(lessonService.findById(LESSON_ID)).thenReturn(lesson);

        mockMvc.perform(get("/lessons/" + LESSON_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons/edit"))
                .andExpect(model().attribute("lesson", lesson))
                .andExpect(model().attributeExists("courses", "groups", "professors",
                        "auditories"));
        verify(lessonService).findById(LESSON_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(lessonService.findById(LESSON_ID)).thenReturn(lesson);
        Lesson changedLesson = new Lesson(LESSON_ID, COURSE_ID_2, PROFESSOR_ID_2, GROUP_ID_2, AUDITORY_ID_2);
        ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        ArgumentCaptor<Professor> professorCaptor = ArgumentCaptor.forClass(Professor.class);
        ArgumentCaptor<Group> groupCaptor = ArgumentCaptor.forClass(Group.class);
        ArgumentCaptor<Auditory> auditoryCaptor = ArgumentCaptor.forClass(Auditory.class);

        mockMvc.perform(patch("/lessons/" + LESSON_ID).flashAttr("lesson", changedLesson))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));
        verify(lessonService).findById(LESSON_ID);
        verify(lessonService).changeCourse(lessonCaptor.capture(), courseCaptor.capture());
        assertEquals(changedLesson.getCourseId(), courseCaptor.getValue().getId());
        assertEquals(changedLesson, lessonCaptor.getValue());
        verify(lessonService).changeProfessor(lessonCaptor.capture(), professorCaptor.capture());
        assertEquals(changedLesson.getProfessorId(), professorCaptor.getValue().getId());
        assertEquals(changedLesson, lessonCaptor.getValue());
        verify(lessonService).changeGroup(lessonCaptor.capture(), groupCaptor.capture());
        assertEquals(changedLesson.getGroupId(), groupCaptor.getValue().getId());
        assertEquals(changedLesson, lessonCaptor.getValue());
        verify(lessonService).changeAuditory(lessonCaptor.capture(), auditoryCaptor.capture());
        assertEquals(changedLesson.getAuditoryId(), auditoryCaptor.getValue().getId());
        assertEquals(changedLesson, lessonCaptor.getValue());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(delete("/lessons/" + LESSON_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));
        verify(lessonService).delete(new Lesson(LESSON_ID));
    }

}