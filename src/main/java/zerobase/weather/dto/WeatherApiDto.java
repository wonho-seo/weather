package zerobase.weather.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherApiDto {
    private List<Weather> weather;
    private Main main;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Weather {

        String main;
        String description;
        String icon;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Main{
        Double temp;
    }
}
