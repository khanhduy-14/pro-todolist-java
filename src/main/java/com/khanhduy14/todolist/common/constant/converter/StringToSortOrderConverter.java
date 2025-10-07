package com.khanhduy14.todolist.common.constant.converter;


import com.khanhduy14.todolist.common.constant.SortOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortOrderConverter implements Converter<String, SortOrder> {

    @Override
    public SortOrder convert(String source) {
        if (source == null || source.isEmpty()) return null;

        switch (source.toLowerCase()) {
            case "asc": return SortOrder.ASC;
            case "desc": return SortOrder.DESC;
            default:
                throw new IllegalArgumentException("Unknown SortOrder: " + source);
        }
    }
}
