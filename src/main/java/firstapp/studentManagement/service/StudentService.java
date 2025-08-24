package firstapp.studentManagement.service;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  //受講生全件検索
  public List<Student> searchStudentList() {
    return repository.searchAllStudents();
  }

  //受講生コース全件検索
  public List<StudentsCourses> searchStudentsCoursesList() {
    return repository.searchAllStudentsCourses();
  }

  //受講生登録処理
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      studentsCourse.setStartDate(LocalDate.now());
      studentsCourse.setCompletionDate(LocalDate.now().plusMonths(8));
      repository.registerStudentsCourses(studentsCourse);
    }
  }

  //受講生情報取得処理
  public StudentDetail searchStudentById(String id) {
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
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
