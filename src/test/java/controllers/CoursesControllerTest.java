package controllers;

import dao.entities.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.CourseService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {
    private static final int COURSE_ID = 2;
    private static final String COURSE_NAME = "Physics";
    private static final String COURSE_NAME_2 = "Physics2";
    private static final String COURSE_DESCRIPTION = "description";
    private static final String COURSE_DESCRIPTION_2 = "description2";

    @Mock
    private CourseService courseService;

    private MockMvc mockMvc;

    private List<Course> courses;
    private Course course = new Course(COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CoursesController(courseService)).build();
    }

    @BeforeEach
    void prepareCourses() {
        courses = new LinkedList<>();
        courses.add(new Course(1, "Math", "description"));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(courseService.getAllItems()).thenReturn(courses);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/show-all"))
                .andExpect(model().attribute("courses", courses));
        verify(courseService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(courseService.findById(COURSE_ID)).thenReturn(course);

        mockMvc.perform(get("/courses/" + COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/show-by-id"))
                .andExpect(model().attribute("course", course));
        verify(courseService).findById(COURSE_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/add-new"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/courses").flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService).save(course);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(courseService.findById(COURSE_ID)).thenReturn(course);

        mockMvc.perform(get("/courses/" + COURSE_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/edit"))
                .andExpect(model().attribute("course", course));
        verify(courseService).findById(COURSE_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(courseService.findById(COURSE_ID)).thenReturn(course);
        Course changedCourse = new Course(COURSE_ID, COURSE_NAME_2, COURSE_DESCRIPTION_2);

        mockMvc.perform(patch("/courses/" + COURSE_ID).flashAttr("course", changedCourse))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService).findById(COURSE_ID);
        verify(courseService).changeName(changedCourse, changedCourse.getName());
        verify(courseService).changeDescription(changedCourse, changedCourse.getDescription());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(delete("/courses/" + COURSE_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService).delete(any());
    }


}