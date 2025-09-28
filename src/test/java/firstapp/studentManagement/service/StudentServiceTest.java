package firstapp.studentManagement.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import firstapp.studentManagement.controller.converter.StudentConverter;
import firstapp.studentManagement.controller.converter.StudentCourseConverter;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.repository.StudentRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @Mock
  private StudentCourseConverter courseConverter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter, courseConverter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
    List<StudentCourse> convertStudentCourseList = new ArrayList<>();
    when(repository.searchAllStudent()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(repository.searchStudentCourseStatusList()).thenReturn(studentCourseStatusList);
    when(courseConverter.convertStudentCourseList(studentCourseList,
        studentCourseStatusList)).thenReturn(convertStudentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).searchAllStudent();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchStudentCourseStatusList();
    verify(courseConverter, times(1)).convertStudentCourseList(studentCourseList,
        studentCourseStatusList);
    verify(converter, times(1)).convertStudentDetails(studentList, convertStudentCourseList);

  }

  @Test
  void 受講生詳細の検索＿リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId("1");
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>(List.of(studentCourse));
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    when(repository.searchStudent(student.getId())).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourseList);
    when(repository.searchStudentCourseStatus(studentCourseList.get(0).getId())).thenReturn(
        studentCourseStatus);

    sut.searchStudentById(student.getId());

    verify(repository, times(1)).searchStudent(student.getId());
    verify(repository, times(1)).searchStudentCourse(student.getId());
    verify(repository, times(1)).searchStudentCourseStatus(studentCourseList.get(0).getId());
  }

  @Test
  void 受講生詳細の登録＿リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setId("1");
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("2");
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
    verify(repository, times(1)).registerStudentCourseStatus(studentCourse.getCourseStatus());

    assertThat(studentCourse.getStudentId()).isEqualTo("1");
    assertThat(studentCourse.getCourseStatus().getStudentCourseId()).isEqualTo("2");
  }

  @Test
  void 受講生詳細の登録のコース情報の受講生ID_コース開始日_コース終了日の初期設定() {
    StudentCourse studentCourse = new StudentCourse();
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    String studentId = "1";

    sut.initStudentCourse(studentCourse, studentCourseStatus, studentId);

    assertThat(studentCourse.getStudentId()).isEqualTo("1");
    assertThat(studentCourse.getStartDate().getMonth()).isEqualTo(
        LocalDate.now().plusMonths(1).getMonth());
    assertThat(studentCourse.getCompletionDate().getMonth()).isEqualTo(
        LocalDate.now().plusMonths(8).getMonth());
    assertThat(studentCourseStatus.getStatus()).isEqualTo(Status.仮申込);

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

  @Test
  void 受講生コース情報の申込状況の更新＿リポジトリの処理が適切に呼び出せていること() {
    StudentCourse studentCourse = new StudentCourse();
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setId("1");
    studentCourseStatus.setStudentCourseId("1");
    studentCourseStatus.setStatus(Status.仮申込);
    StudentCourseStatus dbStudentCourseStatus = new StudentCourseStatus();
    dbStudentCourseStatus.setId("1");
    when(repository.searchStudentCourseStatus(studentCourseStatus.getStudentCourseId())).thenReturn(
        dbStudentCourseStatus);
    when(repository.searchStudentCourseOnly(studentCourseStatus.getStudentCourseId())).thenReturn(
        studentCourse);

    sut.updateStudentCourseStatus(studentCourseStatus);

    verify(repository, times(1)).searchStudentCourseStatus(
        studentCourseStatus.getStudentCourseId());
    verify(repository, times(1)).searchStudentCourseOnly(studentCourseStatus.getStudentCourseId());
    verify(repository, times(1)).updateStudentCourseStatus(studentCourseStatus);
  }

  @Test
  void 受講生コース情報の申込状況の更新処理中の入力したIDと更新前のIDを比較処理の際に例外処理にかかっていること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setId("1");
    studentCourseStatus.setStudentCourseId("1");
    studentCourseStatus.setStatus(Status.仮申込);
    StudentCourseStatus dbStudentCourseStatus = new StudentCourseStatus();
    dbStudentCourseStatus.setId("2");
    when(repository.searchStudentCourseStatus(studentCourseStatus.getStudentCourseId())).thenReturn(
        dbStudentCourseStatus);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> sut.updateStudentCourseStatus(studentCourseStatus));

    assertThat(ex.getMessage()).isEqualTo("IDが間違っています!");
  }

  @Test
  void 申込状況が受講中の場合の確認処理が適切に行われていること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    StudentCourse studentCourse = new StudentCourse();
    Status status = Status.受講中;
    LocalDate setNow = LocalDate.of(2025, 5, 1);
    LocalDate startDate = LocalDate.of(2025, 4, 1);
    LocalDate completionDate = LocalDate.of(2025, 12, 1);
    studentCourseStatus.setStatus(status);
    studentCourse.setStartDate(startDate);
    studentCourse.setCompletionDate(completionDate);
    try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
      mocked.when(LocalDate::now).thenReturn(setNow);

      sut.checkStatus(studentCourseStatus, studentCourse);

      assertThat(setNow).isEqualTo(LocalDate.now());
    }
  }

  @Test
  void 申込状況が受講終了の場合の確認処理が適切に行われていること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    StudentCourse studentCourse = new StudentCourse();
    Status status = Status.受講終了;
    LocalDate setNow = LocalDate.of(2025, 12, 1);
    LocalDate startDate = LocalDate.of(2025, 4, 1);
    LocalDate completionDate = LocalDate.of(2025, 12, 1);
    studentCourseStatus.setStatus(status);
    studentCourse.setStartDate(startDate);
    studentCourse.setCompletionDate(completionDate);
    try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
      mocked.when(LocalDate::now).thenReturn(setNow);

      sut.checkStatus(studentCourseStatus, studentCourse);

      assertThat(setNow).isEqualTo(LocalDate.now());
    }
  }

  @Test
  void 申込状況が受講中の場合の例外処理時の警告が出力されていること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    StudentCourse studentCourse = new StudentCourse();
    Status status = Status.受講中;
    LocalDate setNow = LocalDate.of(2025, 3, 31);
    LocalDate startDate = LocalDate.of(2025, 4, 1);
    LocalDate completionDate = LocalDate.of(2025, 12, 1);
    studentCourseStatus.setStatus(status);
    studentCourse.setStartDate(startDate);
    studentCourse.setCompletionDate(completionDate);
    try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
      mocked.when(LocalDate::now).thenReturn(setNow);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> sut.checkStatus(studentCourseStatus, studentCourse));
      assertThat(ex.getMessage()).isEqualTo("現在は受講期間内の日付ではありません!");
    }
  }

  @Test
  void 申込状況が受講終了の場合の例外処理時の警告が出力されていること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    StudentCourse studentCourse = new StudentCourse();
    Status status = Status.受講終了;
    LocalDate setNow = LocalDate.of(2025, 11, 30);
    LocalDate startDate = LocalDate.of(2025, 4, 1);
    LocalDate completionDate = LocalDate.of(2025, 12, 1);
    studentCourseStatus.setStatus(status);
    studentCourse.setStartDate(startDate);
    studentCourse.setCompletionDate(completionDate);
    try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
      mocked.when(LocalDate::now).thenReturn(setNow);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> sut.checkStatus(studentCourseStatus, studentCourse));

      assertThat(ex.getMessage()).isEqualTo("まだ受講終了予定日前の日付です!");
    }
  }
}