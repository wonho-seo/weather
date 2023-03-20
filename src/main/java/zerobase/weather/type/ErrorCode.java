package zerobase.weather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("예기치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_FOUNT_WEATHER("데이터가 존재하지 않습니다.", 1000);

    private final String description;
    private final int errorStatus;
}
