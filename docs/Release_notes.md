JPO GeoJSON Converter Release Notes
----------------------------

Version 1.0.0
----------------------------------------

### **Summary**
The first release for the jpo-geojsonconverter, version 1.0.0, includes a fully functioning GeoJSON Converter for MAP messages and enhanced SPaT messages. The jpo-geojsonconverter consumes jpo-ode OdeMapJson and OdeSpatJson messages and creates ProcessedMap and ProcessedSpat messages. The jpo-geojsonconverter can use local Kafka or Confluent Cloud SASL authentication. Depending on the needs of the user, this release also provides the option to output all ProcessedMap message GeoJSON as well-known text (WKT) based geometry. Read more about the jpo-geojsonconverter in the [main README](<../README.md>).

Enhancements in this release:
- PR24: Revised log levels for project.
- PR23: Remove MAP node data and validation messages from WKT output.
- PR21: Well-Known Text (WKT) support for ProcessedMap messages.
- PR18: Kafka updates for creating topics on project creation for local Kafka.
- PR16: Schema updates to output processed messages.
- PR14: Confluent Cloud support as a Kafka broker using SASL authentication.
- PR13: Schema updates to MAP.
- PR12 and 13: MapGeoJson reworked to ProcessedMap messages. Contains relational information to support linking ProcessedSpat messages for visualization.
- PR11: SpatGeoJson reworked to ProcessedSpat messages with only enhanced data and no geoJSON.
- PR10: Added Maven JAR compiling plugin.
- PR8: Documentation added for JSON messages.
- PR7: MAP and SPaT OdeJson validation.
- PR5: First unit tests. Coverage >90%.
- PR3: MapGeoJson and SpatGeoJson output added.
- PR2: Initial Spring Boot application with Kafka Streams support and docker-compose for local deployment.
