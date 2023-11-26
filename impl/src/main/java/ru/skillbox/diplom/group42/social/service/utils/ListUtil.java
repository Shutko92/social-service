package ru.skillbox.diplom.group42.social.service.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {
    public static <T, U> List<U> map(List<T> list, Function<? super T, ? extends U> converter) {
        return list.stream()
                .map(converter)
                .collect(Collectors.toList());
    }
}
