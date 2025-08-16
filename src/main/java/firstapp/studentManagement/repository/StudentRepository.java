package firstapp.studentManagement.repository;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert("INSERT INTO students"
      + "(name, furigana, nickname, address, live, age, gender, remark, is_deleted) "
      + "VALUES(#{name}, #{furigana}, #{nickname}, #{address}, #{live}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void register(Student regiStudent);

  @Insert("INSERT INTO students_courses(student_id, course_name, start_date, completion_date) "
      + "VALUES(#{studentId}, #{courseName}, #{startDate}, #{completionDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentsCourses studentsCourses);
}
