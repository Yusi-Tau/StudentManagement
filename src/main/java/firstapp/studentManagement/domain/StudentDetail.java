package firstapp.studentManagement.domain;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentCourse;

}
