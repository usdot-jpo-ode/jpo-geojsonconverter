package us.dot.its.jpo.geojsonconverter.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.PsmFeature;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SchemaGeneratorUtility {
    public static void main(String[] args) throws IOException {
        try {
            // Define the classes for which to generate schemas
            Class<?>[] targetClasses =
                    {ProcessedPsm.class, ProcessedBsm.class, ProcessedMap.class, ProcessedSpat.class};

            ObjectMapper objectMapper = new ObjectMapper();

            SchemaGeneratorConfigBuilder configBuilder =
                    new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);

            // Add Jackson module for better handling of Jackson annotations
            configBuilder.with(new JacksonModule());

            // Configure additional options
            configBuilder.with(Option.EXTRA_OPEN_API_FORMAT_VALUES).without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
                    .with(Option.DEFINITIONS_FOR_ALL_OBJECTS);

            SchemaGenerator generator = new SchemaGenerator(configBuilder.build());

            // Find the project root directory
            Path currentPath = Paths.get("").toAbsolutePath();

            File resourcesDir = null;

            if (args.length > 0 && args[0].equals("--output")) {
                resourcesDir = new File(args[1]);
            } else {
                resourcesDir = new File(currentPath.toString(), "src/main/resources/schemas");
            }

            System.out.println("Creating schemas directory at: " + resourcesDir.getAbsolutePath());
            resourcesDir.mkdirs();

            // Generate schemas and save to files
            for (Class<?> targetClass : targetClasses) {
                JsonNode schema = generator.generateSchema(targetClass);

                // Create the schema file in the resources/schemas directory
                // Add hyphen after "Processed"
                String fileName = targetClass.getSimpleName().replaceAll("(?<=Processed)(?=\\w)", "-").toLowerCase()
                        + ".schema.json";
                File outputFile = new File(resourcesDir, fileName);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, schema);

                System.out.println(
                        "Generated schema for: " + targetClass.getSimpleName() + " at " + outputFile.getAbsolutePath());
            }

            System.out.println("Schema generation completed successfully.");
        } catch (Exception e) {
            System.err.println("Error generating schemas: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Exit successfully
        System.exit(0);
    }
}
