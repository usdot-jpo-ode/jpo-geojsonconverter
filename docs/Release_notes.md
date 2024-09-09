JPO GeoJSON Converter Release Notes
----------------------------

Version 1.4.0
----------------------------------------

### **Summary**
This release includes adding support for processing OdeBsmJson messages and creating ProcessedBsm. ProcessedBsm messages are geojson objects representing FeatureCollections with additional processed fields for additional context. ProcessedBsm currently only supports standard geojson format and not WKT format.

Enhancements in this release:
- PR67: Allow unknown properties via Jackson properties.
- PR66: Adds ProcessedBsm as a new message type that is created utilizing OdeBsmJson output from the jpo-ode.

Version 1.3.0
----------------------------------------

### **Summary**
This release includes adding a schema version to the ProcessedMap and ProcessedSpat message types to track changes in the schema.

Enhancements in this release:
- PR55: Adds schema version values to ProcessedMap and ProcessedSpat.

Version 1.2.0
----------------------------------------

### **Summary**
This release includes upgrading the project to use Java 21 in order to future proof the project.

Enhancements in this release:
- PR44: Java 21 support.


Version 1.1.0
----------------------------------------

### **Summary**
This release includes modifications to the ProcessedMap output, resiliency changes for Docker deployments and some minor bug fixes. The MAP and SPaT messages are now also partitioned in Kafka by their intersection ID rather than the origin IP.

Enhancements in this release:
- PR37: Partition Kafka topics by intersection ID instead of origin IP address.
- PR35: Fix bug with SPaT relative UTC timestamps when the hour rolls over. 
- PR33: Include laneType field in ProcessedMap messages for each lane.
- PR27: Add auto restart policy to local docker-compose deployments.


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
