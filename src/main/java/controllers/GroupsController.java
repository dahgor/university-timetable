package controllers;

import dao.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("groups", groupService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "groups/show-all";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("group", groupService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "groups/show-by-id";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("group", new Group());
        return "groups/add-new";
    }

    @PostMapping()
    public String create(@ModelAttribute("group") Group group) {
        try {
            groupService.save(group);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("group", groupService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("group") Group group) {
        try {
            Group oldGroup = groupService.findById(id);
            if (!group.getName().equals(oldGroup.getName())) {
                groupService.changeName(group, group.getName());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            groupService.delete(new Group(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/groups";
    }
}
