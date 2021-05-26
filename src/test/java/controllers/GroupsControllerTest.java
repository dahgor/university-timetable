package controllers;

import dao.entities.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.GroupService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {
    private static final int GROUP_ID = 2;
    private static final String GROUP_NAME = "ME-15";
    private static final String GROUP_NAME_2 = "ME-16";

    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;

    private List<Group> groups;
    private Group group = new Group(GROUP_ID, GROUP_NAME);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GroupsController(groupService)).build();
    }

    @BeforeEach
    void prepareGroups() {
        groups = new LinkedList<>();
        groups.add(new Group(1, "QW-11"));
    }

    @Test
    void shouldReturnShowAllPageAndItems() throws Exception {
        when(groupService.getAllItems()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/show-all"))
                .andExpect(model().attribute("groups", groups));
        verify(groupService).getAllItems();
    }

    @Test
    void shouldReturnShowByIdPageAndItem() throws Exception {
        when(groupService.findById(GROUP_ID)).thenReturn(group);

        mockMvc.perform(get("/groups/" + GROUP_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/show-by-id"))
                .andExpect(model().attribute("group", group));
        verify(groupService).findById(GROUP_ID);
    }

    @Test
    void shouldReturnAddNewPageAndEmptyItem() throws Exception {
        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/add-new"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void shouldCreateItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(post("/groups").flashAttr("group", group))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));
        verify(groupService).save(group);
    }

    @Test
    void shouldReturnEditPageAndItem() throws Exception {
        when(groupService.findById(GROUP_ID)).thenReturn(group);

        mockMvc.perform(get("/groups/" + GROUP_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/edit"))
                .andExpect(model().attribute("group", group));
        verify(groupService).findById(GROUP_ID);
    }

    @Test
    void shouldUpdateItemAndRedirectToShowByIdPage() throws Exception {
        when(groupService.findById(GROUP_ID)).thenReturn(group);
        Group changedGroup = new Group(GROUP_ID, GROUP_NAME_2);

        mockMvc.perform(patch("/groups/" + GROUP_ID).flashAttr("group", changedGroup))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));
        verify(groupService).findById(GROUP_ID);
        verify(groupService).changeName(changedGroup, changedGroup.getName());
    }

    @Test
    void shouldDeleteItemAndRedirectToShowAllPage() throws Exception {
        mockMvc.perform(delete("/groups/" + GROUP_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));
        verify(groupService).delete(any());
    }

}