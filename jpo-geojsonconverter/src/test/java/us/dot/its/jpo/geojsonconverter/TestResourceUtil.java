package us.dot.its.jpo.geojsonconverter;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestResourceUtil {
    public static String loadResource(String resourceName) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(resourceName);
        return resource.getContentAsString(StandardCharsets.UTF_8);
    }
}
