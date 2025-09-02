package firstapp.studentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  @Pattern(regexp = "^[0-9]+$")
  private String id;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  @NotBlank
  private String nickname;

  @NotBlank
  @Email
  private String address;

  @NotBlank
  private String live;

  private int age;

  private String gender;

  @NotNull
  private String remark;

  private boolean isDeleted;

}
