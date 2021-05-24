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

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("auditory", auditoryService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "auditories/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("auditory") Auditory auditory) {
        try {
            Auditory oldAuditory = auditoryService.findById(id);
            if (!auditory.getLocation().equals(oldAuditory.getLocation())) {
                auditoryService.changeLocation(auditory, auditory.getLocation());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/auditories";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            auditoryService.delete(new Auditory(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/auditories";
    }
}
