package firstapp.studentManagement.service;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.Arrays;
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

  public List<Student> searchStudentList() {
    return repository.searchAllStudents();
  }

  public List<StudentsCourses> searchStudentsCoursesList() {
    return repository.searchStudentsCourses();
  }

  @Transactional
  public void register(StudentDetail studentDetail) {
    repository.register(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      studentsCourse.setStartDate(LocalDate.now());
      studentsCourse.setCompletionDate(LocalDate.now().plusMonths(8));
      repository.registerStudentsCourses(studentsCourse);
    }

  }

  public StudentDetail searchStudentById(int id) {
    Student student = repository.searchById(id);
    StudentsCourses courses = repository.searchByStudentId(id);
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentsCourses(Arrays.asList(courses));
    return detail;
  }

  @Transactional
  public void update(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setCourseName(studentDetail.getStudentsCourses().getFirst().getCourseName());
      repository.updateStudentsCourses(studentsCourse);
    }
  }
}
