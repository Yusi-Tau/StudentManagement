package firstapp.studentManagement.domain;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

}
