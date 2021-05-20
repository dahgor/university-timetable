package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
import services.interfaces.TimeService;

@Controller
@RequestMapping("/times")
public class TimesController {

    private TimeService timeService;

    @Autowired
    public TimesController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("times", timeService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "times/showAll";
    }
}
