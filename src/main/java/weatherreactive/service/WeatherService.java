package weatherreactive.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import weatherreactive.model.Weather;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author ArvikV
 * @version 1.1
 * @since 21.02.2022
 * нахождение самого горячего переделано на стримы
 */
@Service
public class WeatherService {
    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 13));
        weathers.put(4, new Weather(4, "Smolensk", 13));
        weathers.put(5, new Weather(5, "Kiev", 15));
        weathers.put(6, new Weather(6, "Minsk", 15));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    /**
     * ищем самый горячий город
     * @return на выходе город с самой высокой температурой
     * tempTemperaturen эталонный с которым начинаем сравнивать
     * бежим по мапе и сравниваем с эталоном, если больше меняем эталон(переделано на стримчанский)
     * вызываем стрим значений и назожим максимальное сравнивая температуры
     */
    public Mono<Weather> findHottestCity() {
        return Mono.just(weathers.values()
                .stream()
                .max(Comparator.comparing(Weather::getTemperature)).get());
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    /**
     * ищем город с темп больше указанной
     * @param temperaturen указ
     * @return на выходе лист городов
     * берем все значение, подключаем стрим, фильтруем в нем, записуем в лист
     */
    public Flux<Weather> findCityGreatThen(int temperaturen) {
        return Flux.fromIterable(weathers.values()
                .stream().filter(x -> x.getTemperature() > temperaturen)
                .collect(Collectors.toList()));
    }
}
