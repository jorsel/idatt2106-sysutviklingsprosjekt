package idatt2106scrumteam10.GIDD.utils;

import idatt2106scrumteam10.GIDD.models.Location;
import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.services.LocationService;
import idatt2106scrumteam10.GIDD.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Filter {

    @Autowired
    private TagService tagService;

    @Autowired
    private LocationService locationService;

    public List<String> getStringAsList(String string) {
        return Arrays.stream(string.split(",")).filter(s -> !s.equals("")).collect(Collectors.toList());
    }

    public Set<String> getStringAsSet(String string) {
        return Arrays.stream(string.split(",")).filter(s -> !s.equals("")).collect(Collectors.toSet());
    }

    public List<Integer> parseIntegersFromStrings(List<String> integers) {
        return integers.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public List<Double> parseDoublesFromStrings(List<String> doubles) {
        return doubles.stream().map(Double::parseDouble).collect(Collectors.toList());
    }

    public List<Location> getLocationsFromDoubles(List<Double> doubles) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < doubles.size()/2; i++) {
            Location location = locationService.findByLatitudeAndLongitude(doubles.get(i), doubles.get(i + 1));
            if (location != null) {
                locations.add(location);
            }
        }
        return locations;
    }

    public List<LocalDateTime> getDatesFromStrings(List<String> strings) {
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateStart = LocalDate.parse(strings.get(i), formatter).atStartOfDay();
            LocalDateTime dateEnd = LocalDate.parse(strings.get(i), formatter).atTime(LocalTime.MAX);
            localDateTimes.add(dateStart);
            localDateTimes.add(dateEnd);
        }
        return localDateTimes;
    }

    public Map<String, Object> getNewFilter(Map<String, String> filter) {
        Map<String, Object> newFilter = new HashMap<>();
        Sort sort = Sort.by("ID").descending();
        for (String key : filter.keySet()) {
            if (filter.get(key) != null && !filter.get(key).equals("")) {
                switch (key) {
                    case "dates":
                        List<String> dateStrings = getStringAsList(filter.get("dates"));
                        List<LocalDateTime> localDateTimes = getDatesFromStrings(dateStrings);
                        newFilter.put(key, localDateTimes);
                        break;
                    case "tags":
                        Set<String> tagStrings = getStringAsSet(filter.get(key));
                        Set<Tag> tags = tagService.findByTagIn(tagStrings);
                        newFilter.put(key, tags);
                        break;
                    case "locations":
                        List<String> locationStrings = getStringAsList(filter.get(key));
                        List<Double> locationDoubles = parseDoublesFromStrings(locationStrings);
                        List<Location> locations = getLocationsFromDoubles(locationDoubles);
                        newFilter.put(key, locations);
                        break;
                    case "intensities":
                        List<String> intensityStrings = getStringAsList(filter.get(key));
                        List<Integer> intensities = parseIntegersFromStrings(intensityStrings);
                        newFilter.put(key, intensities);
                        break;
                    case "page":
                        int pageNumber = Integer.parseInt(filter.get(key));
                        newFilter.put(key, pageNumber);
                        break;
                    case "sort":
                        Sort.Order order;
                        String sortBy = filter.get(key);
                        switch (sortBy) {
                            case "start_asc":
                                sort = Sort.by("start").ascending();
                                break;
                            case "start_desc":
                                sort = Sort.by("start").descending();
                                break;
                            case "end_asc":
                                sort = Sort.by("end").ascending();
                                break;
                            case "end_desc":
                                sort = Sort.by("end").descending();
                                break;
                            case "intensity_asc":
                                sort = Sort.by("intensity").ascending();
                                break;
                            case "intensity_desc":
                                sort = Sort.by("intensity").descending();
                                break;
                            case "title_asc":
                                order = new Sort.Order(Sort.Direction.ASC, "title").ignoreCase();
                                sort = Sort.by(order);
                                break;
                            case "title_desc":
                                order = new Sort.Order(Sort.Direction.DESC, "title").ignoreCase();
                                sort = Sort.by(order);
                                break;
                        }
                        break;
                    case "search":
                        String search = filter.get(key);
                        newFilter.put(key, search);
                        break;

                }
            }
        }
        newFilter.put("sort", sort);
        newFilter.putIfAbsent("page", 0);
        return newFilter;
    }

}
