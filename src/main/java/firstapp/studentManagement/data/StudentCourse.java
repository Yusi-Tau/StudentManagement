package firstapp.studentManagement.data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourse {

  private String id;

  private String studentId;

  @NotBlank
  private String courseName;

  private LocalDate startDate;

  private LocalDate completionDate;

}
