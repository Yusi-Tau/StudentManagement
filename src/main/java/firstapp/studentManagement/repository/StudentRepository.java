package firstapp.studentManagement.repository;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentsCourses;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchById(String id);

  @Select("SELECT * FROM students")
  List<Student> searchAllStudents();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchByStudentId(String studentId);

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

  @Update("UPDATE students "
      + "SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, address = #{address}, live = #{live}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = false "
      + "WHERE id = #{id}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentsCourses(StudentsCourses studentsCourses);
}
