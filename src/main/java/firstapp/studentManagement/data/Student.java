package firstapp.studentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter

public class Student {

  @Pattern(regexp = "^[0-9]+$", message = "数字のみ入力するようにしてください。")
  private String id;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  @NotBlank
  private String nickname;

  @NotBlank
  @Email(message = "メールアドレスの形式になっていません!")
  private String address;

  @NotBlank
  private String live;

  private int age;

  private String gender;

  @NotNull
  private String remark;

  private boolean isDeleted;

}
