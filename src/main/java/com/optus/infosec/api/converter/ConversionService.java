package com.optus.infosec.api.converter;

import org.springframework.core.convert.support.GenericConversionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SM
 *
 * Conversion Service to conver DTO to Entity and Vice Versa
 *
 * @param <S>
 * @param <T>
 */
public class ConversionService<S,T> extends GenericConversionService {


    /**
     * Convert List from src to target which may include NULL
     *
     * @param srcList
     * @param targetType
     * @return
     */
    public List<T> convertList(final List<S> srcList, Class<T> targetType) {
        return convertListInternal(srcList, targetType, true);
    }

    /**
     * Convert list from source to destination
     *
     * @param srcList
     * @param targetType
     * @param allowedNull
     * @return List<T>
     */
    private List<T> convertListInternal(final List<S> srcList, Class<T> targetType, boolean allowedNull) {
        List<T> targetList = new ArrayList<T>(srcList.size());
        for (S src : srcList) {
            if (!allowedNull) {
                if (src != null) {
                    final T converted = convert(src, targetType);
                    if (converted != null) {
                        targetList.add(converted);
                    }
                }
            } else {
                targetList.add(convert(src, targetType));
            }
        }
        return targetList;
    }
}
