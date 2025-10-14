package firstapp.studentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.search.StudentSearchWords;
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
  void 条件指定なしの場合に受講生詳細の全件取得が行えること() {
    StudentSearchWords searchWords = new StudentSearchWords();

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生IDを条件指定にした場合にそれに該当する受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setId(1);

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    Student resultStudent = actual.getLast().getStudent();
    assertThat(resultStudent.getName()).isEqualTo("佐藤太郎");
  }

  @Test
  void 名前を条件指定にした場合に部分一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setName("藤");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void フリガナを条件指定にした場合に前方一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setFurigana("イ");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void ニックネームを条件指定にした場合に前方一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setNickname("ピ");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 住所を条件指定にした場合に前方一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setLive("愛知");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 一つめの年齢のみを条件指定にした場合にその年齢以上の受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setAgeFrom(21);

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 二つめの年齢のみを条件指定にした場合にその年齢未満の受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setAgeTo(30);

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 両方の年齢を条件指定にした場合に一つめの年齢以上＿二つめの年齢未満の受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setAgeFrom(20);
    searchWords.setAgeTo(30);

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 性別を条件指定にした場合にその性別の受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setGender("その他");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 論理削除を条件指定にした場合にそれに該当する受講生詳細が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setIsDeleted(true);

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void コース名を条件指定にした場合に部分一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setCourseName("マーケ");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 申込状況を条件指定にした場合に部分一致検索ができていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setCourseStatus("終了");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 複数の条件指定を行った場合に該当する受講生が検索できていること() {
    StudentSearchWords searchWords = new StudentSearchWords();
    searchWords.setName("木");
    searchWords.setFurigana("サ");
    searchWords.setNickname("サ");
    searchWords.setLive("愛知");
    searchWords.setAgeFrom(20);
    searchWords.setAgeTo(31);
    searchWords.setGender("女");
    searchWords.setIsDeleted(false);
    searchWords.setCourseName("AWS");
    searchWords.setCourseStatus("本");

    List<StudentDetail> actual = sut.searchSelectStudentDetail(searchWords);
    assertThat(actual.size()).isEqualTo(1);
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
    StudentCourseStatus resultStudentCourseStatus = actual.get(0).getCourseStatus();
    assertThat(actual.get(0).getCourseName()).isEqualTo("Javaコース");
    assertThat(resultStudentCourseStatus.getStatus()).isEqualTo(Status.仮申込);
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