package zerobase.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.WeatherApiDto;
import zerobase.weather.exception.WeatherException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.type.ErrorCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    @Value("${open_weather_map.key}")
    private String apiKey;

    private final DateWeatherRepository dateWeatherRepository;
    private final DiaryRepository diaryRepository;
    private final String apiBaseUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=";

    private static final Logger logger= LoggerFactory.getLogger(WeatherApplication.class);
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DiaryDto createDiary(LocalDate date, String text){
        logger.info("started to create diary");
        DateWeather dateWeather = dateWeatherRepository.findById(date)
                .orElseThrow(()-> new WeatherException(ErrorCode.NOT_FOUNT_WEATHER));

        Diary diary = Diary.builder()
                .text(text)
                .date(dateWeather)
                .build();
        diaryRepository.save(diary);
        logger.info("end to create diary");
        return DiaryDto.builder()
                .text(text)
                .date(dateWeather.getDate())
                .weather(dateWeather.getWeather())
                .icon(dateWeather.getIcon())
                .temperature(dateWeather.getTemperature())
                .build();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate(){
        logger.info("save api");
        dateWeatherRepository.save(getWeatherFromApi());

        logger.info("success save api");
    }

    public List<DiaryDto> readDiary(LocalDate date) {
        logger.info("read diary");
        List<Diary> diary = diaryRepository.findByDate(DateWeather.builder()
                .date(date)
                .build());

        return diary.stream().map((diary1 -> DiaryDto.fromDto(diary1))).collect(Collectors.toList());
    }

    public List<DiaryDto> readDiaries(LocalDate startDate, LocalDate endDate) {
        List<Diary> diary = diaryRepository.findAllByDateBetween(DateWeather.builder()
                        .date(startDate)
                        .build(),
                DateWeather.builder()
                        .date(endDate)
                        .build());

        return diary.stream().map((diary1 -> DiaryDto.fromDto(diary1))).collect(Collectors.toList());
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DiaryDto updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(DateWeather.builder()
                        .date(date)
                        .build())
                .orElseThrow(() -> new RuntimeException());
        nowDiary.setText(text);

        return DiaryDto.fromDto(nowDiary);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(DateWeather.builder()
                .date(date)
                .build());
    }

    public String getWeatherString(){
        String apiUrl = apiBaseUrl + apiKey;
        try{
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader br;

            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            } else{
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e){
            return "fail";
        }
    }

    private DateWeather getWeatherFromApi(){
        return parseWeather(getWeatherString());
    }
    private DateWeather parseWeather(String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            WeatherApiDto weatherApiDto = objectMapper.readValue(jsonString, WeatherApiDto.class);
            WeatherApiDto.Weather weather = weatherApiDto.getWeather().get(0);
            return DateWeather.builder()
                    .date(LocalDate.now())
                    .icon(weather.getIcon())
                    .weather(weather.getMain())
                    .temperature(weatherApiDto.getMain().getTemp())
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
