package firstapp.studentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.domain.StudentDetail;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生のIDと受講生コース情報の受講生IDが一致している情報のみが結合していること() {
    Student student = new Student();
    student.setId("1");
    student.setName("中野");
    student.setFurigana("ナカノ");
    student.setNickname("ナカヤン");
    student.setAddress("aaaa@example.com");
    student.setLive("北海道");
    student.setRemark("");
    List<Student> studentList = new ArrayList<>(List.of(student));
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId("1");
    studentCourse1.setStudentId("1");
    studentCourse1.setCourseName("Javaコース");
    studentCourse1.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse1.setCompletionDate(LocalDate.of(2025, 12, 31));
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId("2");
    studentCourse2.setStudentId("1");
    studentCourse2.setCourseName("AWSコース");
    studentCourse2.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse2.setCompletionDate(LocalDate.of(2025, 12, 31));
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setId("3");
    studentCourse3.setStudentId("2");
    studentCourse3.setCourseName("デザインコース");
    studentCourse3.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse3.setCompletionDate(LocalDate.of(2025, 12, 31));
    List<StudentCourse> studentCourseList = new ArrayList<>(
        List.of(studentCourse1, studentCourse2, studentCourse3));

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    List<StudentCourse> resultStudentCourse = actual.get(0).getStudentCourseList();
    assertThat(resultStudentCourse.size()).isEqualTo(2);

  }

}