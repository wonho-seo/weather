package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.dto.CreateDiaryDto;
import zerobase.weather.dto.DeleteDiary;
import zerobase.weather.dto.DiaryDto;
import zerobase.weather.dto.UpdateDiaryDto;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "다이어리 생성")
    @PostMapping("/create/diary")
    public CreateDiaryDto.Response createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2023-03-20")
                                               LocalDate date, @RequestBody CreateDiaryDto.Request request) {
        return CreateDiaryDto.Response.formEntity(diaryService.createDiary(date, request.getText()));
    }

    @ApiOperation(value = "특정 날짜 다이어리 가져오기")
    @GetMapping("/read/diary")
    public List<DiaryDto> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2023-03-20")
                                    LocalDate date) {
        return diaryService.readDiary(date);
    }

    @ApiOperation(value = "두 날짜 사이의 다이어리 목록 가져오기")
    @GetMapping("/read/diaries")
    public List<DiaryDto> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2023-03-20")
                                      LocalDate startDate,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2023-03-20")
                                      LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @ApiOperation(value = "특정날짜의 다이어리 수정", notes = "단 특정날짜에 가장 먼저 작성된 것만 수정가능")
    @PutMapping("/update/diary")
    public UpdateDiaryDto.Response updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2023-03-20")
                                               LocalDate date, @RequestBody UpdateDiaryDto.Request request) {
        return UpdateDiaryDto.Response.formEntity(diaryService.updateDiary(date, request.getText()));
    }

    @ApiOperation(value = "특정날자 다이어리 삭제", notes = "모두 삭제")
    @DeleteMapping("/delete/diary")
    public DeleteDiary.Response deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
        return new DeleteDiary.Response();
    }


    @ApiOperation(value = "강제 weather_api 호출")
    @PostMapping("/weather")
    public ResponseEntity<String> getApi(){
        diaryService.saveWeatherDate();
        return ResponseEntity.ok("success");
    }
}
