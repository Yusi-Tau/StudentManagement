package firstapp.studentManagement.service;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  public void create(StudentDetail detail) {
    Student regiStudent = new Student();
    regiStudent.setId(detail.getStudent().getId());
    regiStudent.setName(detail.getStudent().getName());
    regiStudent.setFurigana(detail.getStudent().getFurigana());
    regiStudent.setNickname(detail.getStudent().getNickname());
    regiStudent.setAddress(detail.getStudent().getAddress());
    regiStudent.setLive(detail.getStudent().getLive());
    regiStudent.setAge(detail.getStudent().getAge());
    regiStudent.setGender(detail.getStudent().getGender());
    regiStudent.setRemark(detail.getStudent().getRemark());
    repository.create(regiStudent);

  }
}
