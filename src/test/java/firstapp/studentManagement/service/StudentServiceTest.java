package firstapp.studentManagement.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchAllStudent()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).searchAllStudent();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

  }

  @Test
  void 受講生詳細の検索＿リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId("1");
    List<StudentCourse> studentCourse = new ArrayList<>();
    when(repository.searchStudent(student.getId())).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourse);

    sut.searchStudentById(student.getId());

    verify(repository, times(1)).searchStudent(student.getId());
    verify(repository, times(1)).searchStudentCourse(student.getId());
  }

  @Test
  void 受講生詳細の登録＿リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId("1");
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse();
    studentCourseList.add(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);

    assertEquals(student.getId(), studentCourse.getStudentId());
  }

  @Test
  void 受講生詳細の登録のコース情報の受講生ID_コース開始日_コース終了日の初期設定() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    student.setId("1");

    sut.initStudentCourse(studentCourse, student);

    assertEquals(student.getId(), studentCourse.getStudentId());
    assertEquals(LocalDate.now().plusMonths(1).getMonth(),
        studentCourse.getStartDate().getMonth());
    assertEquals(LocalDate.now().plusMonths(8).getMonth(),
        studentCourse.getCompletionDate().getMonth());

  }

  @Test
  void 受講生詳細の更新＿リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse();
    studentCourseList.add(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

}