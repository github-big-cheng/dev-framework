package com.wisely.framework.handler.data;

import com.wisely.framework.helper.ValidHelper;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class DataConverterImportSelector implements ImportSelector {

    /**
     * 优先级
     * 1. com.dounion.framework.handler.data.DataConverter
     * 2. com.dounion.framework.handler.data.DefaultDataConverter
     *
     * @param annotationMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        List<String> dataConverts =
                SpringFactoriesLoader.loadFactoryNames(DataConverter.class, DataConverter.class.getClassLoader());
        if (ValidHelper.isEmpty(dataConverts)) {
            dataConverts =
                    SpringFactoriesLoader.loadFactoryNames(DefaultDataConverter.class, DefaultDataConverter.class.getClassLoader());
        }

        return dataConverts.toArray(new String[dataConverts.size()]);
    }

}
