package firstapp.studentManagement.controller;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.service.StudentService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/studentsCoursesList")
  public String getStudentsCoursesList(Model model) {
    model.addAttribute("studentsCoursesList", service.searchStudentsCoursesList());
    return "studentsCoursesList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.register(studentDetail);
    return "redirect:/studentList";
  }

  @GetMapping("/student/{id}")
  public String nowStudent(@PathVariable("id") String id, Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail = service.searchStudentById(id);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@Validated @ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.update(studentDetail);
    return "redirect:/studentList";
  }

}
