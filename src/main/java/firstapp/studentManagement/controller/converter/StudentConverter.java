package firstapp.studentManagement.controller.converter;

import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourse;
import firstapp.studentManagement.domain.StudentDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するので、ループで回して受講生詳細情報を組み立てる。
   *
   * @param studentList       受講性一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  /**
   * 検索条件にコース名、または申込状況が指定されており、なおかつ対象のコース情報以外も閲覧したい場合に実行されるマッピングです。
   * 受講生コース情報は受講生に対して複数存在するので、ループで回して受講生詳細情報を組み立てる。
   *
   * @param studentDetails    受講生詳細一覧(条件検索)
   * @param studentCourseList 受講生コース情報のリスト
   * @return 対象コース以外を含めた受講生詳細情報のリスト
   */
  public List<StudentDetail> convertCourseAllShowStudentDetails(List<StudentDetail> studentDetails,
      List<StudentCourse> studentCourseList) {
    List<Student> studentList = studentDetails.stream().map(StudentDetail::getStudent)
        .collect(Collectors.toList());

    List<StudentDetail> convertDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      convertDetails.add(studentDetail);
    });
    return convertDetails;
  }

}
