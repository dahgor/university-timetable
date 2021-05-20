package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
