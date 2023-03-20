package zerobase.weather.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findByDate(DateWeather date);

    List<Diary> findAllByDateBetween(DateWeather startDate, DateWeather endDate);

    Optional<Diary> getFirstByDate(DateWeather date);

    void deleteAllByDate(DateWeather date);
}
