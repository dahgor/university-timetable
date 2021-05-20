package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
import services.interfaces.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonsController {

    private LessonService lessonService;

    @Autowired
    public LessonsController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("lessons", lessonService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "lessons/showAll";
    }
}
