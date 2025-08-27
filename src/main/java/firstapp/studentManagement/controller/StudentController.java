package firstapp.studentManagement.controller;

import firstapp.studentManagement.data.StudentCourse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@RestController

public class StudentController {

  private StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;

  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  //受講生コース全件検索
  @GetMapping("/studentsCoursesList")
  public String getStudentsCoursesList(Model model) {
    model.addAttribute("studentsCoursesList", service.searchStudentCourseList());
    return "studentsCoursesList";
  }

  //受講生登録処理
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourseList(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail nowStudent(@PathVariable String id) {
    return service.searchStudentById(id);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います。(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail.getStudent().getName() + "さんの更新が成功しました。");
  }


}
