package firstapp.studentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コースの申込状況")
@Getter
@Setter
public class StudentCourseStatus {

  @NotBlank
  @Pattern(regexp = "^[0-9]+$", message = "数字のみ入力するようにしてください。")
  private String id;

  @NotBlank
  @Pattern(regexp = "^[0-9]+$", message = "数字のみ入力するようにしてください。")
  private String studentCourseId;

  @NotNull
  private Status status;

  public enum Status {
    仮申込,
    本申込,
    受講中,
    受講終了

  }


}
