package zerobase.weather.dto;

import lombok.*;

public class DeleteDiary {

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Response {
        private final String message = "삭제에 성공했습니다.";
    }
}
