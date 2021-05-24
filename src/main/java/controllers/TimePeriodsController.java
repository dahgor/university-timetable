package controllers;

import dao.entities.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.TimePeriodService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/timePeriods")
public class TimePeriodsController {
    private static final String SECONDS_SUFFIX = ":00";
    private static final int EPOCH_YEAR = 1970;
    private static final int EPOCH_MONTH = 1;
    private static final int EPOCH_DAY = 1;

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
        model.addAttribute("timePeriod", new TimePeriod());
        model.addAttribute("startHour", new Time(1));
        model.addAttribute("endHour", new Time(1));
        return "timePeriods/addNew";
    }

    @PostMapping()
    public String create(@RequestParam(value = "start", required = true) String startHour,
                         @RequestParam(value = "end", required = true) String endHour) {
        try {
            Time startTime = Time.valueOf(startHour + SECONDS_SUFFIX);
            Time endTime = Time.valueOf(endHour + SECONDS_SUFFIX);
            Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY), startTime.toLocalTime()));
            Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY), endTime.toLocalTime()));
            TimePeriod timePeriod = new TimePeriod(start, end);
            timePeriodService.save(timePeriod);
        } catch (ServiceException e) {
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
                         @RequestParam(value = "start", required = true) String startHour,
                         @RequestParam(value = "end", required = true) String endHour) {
        try {
            TimePeriod oldTimePeriod = timePeriodService.findById(id);
            Time startTime = Time.valueOf(startHour + SECONDS_SUFFIX);
            Time endTime = Time.valueOf(endHour + SECONDS_SUFFIX);
            Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY), startTime.toLocalTime()));
            Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(EPOCH_YEAR, EPOCH_MONTH, EPOCH_DAY), endTime.toLocalTime()));
            if (!oldTimePeriod.getStartHour().equals(start)) {
                timePeriodService.changeStartHour(new TimePeriod(id), start);
            }
            if (!oldTimePeriod.getEndHour().equals(end)) {
                timePeriodService.changeEndHour(new TimePeriod(id), end);
            }
        } catch (ServiceException e) {
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
