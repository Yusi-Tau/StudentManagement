package firstapp.studentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstapp.studentManagement.data.Student;
import firstapp.studentManagement.data.StudentCourseStatus;
import firstapp.studentManagement.data.StudentCourseStatus.Status;
import firstapp.studentManagement.service.StudentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の一覧検索の条件検索が実行できて空のリストが返ってくること()
      throws Exception {
    mockMvc.perform(post("/studentSearch").contentType(MediaType.APPLICATION_JSON).content(
        """
            {
                  "id": "1",
                  "name": "中",
                  "furigana": "ナ",
                  "nickname": "タ",
                  "live": "東",
                  "ageFrom": "20",
                  "ageTo": "30",
                  "gender": "男",
                  "deleted": "false",
                  "courseName": "Java",
                  "courseStats": "仮",
                  "courseNameAllShow": "true"
            }
            """
    )).andExpect(status().isOk());

    verify(service, times(1)).searchSelectStudent(any());
  }

  @Test
  void 受講生詳細の検索が実行できて空のリストが返ってくること() throws Exception {
    String id = "2";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentById(id);
  }

  @Test
  void 受講生詳細の登録が実行できて登録内容が返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "name": "田村陽介",
                        "furigana": "タムラヨウスケ",
                        "nickname": "タムタム",
                        "address": "efgh@example.com",
                        "live": "埼玉県桶川市",
                        "age": "30",
                        "gender": "男",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "courseName": "Javaコース"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて更新内容が正しいこと() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
        """
            {
                "student": {
                    "id": "2",
                    "name": "佐々木優子",
                    "furigana": "ササキユウコ",
                    "nickname": "ササック",
                    "address": "efgh@example.com",
                    "live": "埼玉県桶川市",
                    "age": "30",
                    "gender": "女",
                    "remark": "",
                    "deleted": "false"
                },
                "studentCourseList": [
                    {
                        "courseName": "デザインコース"
            
                    }
                ]
            }
            """
    )).andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生コース情報の申込状況の更新が実行できて更新内容が正しいこと() throws Exception {
    mockMvc.perform(
        put("/updateStudentCourseStatus").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                "id" : "1",
                "studentCourseId" : "1",
                "status" : "本申込"
                }
                """
        )).andExpect(status().isOk());

    verify(service, times(1)).updateStudentCourseStatus(any());
  }

  @Test
  void 受講生詳細の受講生で入力チェックにかからないこと() {
    Student student = new Student();
    student.setId("11");
    student.setName("中野");
    student.setFurigana("ナカノ");
    student.setNickname("ナカヤン");
    student.setAddress("aaaa@example.com");
    student.setLive("北海道");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いたときに入力チェックにかかること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("中野");
    student.setFurigana("ナカノ");
    student.setNickname("ナカヤン");
    student.setAddress("aaaa@example.com");
    student.setLive("北海道");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 受講生詳細の受講生でメールアドレスにメールアドレスの形式以外を用いたときに入力チェックにかかること() {
    Student student = new Student();
    student.setId("1");
    student.setName("中野");
    student.setFurigana("ナカノ");
    student.setNickname("ナカヤン");
    student.setAddress("aaaa");
    student.setLive("北海道");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("メールアドレスの形式になっていません!");
  }

  @Test
  void 受講生詳細の受講生で名前_フリガナ_ニックネーム_メールアドレス_住所が半角スペースのみの場合に入力チェックにかかること() {
    Student student = new Student();
    student.setId("1");
    student.setName("  ");
    student.setFurigana("  ");
    student.setNickname("  ");
    student.setAddress("  ");
    student.setLive("  ");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の申込状況で入力チェックにかからないこと() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setId("1");
    studentCourseStatus.setStudentCourseId("1");
    studentCourseStatus.setStatus(Status.本申込);

    Set<ConstraintViolation<StudentCourseStatus>> violations = validator.validate(
        studentCourseStatus);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生コース情報の申込状況でIDに数字以外を用いたときに入力チェックにかかること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setId("aaa");
    studentCourseStatus.setStudentCourseId("bbb");
    studentCourseStatus.setStatus(Status.本申込);

    Set<ConstraintViolation<StudentCourseStatus>> violations = validator.validate(
        studentCourseStatus);

    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");

  }

}