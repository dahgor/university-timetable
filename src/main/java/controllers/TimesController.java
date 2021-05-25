package controllers;

import dao.entities.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.TimePeriodService;
import services.interfaces.TimeService;

@Controller
@RequestMapping("/times")
public class TimesController {

    private TimeService timeService;
    private TimePeriodService timePeriodService;

    @Autowired
    public TimesController(TimeService timeService, TimePeriodService timePeriodService) {
        this.timeService = timeService;
        this.timePeriodService = timePeriodService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("times", timeService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "times/show-all";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("time", timeService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "times/show-by-id";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("time", new Time());
        try {
            model.addAttribute("timePeriods", timePeriodService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "times/add-new";
    }

    @PostMapping()
    public String create(@ModelAttribute("time") Time time) {
        try {
            timeService.save(time);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/times";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id) {
        return "redirect:/times";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            timeService.delete(new Time(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/times";
    }
}
