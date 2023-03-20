package zerobase.weather.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

import java.time.LocalDate;

public class CreateDiaryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @ApiParam(value = "일기 내용")
        private String text;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @ApiParam(value = "저장된 일기 내용")
        private String text;

        @ApiParam(value = "저장된 날씨")
        private String weather;

        @ApiParam(value = "저장된 아이콘")
        private String icon;

        @ApiParam(value = "저장된 날짜")
        private LocalDate date;

        @ApiParam(value = "저장된 온도")
        private Double temperature;

        public static Response formEntity(DiaryDto diary) {
            return Response.builder()
                    .text(diary.getText())
                    .weather(diary.getWeather())
                    .icon(diary.getIcon())
                    .date(diary.getDate())
                    .temperature(diary.getTemperature())
                    .build();
        }
    }

}
