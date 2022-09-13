package com.dounion.framework.helper;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.util.Arrays;

public class ResourceHelperTest {

    @Test
    public void test() throws Exception {
        final String path = "/application-framework-core.properties";
        Resource resource = ResourceHelper.getResource(path);
        System.out.println(resource.isFile());
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        System.out.println(Arrays.toString(bytes));
    }

}
