package firstapp.studentManagement.repository;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert("INSERT INTO students"
      + "(id, name, furigana, nickname, address, live, age, gender, remark) "
      + "VALUES(#{id}, #{name}, #{furigana}, #{nickname}, #{address}, #{live}, #{age}, #{gender}, #{remark})")
  void create(Student regiStudent);


}
