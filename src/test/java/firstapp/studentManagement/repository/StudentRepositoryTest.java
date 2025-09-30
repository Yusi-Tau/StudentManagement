package firstapp.studentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.searchAllStudent();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生のコース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生のコース情報の申込状況の全件検索が行えること() {
    List<StudentCourseStatus> actual = sut.searchStudentCourseStatusList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生IDを使った受講生検索が行えること() {
    String id = "1";

    Student actual = sut.searchStudent(id);
    assertThat(actual.getName()).isEqualTo("佐藤太郎");
  }

  @Test
  void 受講生IDを使った受講生コース情報の検索が行えること() {
    String id = "1";

    List<StudentCourse> actual = sut.searchStudentCourse(id);
    assertThat(actual.get(0).getCourseName()).isEqualTo("Javaコース");
  }

  @Test
  void 受講生コースIDを使った受講生コース情報の申込状況の検索が行えること() {
    String id = "1";

    StudentCourseStatus actual = sut.searchStudentCourseStatus(id);
    assertThat(actual.getStatus()).isEqualTo(Status.仮申込);
  }


  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("中野");
    student.setFurigana("ナカノ");
    student.setNickname("ナカヤン");
    student.setAddress("aaaa@example.com");
    student.setLive("北海道");
    student.setAge(20);
    student.setGender("その他");
    student.setRemark("");
    sut.registerStudent(student);

    List<Student> actual = sut.searchAllStudent();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("フロントエンドコース");
    studentCourse.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse.setCompletionDate(LocalDate.of(2025, 12, 31));
    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の申込情報の登録が行えること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStudentCourseId("6");
    studentCourseStatus.setStatus(Status.仮申込);
    sut.registerStudentCourseStatus(studentCourseStatus);

    List<StudentCourseStatus> actual = sut.searchStudentCourseStatusList();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生の更新が行えること() {
    String id = "1";
    Student student = new Student();
    student.setId(id);
    student.setName("佐藤太郎丸");
    student.setFurigana("サトウタロウマル");
    student.setNickname("ナカヤン");
    student.setAddress("abcd@example.com");
    student.setLive("宮城県仙台市");
    student.setAge(44);
    student.setGender("男");
    student.setRemark("");
    sut.updateStudent(student);

    Student actual = sut.searchStudent(id);
    assertThat(actual.getName()).isEqualTo("佐藤太郎丸");
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    String id = "1";
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId(id);
    studentCourse.setCourseName("AWSコース");
    studentCourse.setStartDate(LocalDate.of(2025, 4, 1));
    studentCourse.setCompletionDate(LocalDate.of(2025, 12, 31));
    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse(id);
    assertThat(actual.get(0).getCourseName()).isEqualTo("AWSコース");
  }

  @Test
  void 受講生コースIDに対応する受講生コース情報の検索が行えること() {
    String studentCourseId = "1";

    StudentCourse actual = sut.searchStudentCourseOnly(studentCourseId);
    assertThat(actual.getCourseName()).isEqualTo("Javaコース");
  }

  @Test
  void 受講生コース情報の申込状況の更新が行えること() {
    String id = "1";
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setId("1");
    studentCourseStatus.setStudentCourseId(id);
    studentCourseStatus.setStatus(Status.受講終了);
    sut.updateStudentCourseStatus(studentCourseStatus);

    StudentCourseStatus actual = sut.searchStudentCourseStatus(id);
    assertThat(actual.getStatus()).isEqualTo(Status.受講終了);
  }
}