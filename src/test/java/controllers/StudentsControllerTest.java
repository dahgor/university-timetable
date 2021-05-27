package controllers;

import dao.entities.Group;
import dao.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.GroupService;
import services.interfaces.StudentService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {
    private static final int STUDENT_ID = 2;
    private static final int GROUP_ID = 5;
    private static final int GROUP_ID_2 = 6;
    private static final String FIRST_NAME = "John";
    private static final String FIRST_NAME_2 = "John2";
    private static final String LAST_NAME = "Wick";
    private static final String LAST_NAME_2 = "Wick2";

    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;

    private List<Student> students;
    private Student student = new Student(STUDENT_ID, GROUP_ID, FIRST_NAME, LAST_NAME);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentsController(studentService, groupService)).build();
    }

    @BeforeEach
    void prepareStudents() {
        students = new LinkedList<>();
        students.add(new Student(1, 1, "Mike", "Smith"));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(studentService.getAllItems()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/show-all"))
                .andExpect(model().attribute("students", students));
        verify(studentService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(studentService.findById(STUDENT_ID)).thenReturn(student);

        mockMvc.perform(get("/students/" + STUDENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("students/show-by-id"))
                .andExpect(model().attribute("student", student));
        verify(studentService).findById(STUDENT_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        when(groupService.getAllItems()).thenReturn(new LinkedList<Group>());

        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/add-new"))
                .andExpect(model().attributeExists("student", "groups"));
        verify(groupService).getAllItems();
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/students").flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
        verify(studentService).save(student);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(groupService.getAllItems()).thenReturn(any());
        when(studentService.findById(STUDENT_ID)).thenReturn(student);

        mockMvc.perform(get("/students/" + STUDENT_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(model().attribute("student", student));
        verify(studentService).findById(STUDENT_ID);
        verify(groupService).getAllItems();
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        Student changedStudent = new Student(STUDENT_ID, GROUP_ID_2, FIRST_NAME_2, LAST_NAME_2);
        when(studentService.findById(STUDENT_ID)).thenReturn(student);
        when(groupService.findById(changedStudent.getGroupId())).thenReturn(new Group(changedStudent.getGroupId()));
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        ArgumentCaptor<Group> groupCaptor = ArgumentCaptor.forClass(Group.class);

        mockMvc.perform(patch("/students/" + STUDENT_ID).flashAttr("student", changedStudent))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
        verify(studentService).findById(STUDENT_ID);
        verify(studentService).changeGroup(studentCaptor.capture(), groupCaptor.capture());
        assertEquals(changedStudent.getGroupId(), groupCaptor.getValue().getId());
        verify(studentService).changeFirstName(changedStudent, changedStudent.getFirstName());
        verify(studentService).changeLastName(changedStudent, changedStudent.getLastName());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        mockMvc.perform(delete("/students/" + STUDENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
        verify(studentService).delete(studentCaptor.capture());
        assertEquals(STUDENT_ID, studentCaptor.getValue().getId());
    }

}