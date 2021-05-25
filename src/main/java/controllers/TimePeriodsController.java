package controllers;

import dao.entities.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.TimePeriodService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
@RequestMapping("/timePeriods")
public class TimePeriodsController {
    private static final String INPUT_FORMAT = "hh:mm";

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

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("timePeriod", timePeriodService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "timePeriods/showById";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        return "timePeriods/addNew";
    }

    @PostMapping()
    public String create(@RequestParam(value = "start", required = true) String start,
                         @RequestParam(value = "end", required = true) String end) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_FORMAT);
            Date parsedStart = inputFormat.parse(start);
            Date parsedEnd = inputFormat.parse(end);
            Timestamp startHour = new Timestamp(parsedStart.getTime());
            Timestamp endHour = new Timestamp(parsedEnd.getTime());
            timePeriodService.save(new TimePeriod(startHour, endHour));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/timePeriods";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            TimePeriod timePeriod = timePeriodService.findById(id);
            model.addAttribute("timePeriod", timePeriod);
            model.addAttribute("start", timePeriod.getStartHour().toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("hh:mm")));
            model.addAttribute("end", timePeriod.getEndHour().toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("hh:mm")));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "timePeriods/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @RequestParam(value = "start", required = true) String start,
                         @RequestParam(value = "end", required = true) String end) {
        try {
            TimePeriod oldTimePeriod = timePeriodService.findById(id);
            SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_FORMAT);
            Date parsedStart = inputFormat.parse(start);
            Date parsedEnd = inputFormat.parse(end);
            Timestamp startHour = new Timestamp(parsedStart.getTime());
            Timestamp endHour = new Timestamp(parsedEnd.getTime());
            if (!oldTimePeriod.getStartHour().equals(startHour)) {
                timePeriodService.changeStartHour(new TimePeriod(id), startHour);
            }
            if (!oldTimePeriod.getEndHour().equals(endHour)) {
                timePeriodService.changeEndHour(new TimePeriod(id), endHour);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/timePeriods";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            timePeriodService.delete(new TimePeriod(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/timePeriods";
    }
}
