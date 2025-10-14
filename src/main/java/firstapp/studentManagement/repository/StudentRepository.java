package firstapp.studentManagement.repository;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.domain.StudentDetail;
import firstapp.studentManagement.search.StudentSearchWords;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生全件検索を行います。
   *
   * @return 受講性一覧(全件)
   */
  List<Student> searchAllStudent();

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生のコース情報の申込状況の全件検索を行います。
   *
   * @return 受講生のコース情報の申込状況(全件)
   */
  List<StudentCourseStatus> searchStudentCourseStatusList();

  /**
   * 受講生詳細の全件検索、または条件検索を行います。
   *
   * @param searchWords 検索条件に使用する検索ワード一覧
   * @return 受講生詳細一覧(全件または条件)
   */
  List<StudentDetail> searchSelectStudentDetail(StudentSearchWords searchWords);


  /**
   * 受講生の検索を行います。
   *
   * @param id 受講性ID
   * @return 受講性
   */
  Student searchStudent(String id);

  /**
   * 受講生IDに紐づく受講生コース情報(申込状況を含む)を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報(申込状況を含む)
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 受講生コースIDに紐づくコースの申込状況を検索します。
   *
   * @param studentCourseId 受講生コースID
   * @return 受講生コースIDに紐づくコースの申込状況
   */
  StudentCourseStatus searchStudentCourseStatus(String studentCourseId);

  /**
   * 受講生を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コース情報の申込状況を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourseStatus 受講生コース情報の申込状況
   */
  void registerStudentCourseStatus(StudentCourseStatus studentCourseStatus);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コースIDに対応するコース情報を検索します。
   *
   * @param studentCourseId 受講生コースID
   * @return 受講生コースIDに対応するコース情報
   */

  StudentCourse searchStudentCourseOnly(String studentCourseId);

  /**
   * 受講生コース情報に付随する申込状況を更新します。
   *
   * @param studentCourseStatus 受講生コース情報の申込状況
   */

  void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus);
}
