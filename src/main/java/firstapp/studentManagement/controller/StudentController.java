package firstapp.studentManagement.controller;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.service.StudentService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  //受講生全件検索
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();
    return converter.convertStudentDetails(students, studentsCourses);
  }

  //受講生コース全件検索
  @GetMapping("/studentsCoursesList")
  public String getStudentsCoursesList(Model model) {
    model.addAttribute("studentsCoursesList", service.searchStudentsCoursesList());
    return "studentsCoursesList";
  }

  //受講生登録処理
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);
    return ResponseEntity.ok(studentDetail.getStudent().getName() + "さんの登録が完了しました。");
  }

  //受講生情報取得処理
  @GetMapping("/student/{id}")
  public String nowStudent(@PathVariable("id") String id, Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail = service.searchStudentById(id);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  //受講生更新処理
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail.getStudent().getName() + "さんの更新が成功しました。");
  }


}
