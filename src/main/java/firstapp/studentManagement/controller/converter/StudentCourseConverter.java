package firstapp.studentManagement.controller.converter;

import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.data.StudentCourseStatus;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 受講生コース情報をコース名、受講開始日、終了日、申込状況、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentCourseConverter {

  /**
   * 受講生コース情報に紐づく申込状況をマッピングする。
   *
   * @param studentCourseList       受講生コース情報のリスト
   * @param studentCourseStatusList 受講生コース情報の申込状況のリスト
   * @return 受講生コース情報のリスト
   */
  public List<StudentCourse> convertStudentCourseList(List<StudentCourse> studentCourseList,
      List<StudentCourseStatus> studentCourseStatusList) {
    studentCourseList.forEach(studentCourse -> {
      studentCourseStatusList.stream().filter(studentCourseStatus -> studentCourse.getId()
              .equals(studentCourseStatus.getStudentCourseId()))
          .forEachOrdered(studentCourse::setCourseStatus);
    });
    return studentCourseList;
  }

}
