package controllers;

import dao.entities.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.ProfessorService;

@Controller
@RequestMapping("/professors")
public class ProfessorsController {

    private ProfessorService professorService;

    @Autowired
    public ProfessorsController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("professors", professorService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "professors/showAll";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("professor", professorService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "professors/showById";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("professor", new Professor());
        return "professors/addNew";
    }

    @PostMapping()
    public String create(@ModelAttribute("professor") Professor professor) {
        try {
            professorService.save(professor);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/professors";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("professor", professorService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "professors/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("professor") Professor professor) {
        try {
            Professor oldProfessor = professorService.findById(id);
            if (!professor.getFirstName().equals(oldProfessor.getFirstName())) {
                professorService.changeFirstName(professor, professor.getFirstName());
            }
            if (!professor.getLastName().equals(oldProfessor.getLastName())) {
                professorService.changeLastName(professor, professor.getLastName());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/professors";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            professorService.delete(new Professor(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/professors";
    }
}
