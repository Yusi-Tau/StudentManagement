package firstapp.studentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private String name = "Yusi Tau";
	private String age = "45";

	private Map<String,String> student = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public Map<String,String> getInfo() {
		return student;
	}

	@PostMapping("/studentInfo")
	public void setInfo(String name, String age) {
		this.name = name;
		this.age = age;
	}

	@PostMapping("/studentName")
	public void setName(String name) {
		this.name = name;
	}

	@PostMapping("/studentMap")
	public void setMap(@RequestParam String name, @RequestParam String age) {
		this.student.put(name,age);
	}
}
