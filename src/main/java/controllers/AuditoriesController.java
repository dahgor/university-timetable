package controllers;

import dao.entities.Auditory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.AuditoryService;

@Controller
@RequestMapping("/auditories")
public class AuditoriesController {

    private final AuditoryService auditoryService;

    @Autowired
    public AuditoriesController(AuditoryService auditoryService) {
        this.auditoryService = auditoryService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("auditories", auditoryService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "auditories/showAll";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("auditory", auditoryService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "auditories/showById";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("auditory", new Auditory());
        return "auditories/addNew";
    }

    @PostMapping()
    public String create(@ModelAttribute Auditory auditory) {
        try {
            auditoryService.save(auditory);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/auditories";
    }
}
