JPO GeoJSON Converter Release Notes
----------------------------

Version 2.2.0
----------------------------------------

### **Summary**
This release includes compatibility upgrades for the jpo-ode 4.1.0 version.

Enhancements in this release:
- [CDOT PR 13](https://github.com/CDOT-CV/jpo-geojsonconverter/pull/13): Update gjc to work with latest version of the jpo-ode 4.1.0

Version 2.0.0 + 2.1.0
----------------------------------------

### **Summary**
This release includes optimizations to the jpo-geojsonconverter's Kafka data production rates, a migration from the jpo-ode submodule to a GitHub Maven Central artifact, schema updates, ProcessedBsm processing logic modifications, and new support for the latest major version of the jpo-ode (4.0.0). Due to the support of the latest breaking changes of the jpo-ode, this version of the jpo-geojsonconverter is also incrementing a major version. OdeMapJson from the jpo-ode 4.0.0 will not be supported by versions of the jpo-geojsonconverter prior to 2.0.0. This also means OdeMapJson from earlier versions of the jpo-ode will not be supported by the jpo-geojsonconverter 2.0.0.

Enhancements in this release:
- USDOT PR84: Refactor output ProcessedBsm to be just a feature instead of a feature collection.
- CDOT PR5: Updated the processing logic of the ProcessedMap message to pull node values from the latest output of the OdeMapJson topic from the jpo-ode.
- USDOT PR80: Added GitHub action caching.
- USDOT PR78: Switched the artifact publisher to temurin.
- USDOT PR77: Modified the processing logic for the ProcessedBsm `timeStamp` value to pull from the OdeBsmJson `odeReceivedAt` value.
- USDOT PR76: Update schemas to JSON schema version `2020-12`.
- USDOT PR75: Remove repository submodules for the jpo-ode and add GitHub Maven Central artifact publishing.
- USDOT PR73: Added zstd compression to Kafka producers and consumers. 

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
