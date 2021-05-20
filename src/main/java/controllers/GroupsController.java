package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "groups/showAll";
    }
}
