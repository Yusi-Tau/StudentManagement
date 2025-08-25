package firstapp.studentManagement.service;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.time.LocalDate;
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

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講性一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchAllStudents();
    List<StudentsCourses> studentsCoursesList = repository.searchAllStudentsCourses();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  //受講生コース全件検索
  public List<StudentsCourses> searchStudentsCoursesList() {
    return repository.searchAllStudentsCourses();
  }

  //受講生登録処理
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      studentsCourse.setStartDate(LocalDate.now().plusMonths(1));
      studentsCourse.setCompletionDate(LocalDate.now().plusMonths(8));
      repository.registerStudentsCourses(studentsCourse);
    }
    return studentDetail;
  }

  /**
   * 受講生検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 　受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudentById(String id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourses(student.getId());
    return new StudentDetail(student, studentsCourses);
  }

  //受講生更新処理
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      repository.updateStudentsCourses(studentsCourse);
    }
  }

}
