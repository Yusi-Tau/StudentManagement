package firstapp.studentManagement.controller.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class Handler {

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<String> handleNullException(NullPointerException nullEx) {
    log.error("データが存在しませんでした！", nullEx);
    return new ResponseEntity<>("データが見つかりませんでした!", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleNotValidException(
      MethodArgumentNotValidException notValidEx) {
    log.error("未入力や検証に失敗した値が存在します！", notValidEx);
    return new ResponseEntity<>("入力されていない、または想定されていない値が入力されています！ \n"
        + "入力内容をご確認いただき、再度入力をお試しください。",
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintException(ConstraintViolationException conEx) {
    log.error("正規表現で定められていない値が入力されています！", conEx);
    return new ResponseEntity<>("検索不能なURLです。\n"
        + "student/◯◯の◯◯には、半角数字を入力してください。", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageException(HttpMessageNotReadableException httpEx) {
    log.error("入力した値がサーバー側で読み取れる形ではありませんでした。", httpEx);
    return new ResponseEntity<>("入力した値がサーバーで読み取れませんでした。\n"
        + "入力内容をご確認いただき、再度入力をお試しください。",
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleArgumentException(IllegalArgumentException ex) {
    log.error("入力された値が他のデータとの整合性が取れていません！", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException runEx) {
    log.error("予期せぬエラーが発生しました！", runEx);
    return new ResponseEntity<>("サーバーで予期せぬエラーが発生しました！",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
