package zerobase.weather.dto;

import lombok.*;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDto {
    private String text;
    private LocalDate date;
    private String weather;
    private String icon;
    private Double temperature;

    public static DiaryDto fromDto(Diary diary){
        return DiaryDto.builder()
                .text(diary.getText())
                .date(diary.getDate().getDate())
                .weather(diary.getDate().getWeather())
                .icon(diary.getDate().getIcon())
                .temperature(diary.getDate().getTemperature())
                .build();
    }
}
