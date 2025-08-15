package firstapp.studentManagement.controller;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.service.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/studentsCoursesList")
  public List<StudentsCourses> getStudentCourseList() {
    return service.searchStudentsCourseList();
  }

}
