package controllers;

import dao.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        return "courses/show-all";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("course", courseService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "courses/show-by-id";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        model.addAttribute("course", new Course());
        return "courses/add-new";
    }

    @PostMapping()
    public String create(@ModelAttribute("course") Course course) {
        try {
            courseService.save(course);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("course", courseService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "courses/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("course") Course course) {
        try {
            Course oldCourse = courseService.findById(id);
            if (!course.getName().equals(oldCourse.getName())) {
                courseService.changeName(course, course.getName());
            }
            if (!course.getDescription().equals(oldCourse.getDescription())) {
                courseService.changeDescription(course, course.getDescription());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            courseService.delete(new Course(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/courses";
    }

}
