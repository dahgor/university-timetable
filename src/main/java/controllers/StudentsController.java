package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
import services.interfaces.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private StudentService studentService;

    @Autowired
    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("students", studentService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "students/showAll";
    }
}
