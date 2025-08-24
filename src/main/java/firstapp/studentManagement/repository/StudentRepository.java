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

  //受講生全件検索
  @Select("SELECT * FROM students ")
  List<Student> searchAllStudents();

  //受講生コース全件検索
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchAllStudentsCourses();

  //受講生情報取得処理
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  //受講生情報取得処理(コース)
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentCourses(String studentId);

  //受講生登録処理
  @Insert("INSERT INTO students"
      + "(name, furigana, nickname, address, live, age, gender, remark, is_deleted) "
      + "VALUES(#{name}, #{furigana}, #{nickname}, #{address}, #{live}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  //受講生登録処理(コース)
  @Insert("INSERT INTO students_courses(student_id, course_name, start_date, completion_date) "
      + "VALUES(#{studentId}, #{courseName}, #{startDate}, #{completionDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentsCourses studentsCourses);

  //受講生更新処理
  @Update("UPDATE students "
      + "SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, address = #{address}, live = #{live}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} "
      + "WHERE id = #{id}")
  void updateStudent(Student student);

  //受講生更新処理(コース)
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentsCourses(StudentsCourses studentsCourses);

}
