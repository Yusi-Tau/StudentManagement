package firstapp.studentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentCourseConverterTest {

  private StudentCourseConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentCourseConverter();
  }

  @Test
  void 受講生コース情報のIDと受講生コース情報の申込情報の受講生コースIDが一致している情報のみが結合していること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse.setCompletionDate(LocalDate.of(2025, 12, 1));
    List<StudentCourse> studentCourseList = new ArrayList<>(List.of(studentCourse));
    StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus();
    studentCourseStatus1.setId("1");
    studentCourseStatus1.setStudentCourseId("1");
    studentCourseStatus1.setStatus(Status.本申込);
    StudentCourseStatus studentCourseStatus2 = new StudentCourseStatus();
    studentCourseStatus2.setId("2");
    studentCourseStatus2.setStudentCourseId("2");
    studentCourseStatus2.setStatus(Status.受講中);
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>(
        List.of(studentCourseStatus1, studentCourseStatus2));

    List<StudentCourse> actual = sut.convertStudentCourseList(studentCourseList,
        studentCourseStatusList);

    StudentCourseStatus resultStudentCourseStatus = actual.get(0).getCourseStatus();
    assertThat(resultStudentCourseStatus.getStatus()).isEqualTo(Status.本申込);

  }

}