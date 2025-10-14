package firstapp.studentManagement.service;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.controller.converter.StudentCourseConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import firstapp.studentManagement.search.StudentSearchWords;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録、更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;
  private StudentCourseConverter courseConverter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter,
      StudentCourseConverter courseConverter) {
    this.repository = repository;
    this.converter = converter;
    this.courseConverter = courseConverter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講性詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchAllStudent();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<StudentCourseStatus> studentCourseStatusList = repository.searchStudentCourseStatusList();
    List<StudentCourse> convertStudentCourseList = courseConverter.convertStudentCourseList(
        studentCourseList, studentCourseStatusList);
    return converter.convertStudentDetails(studentList, convertStudentCourseList);
  }


  /**
   * 受講生詳細の全件検索を行います。全件検索、または指定された条件に従った条件検索を行います。
   *
   * @param searchWords 検索条件として使用する検索ワード一覧
   * @return 受講生詳細一覧(全件または条件)
   */
  public List<StudentDetail> searchSelectStudent(StudentSearchWords searchWords) {
    Integer ageFrom = searchWords.getAgeFrom();
    Integer ageTo = searchWords.getAgeTo();
    if (ageFrom != null && ageTo != null && ageFrom >= ageTo) {
      throw new IllegalArgumentException("年齢の範囲指定が不正です。");
    }
    List<StudentDetail> studentDetails = repository.searchSelectStudentDetail(searchWords);
    String courseName = searchWords.getCourseName();
    String status = searchWords.getCourseStatus();
    boolean courseNameAllShow = searchWords.isCourseNameAllShow();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    if ((courseName != null && !courseName.isBlank() || status != null && !status.isBlank())
        && courseNameAllShow && !studentDetails.isEmpty()) {
      for (StudentDetail studentDetail : studentDetails) {
        String studentId = studentDetail.getStudent().getId();
        studentCourseList.addAll(repository.searchStudentCourse(studentId));
      }
      studentDetails = converter.convertCourseAllShowStudentDetails(studentDetails,
          studentCourseList);
    }
    return studentDetails;
  }


  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
      initStudentCourse(studentCourse, studentCourseStatus, student.getId());
      repository.registerStudentCourse(studentCourse);
      studentCourseStatus.setStudentCourseId(studentCourse.getId());
      repository.registerStudentCourseStatus(studentCourseStatus);
      studentCourse.setCourseStatus(studentCourseStatus);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報、申込状況を登録する際の初期情報を設定します。
   *
   * @param studentCourse 受講生コース情報
   * @param studentId     受講生ID
   */
  void initStudentCourse(StudentCourse studentCourse, StudentCourseStatus studentCourseStatus,
      String studentId) {
    LocalDate now = LocalDate.now();

    studentCourse.setStudentId(studentId);
    studentCourse.setStartDate(now.plusMonths(1));
    studentCourse.setCompletionDate(now.plusMonths(8));
    studentCourseStatus.setStatus(Status.仮申込);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentById(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の更新を行います。 受講生と受講生コース情報のコース名をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateStudentCourse(studentCourse);
    }
  }

  /**
   * 受講生コース情報の申込状況の更新を行います。 受講生コースIDを使ってコースの開始日、終了日を取得して、更新内容が正しいかを判定します。
   *
   * @param studentCourseStatus 受講生コース情報の申込状況
   */
  @Transactional
  public void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus) {
    StudentCourseStatus dbStudentCourseStatus = repository.searchStudentCourseStatus(
        studentCourseStatus.getStudentCourseId());
    if (!dbStudentCourseStatus.getId().equals(studentCourseStatus.getId())) {
      throw new IllegalArgumentException("IDが間違っています!");
    }
    StudentCourse studentCourse = repository.searchStudentCourseOnly(
        studentCourseStatus.getStudentCourseId());
    checkStatus(studentCourseStatus, studentCourse);
    repository.updateStudentCourseStatus(studentCourseStatus);
  }

  /**
   * statusに入力された内容が受講中、受講終了の場合、現在の日付と受講開始部、終了日を比較して入力が正しいかの確認を行います。
   *
   * @param studentCourseStatus 受講生コース情報の申込状況
   * @param studentCourse       受講生コース情報
   */
  void checkStatus(StudentCourseStatus studentCourseStatus,
      StudentCourse studentCourse) {
    Status status = studentCourseStatus.getStatus();
    LocalDate now = LocalDate.now();
    LocalDate startDate = studentCourse.getStartDate();
    LocalDate completionDate = studentCourse.getCompletionDate();
    switch (status) {
      case 受講中 -> {
        if (now.isBefore(startDate) || now.isAfter(completionDate)) {
          throw new IllegalArgumentException("現在は受講期間内の日付ではありません!");
        }
      }
      case 受講終了 -> {
        if (now.isBefore(completionDate)) {
          throw new IllegalArgumentException("まだ受講終了予定日前の日付です!");
        }
      }
    }
  }

}
