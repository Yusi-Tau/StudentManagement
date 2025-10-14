package firstapp.studentManagement.controller;

import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.search.StudentSearchWords;
import firstapp.studentManagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の一覧検索です。全件検索、または指定された条件に従った条件検索を行います。
   *
   * @param searchWords 検索条件として使用する検索ワード一覧
   * @return 受講生詳細一覧(全件または条件)
   */
  @Operation(summary = "一覧検索", description = "受講生の全件検索、または条件検索を行います。")
  @PostMapping("/studentSearch")
  public List<StudentDetail> getSelectStudent(
      @RequestBody StudentSearchWords searchWords) {
    return service.searchSelectStudent(searchWords);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生検索", description = "IDに紐づく受講生の情報を取得します。")
  @GetMapping("/student/{id}")
  public StudentDetail nowStudent(@PathVariable @Pattern(regexp = "^[0-9]+$") String id) {
    return service.searchStudentById(id);
  }

  /**
   * 受講生詳細の更新を行います(申込状況を除く)。 キャンセルフラグの更新もここで行います。(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "登録済みの受講生の情報を更新します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail.getStudent().getName() + "さんの更新が成功しました。");
  }

  /**
   *
   * @param studentCourseStatus 受講生コース情報の申込状況
   * @return 実行結果
   */
  @Operation(summary = "コースの申込状況更新", description = "登録済みのコースの申込状況を更新します。")
  @PutMapping("/updateStudentCourseStatus")
  public ResponseEntity<String> updateStudentCourseStatus(
      @RequestBody @Valid StudentCourseStatus studentCourseStatus) {
    service.updateStudentCourseStatus(studentCourseStatus);
    return ResponseEntity.ok(
        "申込状況が" + studentCourseStatus.getStatus() + "に正常に更新されました。");
  }


}
