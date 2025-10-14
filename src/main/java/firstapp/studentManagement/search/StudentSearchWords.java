package firstapp.studentManagement.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "検索ワード一覧")
@Getter
@Setter
public class StudentSearchWords {

  private Integer id;

  private String name;

  private String furigana;

  private String nickname;

  private String live;

  private Integer ageFrom;

  private Integer ageTo;

  private String gender;

  private Boolean isDeleted;

  private String courseName;

  private String courseStatus;

  private boolean courseNameAllShow;
}
