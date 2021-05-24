package controllers;

import dao.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.GroupService;
import services.interfaces.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private StudentService studentService;
    private GroupService groupService;

    @Autowired
    public StudentsController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
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

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("student", studentService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "students/showById";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        try {
            model.addAttribute("groups", groupService.getAllItems());
            model.addAttribute("student", new Student());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "students/addNew";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") Student student) {
        try {
            studentService.save(student);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("student", studentService.findById(id));
            model.addAttribute("groups", groupService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("student") Student student) {
        try {
            Student oldStudent = studentService.findById(id);
            if (!student.getFirstName().equals(oldStudent.getFirstName())) {
                studentService.changeFirstName(student, student.getFirstName());
            }
            if (!student.getLastName().equals(oldStudent.getLastName())) {
                studentService.changeLastName(student, student.getLastName());
            }
            if (student.getGroupId() != oldStudent.getGroupId()) {
                studentService.changeGroup(student, groupService.findById(student.getGroupId()));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            studentService.delete(new Student(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/students";
    }
}
