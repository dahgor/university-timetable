package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
import services.interfaces.CourseService;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private CourseService courseService;

    @Autowired
    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("courses", courseService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "courses/showAll";
    }
}
