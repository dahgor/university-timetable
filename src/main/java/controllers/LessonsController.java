package controllers;

import dao.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ServiceException;
import services.interfaces.*;

@Controller
@RequestMapping("/lessons")
public class LessonsController {

    private LessonService lessonService;
    private CourseService courseService;
    private ProfessorService professorService;
    private GroupService groupService;
    private AuditoryService auditoryService;

    @Autowired
    public LessonsController(LessonService lessonService, CourseService courseService,
                             ProfessorService professorService, GroupService groupService,
                             AuditoryService auditoryService) {
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.professorService = professorService;
        this.groupService = groupService;
        this.auditoryService = auditoryService;
    }

    @GetMapping()
    public String showAll(Model model) {
        try {
            model.addAttribute("lessons", lessonService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "lessons/show-all";
    }

    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("lesson", lessonService.findById(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "lessons/show-by-id";
    }

    @GetMapping("/new")
    public String addNew(Model model) {
        try {
            model.addAttribute("courses", courseService.getAllItems());
            model.addAttribute("professors", professorService.getAllItems());
            model.addAttribute("groups", groupService.getAllItems());
            model.addAttribute("auditories", auditoryService.getAllItems());
            model.addAttribute("lesson", new Lesson());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "lessons/add-new";
    }

    @PostMapping()
    public String create(@ModelAttribute("lesson") Lesson lesson) {
        try {
            lessonService.save(lesson);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("lesson", lessonService.findById(id));
            model.addAttribute("courses", courseService.getAllItems());
            model.addAttribute("professors", professorService.getAllItems());
            model.addAttribute("groups", groupService.getAllItems());
            model.addAttribute("auditories", auditoryService.getAllItems());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "lessons/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("lesson") Lesson lesson) {
        try {
            Lesson oldLesson = lessonService.findById(id);
            if (lesson.getAuditoryId() != oldLesson.getAuditoryId()) {
                lessonService.changeAuditory(lesson, new Auditory(lesson.getAuditoryId()));
            }
            if (lesson.getCourseId() != oldLesson.getCourseId()) {
                lessonService.changeCourse(lesson, new Course(lesson.getCourseId()));
            }
            if (lesson.getGroupId() != oldLesson.getGroupId()) {
                lessonService.changeGroup(lesson, new Group(lesson.getGroupId()));
            }
            if (lesson.getProfessorId() != oldLesson.getProfessorId()) {
                lessonService.changeProfessor(lesson, new Professor(lesson.getProfessorId()));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/lessons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            lessonService.delete(new Lesson(id));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/lessons";
    }
}
