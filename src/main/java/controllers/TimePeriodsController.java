package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ServiceException;
import services.interfaces.TimePeriodService;

@Controller
@RequestMapping("/timePeriods")
public class TimePeriodsController {

    private TimePeriodService timePeriodService;

    @Autowired
    public TimePeriodsController(TimePeriodService timePeriodService) {
        this.timePeriodService = timePeriodService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("timePeriods", timePeriodService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "timePeriods/showAll";
    }
}
