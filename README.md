# jpo-geojsonconverter

**US Department of Transportation (USDOT) Intelligent Transportation Systems (ITS) Joint Program Office (JPO) Intersection GeoJSON Converter**

The JPO Intersection GeoJSON Converter is a real-time validator and data converter of JPO-ODE MAP and SPaT JSON based on the SAE J2735 message standard. Messages are consumed from Kafka and validated based on both the SAE J2735 standard and the more robust Connected Transportation Interoperability (CTI) Intersection Implementation Guide Message Requirements (Section 3.3.3). Message validation occurs simultaneously as the GeoJSON converter converts the JPO-ODE MAP and SPaT messages into mappable geoJSON. The JPO Intersection GeoJSON Converter outputs the resulting geoJSON onto Kafka topics. These messages contain validation information that identifies all issues encountered with validation, if any.

![alt text](docs/jpo-geojsonconverter_arch_diagram.png "jpo-geojsonconverter Design Diagram")

The message validation has been included in the jpo-geojsonconverter in order to prevent too many small microservices from being created. The extent of the current validation that occurs is surface level and is supported by simple verification against a schema that is based on J2735 and the CTI Intersection Implementation Guide. There may be reason to eventually break this feature out into a new, separate repository if more complex validation must be performed.

All stakeholders are invited to provide input to these documents. To provide feedback, we recommend that you create an "issue" in this repository (<https://github.com/usdot-jpo-ode/jpo-geojsonconverter/issues>). You will need a GitHub account to create an issue. If you don’t have an account, a dialog will be presented to you to create one at no cost.

## Release Notes

The current version and release history of the JPO GeoJSON Converter: [Release Notes](<docs/Release_notes.md>)

---

<a name="toc"/>

## Table of Contents

1. [Usage Example](#usage-example)
2. [Configuration](#configuration)
3. [Installation](#installation)
4. [File Manifest](#file-manifest)
5. [Development Setup](#development-setup)
6. [Contact Information](#contact-information)
7. [Contributing](#contributing)
<!--
#########################################
############# Usage Example #############
#########################################
 -->

<a name="usage-example"/>

## 1. Usage Example

The jpo-geojsonconverter is used to convert the ODE JSON output of MAP, SPaT and BSM messages into GeoJSON ProcessedMap, enhanced ProcessedSpat, GeoJSON ProcessedBsm, and GeoJSON ProcessedPsm messages. In order to verify your jpo-geojsonconverter is functioning, you must run the jpo-ode, then the jpo-geojsonconverter, and then send the jpo-ode raw ASN1 encoded MAP, SPaT and BSM data.

Follow the configuration section to properly configure and launch your jpo-ode and jpo-geojsonconverter.

Run one of the UDP sender Python scripts from the [jpo-ode repository](https://github.com/usdot-jpo-ode/jpo-ode/tree/dev/scripts/tests) to generate some example MAP/SPaT/BSM messages. Make sure to set your DOCKER_HOST_IP environment variable so the script will properly send the ASN1 encoded messages to your locally running JPO-ODE.

Once the message has been sent to the jpo-ode, it will be eventually be decoded and serialized into a JSON string. This string will be placed into the Kafka topics topic.OdeMapJson, topic.OdeSpatJson, and topic.OdeBsmJson. The jpo-geojsonconverter will then transform them into geoJSON. If a user needs WKT formatted GeoJSON, it is possible to turn this on by specifying the geometry.output.mode environment variable to "WKT". Currently WKT does not support the BSM messages.

### <b>Output Message Types</b>

### ProcessedMap

When an OdeMapJson message is processed through the jpo-geojsonconverter, a ProcessedMap message is created. This message is a single JSON object that contains two geoJSON FeatureCollection objects and one regular JSON object.

- *mapFeatureCollection* - Feature Collection for storing all of the unique metadata and geographic data for each lane in an intersection. Mapping this object would display all defined lanes in an OdeMapJson object.
- *connectingLanesFeatureCollection* - Feature Collection for storing all geographic data for connecting lanes within an intersection. When mapped, the feature collection displays a bunch of two point lines connecting each egress lane to all possible and legal traversals to ingress lanes. Useful for visualizing ProcessedSpat data.
- *properties* - General metadata and property values that are important to keep with the processed MAP message but aren't related to any single lane in the intersection. RSU IP, intersection ID, and validation messages are stored here.

[ProcessedMap schema can be found here.](<jpo-geojsonconverter/src/main/resources/schemas/processed-map.schema.json>)

### ProcessedSpat

When an OdeSpatJson message is processed through the jpo-geojsonconverter, a ProcessedSpat message is created. This message is a single JSON object that contains all of the important information within a SPaT message for matching it to a corresponding ProcessedMap message and for identifying its state. There is no geoJSON component to a ProcessedSpat message on its own.

[ProcessedSpat schema can be found here.](<jpo-geojsonconverter/src/main/resources/schemas/processed-spat.schema.json>)

Example ProcessedSpat message:

```json
{
	"schemaVersion": 2,
	"messageType": "SPAT",
	"odeReceivedAt": "2025-07-16T22:55:40.636Z",
	"originIp": "172.18.0.1",
	"asn1": "0381004003807C00134700081132000000E437070010434257925790010232119A11CE800C10D095E495E400808684AF24AF20050434257925790030232119A11CE801C10D095E495E401008684AF24AF200",
	"validationMessages": [
		{
			"message": "CTI-4501 conformance issue: the SPAT 'timeStamp' DE_MinuteOfTheYear is missing"
		},
		{
			"message": "CTI-4501 conformance issue: the intersections 'id.region' DE_RoadRegulatorID is missing"
		},
		{
			"message": "CTI-4501 conformance issue: the state-time-speed 'timing.startTime' DE_TimeMark is missing"
		},
		{
			"message": "CTI-4501 conformance issue: the state-time-speed 'timing.nextTime' DE_TimeMark is missing"
		}
	],
	"intersectionId": 8804,
	"cti4501Conformant": false,
	"revision": 0,
	"status": {
		"manualControlIsEnabled": false,
		"stopTimeIsActivated": false,
		"failureFlash": false,
		"preemptIsActive": false,
		"signalPriorityIsActive": false,
		"fixedTimeOperation": false,
		"trafficDependentOperation": false,
		"standbyOperation": false,
		"failureMode": false,
		"off": false,
		"recentMAPmessageUpdate": false,
		"recentChangeInMAPassignedLanesIDsUsed": false,
		"noValidMAPisAvailableAtThisTime": false,
		"noValidSPATisAvailableAtThisTime": false
	},
	"utcTimeStamp": "2025-07-16T22:55:58.423Z",
	"states": [
		{
			"signalGroup": 1,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		},
		{
			"signalGroup": 2,
			"stateTimeSpeed": [
				{
					"eventState": "protected-Movement-Allowed",
					"timing": {
						"minEndTime": "2025-07-16T22:30:02.4Z",
						"maxEndTime": "2025-07-16T22:30:23.4Z"
					}
				}
			]
		},
		{
			"signalGroup": 3,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		},
		{
			"signalGroup": 4,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		},
		{
			"signalGroup": 5,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		},
		{
			"signalGroup": 6,
			"stateTimeSpeed": [
				{
					"eventState": "protected-Movement-Allowed",
					"timing": {
						"minEndTime": "2025-07-16T22:30:02.4Z",
						"maxEndTime": "2025-07-16T22:30:23.4Z"
					}
				}
			]
		},
		{
			"signalGroup": 7,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		},
		{
			"signalGroup": 8,
			"stateTimeSpeed": [
				{
					"eventState": "stop-And-Remain",
					"timing": {
						"minEndTime": "2025-07-16T22:31:58.6Z",
						"maxEndTime": "2025-07-16T22:31:58.6Z"
					}
				}
			]
		}
	]
}
```

### ProcessedBsm

When an OdeBsmJson message is processed through the jpo-geojsonconverter, a ProcessedBsm message is created. This message is a GeoJSON feature collection that contains extra metadata fields that represents a single BSM.

- *type* - The type of object the JSON is. This will always be a 'Feature'.
- *geometry* - The geometry of the BSM.
- *properties* - The properties of the BSM.
  - *schemaVersion* - The schema version that the JSON object was generated for.
  - *messageType* - The message type the FeatureCollection represents.
  - *odeReceivedAt* - A date-time string representing when the BSM was received by the ODE.
  - *timestamp* - A date-time string representing when the BSM processed and transformed into a ProcessedBsm.
  - *originIp* - An IP string representing the RSU the BSM was generated by if there is a IP present.
  - *validationMessages* - A list of validation messages that specify issues found within the original OdeBsmJson object based on the JSON schema.

[ProcessedBsm schema can be found here.](<jpo-geojsonconverter/src/main/resources/schemas/processed-bsm.schema.json>)

```json
{
	"type": "Feature",
	"geometry": {
		"type": "Point",
		"coordinates": [
			-105.0317754,
			40.5659938
		]
	},
	"properties": {
		"schemaVersion": 2,
		"messageType": "BSM",
		"odeReceivedAt": "2025-07-15T12:25:38.620Z",
		"timeStamp": "2025-07-15T12:25:25.399Z",
		"originIp": "172.18.0.1",
		"asn1": "001480B8494C4C950CD8CDE6E9651116579F22A424DD78FFFFF00761E4FD7EB7D07F7FFF80005F11D1020214C1C0FFC7C016AFF4017A0FF65403B0FD204C20FFCCC04F8FE40C420FFE6404CEFE60E9A10133408FCFDE1438103AB4138F00E1EEC1048EC160103E237410445C171104E26BC103DC4154305C2C84103B1C1C8F0A82F42103F34262D1123198103DAC25FB12034CE10381C259F12038CA103574251B10E3B2210324C23AD0F23D8EFFFE0000209340D10000004264BF00",
		"validationMessages": [],
		"accelSet": {
			"accelLat": 0.0,
			"accelLong": 0.27,
			"accelVert": 0.0,
			"accelYaw": 0.0
		},
		"accuracy": {
			"semiMajor": 9.3,
			"semiMinor": 12.05
		},
		"brakes": {
			"wheelBrakes": {
				"unavailable": true,
				"leftFront": false,
				"leftRear": false,
				"rightFront": false,
				"rightRear": false
			},
			"traction": "unavailable",
			"abs": "unavailable",
			"scs": "unavailable",
			"brakeBoost": "unavailable",
			"auxBrakes": "unavailable"
		},
		"heading": 313.25,
		"id": "31325433",
		"msgCnt": 37,
		"secMark": 25399,
		"size": {
			"width": 190,
			"length": 570
		},
		"speed": 0.28,
		"transmission": "unavailable"
	}
}
```

### ProcessedPsm

When an OdePsmJson message is processed through the jpo-geojsonconverter, a ProcessedPsm message is created. This message is a GeoJSON feature that contains extra metadata fields that represents a single PSM.

- *type* - The type of object the JSON is. This will always be a 'Feature'.
- *geometry* - The geometry of the PSM.
- *properties* - The properties of the PSM.
  - *schemaVersion* - The schema version of the base PSM message.
  - *messageType* - The message type the Feature represents.
  - *odeReceivedAt* - A date-time string representing when the PSM was received by the ODE.
  - *timeStamp* - A date-time string representing the PSM generation time. It uses the time that the ODE was generated as the base timestamp but pulls the milliseconds in the seconds (from the "secMark" field) to represent the PSM generation time.
  - *originIp* - An IP string representing the RSU the PSM was generated by if there is a IP present.
  - *validationMessages* - A list of validation messages that specify issues found within the original OdePsmJson object based on the JSON schema.
  - *basicType* - The basic type of the PSM.
  - *id* - The ID of the PSM.
  - *msgCnt* - The message count of the PSM.
  - *secMark* - J2735 secMark field of the PSM that represents the milliseconds in the seconds of the PSM generation time.
  - *speed* - The speed of the PSM.
  - *heading* - The heading of the PSM.

[ProcessedPsm schema can be found here.](<jpo-geojsonconverter/src/main/resources/schemas/processed-psm.schema.json>)

Example ProcessedPsm message:

```json
{
	"type": "Feature",
	"geometry": {
		"type": "Point",
		"coordinates": [
			-74.27614369999999,
			40.2397377
		]
	},
	"properties": {
		"schemaVersion": 2,
		"messageType": "PSM",
		"odeReceivedAt": "2025-07-25T10:09:36.120Z",
		"timeStamp": "2025-07-25T10:09:03.564Z",
		"originIp": "172.18.0.1",
		"asn1": "00201A0000021BD86891DE75F84DA101C13F042E2214141FFF00022C2000270000000163B2CC79860100",
		"validationMessages": [],
		"basicType": "aPEDESTRIAN",
		"id": "24779D7E",
		"msgCnt": 26,
		"secMark": 3564,
		"speed": 0.0,
		"heading": 111.22500000000001
	}
}
```

### ProcessedRtcm

The GeoJSON Converter produces `ProcessedRtcm` objects from `RTCMcorrections` message frames recieved from the ODE.

Validation with reference to the [CTI-4501 specification](https://www.ite.org/ITEORG/assets/File/Standards/CTI%204501v0101.pdf) is performed as required by the Conflict Monitor (CIMMS).

The RTCM decoding functionality can be configured with the `rtcm.full.decode` setting in `application.yaml`.

* If `rtcm.full.decode=true`, the app uses the native `gpsd-client` library to fully decode the RTCM payloads.
* If `rtcm.full.decode=false`, it uses pure Java methods to partially decode the payload.  In partial mode it is only capable of extracting the message types and station IDs for RTCM rev 3 messages.

`ProcessedRtcm` fields:

* *type* - Always 'Feature'
* *geometry* - Point geometry, location of the station from the `FullPositionVector` frame.
* *properties* - Selected properties from the message frame, and decoded from the binary RTCM messages, including:
  * *msgCnt* - Message count
  * *rev* - Must equal "rtcmRev3" to be CTI-4501 compliant, although it is possible to decode rev 2 messages in full decode mode.
  * *messageTypes* - A list of message types from the decoded messages.
  * *stationId* - The station ID from the decoded messages.
  * *messages* - A list of fully or partially decoded messages.  
    * *hex* - The hex-encoded raw RTCM message.
    * *decodedMessage* - The decoded message in JSON format. There are numerous message types, and the structure of this node varies depending on type.

Example `ProcessedRtcm` message:

```json
{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [
      -111.50943489999999,
      40.660336
    ]
  },
  "properties": {
    "schemaVersion": 1,
    "messageType": "RTCM",
    "odeReceivedAt": "2025-07-28T20:34:11.033Z",
    "originIp": "172.18.0.1",
    "asn1": "001C6D25244028D2D4B29BC25EC0C12C418D300133ED980037BDD1A80C6358121DD4E499FFC6712E91F0D10F4C00F90266005105115939553131053951153939048081393D391400003551492535093114810531313D64513985D880D4B8D0D080BC8109BDBDD080D4B8D0D0023197E4",
    "msgCnt": 82,
    "rev": "rtcmRev3",
    "longitude": -111.50943489999999,
    "latitude": 40.660336,
    "elevation": 2063.0,
    "messageTypes": [
      1005,
      1033
    ],
    "stationId": 2432,
    "messages": [
      {
        "hex": "D300133ED980037BDD1A80C6358121DD4E499FFC6712E91F0D",
        "decodedMessage": {
          "class": "RTCM3",
          "device": "stdin",
          "type": 1005,
          "length": 19,
          "station_id": 2432,
          "system": [
            "GPS",
            "GLONASS"
          ],
          "refstation": true,
          "sro": false,
          "x": -1776533.4842,
          "y": -4507816.005,
          "z": 4133882.4466
        }
      },
      {
        "hex": "D3003E409980144144564E554C4C414E54454E4E4120204E4F4E4500000D5452494D424C4520414C4C4F59144E617620352E3434202F20426F6F7420352E3434008C65F9",
        "decodedMessage": {
          "class": "RTCM3",
          "device": "stdin",
          "type": 1033,
          "length": 62,
          "station_id": 2432,
          "desc": "ADVNULLANTENNA  NONE",
          "setup_id": 0,
          "serial": "",
          "receiver": "TRIMBLE ALLOY",
          "firmware": "Nav 5.44 / Bo"
        }
      }
    ],
    "validationMessages": [
      {
        "message": "CTI-4501 conformance issue: anchorPoint (DF_FullPositionVector) 'utcTime' field: DDateTime is missing."
      }
    ],
    "cti4501Conformant": false
  }
}
```

### ProcessedSrm

The GeoJSON Converter produces `ProcessedSrm` messages from `SignalRequestMessage` (SRM) message frames received from the ODE.

The SRMs are validated with the J2735 JSON schema.  CTI-4501 doesn't have any guidelines for SRMs, so no additional validation is performed beyond J2735 conformance.

SRMs are similar to BSMs in that they are sent from a vehicle and can include the vehicle's location. Unlike BSMs, the location is optional in SRMs, but if it is present it is used as the point geometry of a GeoJSON feature.

It is possible for one SRM to contain requests for multiple lanes at one intersection, or to target more than one intersection, so ProcessedSrm data structures contain a list of requests. We have observed actual data with multiple requests for different lanes in CDOT's installation. The processed structure retains all the requests in the SRM, even if there happened to be requests for more than one intersection.

The Kafka messages for ProcessedSrm use a key containing the RSU ID and VehicleID. The intersection ID and region are not included in the key because of the possibility of a message targeting more than one intersection.

Example `ProcessedSrm` message:

```json
{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [
      -105.0851191,
      39.5532496
    ]
  },
  "properties": {
    "schemaVersion": 1,
    "messageType": "SRM",
    "odeReceivedAt": "2025-09-26T00:46:10.771Z",
    "originIp": "172.18.0.1",
    "asn1": "001D2F72DC028000050890BD481080201F1A62242F5205200408430AB02F236614C002D4D3841D02CA71A8851970D51C3060",
    "timeStamp": "2025-09-18T06:29:00Z",
    "sequenceNumber": 5,
    "vehicleID": "2031825062",
    "role": "PUBLICTRANSPORT",
    "latitude": 39.5532496,
    "longitude": -105.0851191,
    "elevation": 1679.1000000000001,
    "heading": 21.3,
    "transmission": "UNAVAILABLE",
    "speedMetersPerSecond": 7.74,
    "requests": [
      {
        "intersectionId": 12114,
        "requestID": 4,
        "priorityRequestType": "PRIORITYREQUEST",
        "inboundLaneID": 2,
        "outboundLaneID": 15,
        "estimatedTimeOfArrivalDurationSeconds": 36.145000000
      },
      {
        "intersectionId": 12114,
        "requestID": 5,
        "priorityRequestType": "PRIORITYREQUEST",
        "inboundLaneID": 1,
        "outboundLaneID": 16,
        "estimatedTimeOfArrivalDurationSeconds": 34.325000000
      }
    ],
    "validationMessages": []
  }
}
```


### ProcessedSsm

The GeoJSON Converter produces `ProcessedSsm` messages from `SignalStatusMessage` (SSM) message frames received from the ODE.

The SSMs are validated against the J2735 JSON schema.  CTI-4501 doesn't have any guidelines for SSMs, so no additional validation is performed beyond J2735 conformance.

SSMs are similar to SPATs in that they don't have a geometry of their own but must be matched with a corresponding MAP message to obtain location data. Therefore, ProcessedSsm objects are simple POJOs, not GeoJSON.

Also, like SPATs, SSMs can contain groups of messages for more than one intersection, but it is anticipated that it is unlikely that they actually would, because they are broadcast from RSUs located at one intersection. Therefore, this code takes only the first intersection from any SSM it receives, and logs a warning if an SSM contains more than one intersection. This is the same as how SPATs are treated.

It also would be possible for SSMs to contain responses for more than one vehicle, or for requests for more than one lane for a vehicle. We have not seen examples of SSMs with multiple statuses in a cursory check of one day of data from CDOT, but this scenario seems more likely that it could happen according to the language in the J2735 where SSMs are defined, which describes them as collections of statuses targeted for multiple vehicles and lanes. For this reason, the ProcessedSsm data structure contains a list of statuses.

The Kafka messages for ProcessedSsm use RSU ID and Intersection in the key because they are associated with only one intersection.

Example `ProcessedSsm` message:

```json
{
  "schemaVersion": 1,
  "messageType": "SSM",
  "asn1": "001E2565B8056D601800E8BD482C95E46CC2981028200807C30A9325791B30A6050A08010210C2A4",
  "odeReceivedAt": "2025-09-26T00:55:06.425Z",
  "originIp": "172.18.0.1",
  "timeStamp": "2025-09-18T06:29:28Z",
  "sequenceNumber": 12,
  "statusSequenceNumber": 29,
  "intersectionId": 12114,
  "statusList": [
    {
      "vehicleID": "2031825062",
      "requestID": 4,
      "requesterSequenceNumber": 5,
      "requesterRole": "PUBLICTRANSPORT",
      "inboundOnLaneID": 2,
      "outboundOnLaneID": 15,
      "estimatedTimeOfArrivalDurationSeconds": 34.325000000,
      "status": "PROCESSING"
    },
    {
      "vehicleID": "2031825062",
      "requestID": 5,
      "requesterSequenceNumber": 5,
      "requesterRole": "PUBLICTRANSPORT",
      "inboundOnLaneID": 1,
      "outboundOnLaneID": 16,
      "estimatedTimeOfArrivalDurationSeconds": 34.325000000,
      "status": "PROCESSING"
    }
  ],
  "validationMessages": []
}
```


[Back to top](#toc)

<!--
#########################################
############# Configuration #############
#########################################
 -->

<a name="configuration"/>

## 2. Configuration

### System Requirements

Recommended machine specs running Docker to run the GeoJsonConverter:

- Minimum RAM: 16 GB
- Minimum storage space: 100 GB
- Supported operating systems:
  - Ubuntu 20.04 Linux (Recommended)
  - Windows 10/11 Professional (Professional version required for Docker virtualization)
  - OSX 10 Mojave

The GeoJsonConverter software can run on most standard Window, Mac, or Linux based computers with
Pentium core processors. Performance of the software will be based on the computing power and available RAM in
the system.  Larger data flows can require much larger space requirements depending on the
amount of data being processed by the software. The GeoJsonConverter software application was developed using the open source programming language Java. If running the GeoJsonConverter outside of Docker, the application requires the Java 21 runtime environment.

### Software Prerequisites

The GeoJsonConverter is a micro service that runs as an independent application but serves the sole purpose of converting JSON objects created by the JPO-ODE via Apache Kafka. To support these JSON objects, the GeoJsonConverter application utilizes some classes from the JPO-ODE. These classes are referenced in the GeoJsonConverter by pulling the built `.jar` artifact from GitHub Maven Central. The JPO-ODE is still required to launch Kafka, Zookeeper, the ASN1 decoder and create the required Kafka topics. All other required dependencies will automatically be downloaded and installed as part of the Docker build process.

- Docker: <https://docs.docker.com/engine/installation/>
- Docker-Compose: <https://docs.docker.com/compose/install/>

### Tips and Advice

Read the following guides to familiarize yourself with GeoJsonConverter's Docker and the ODE managed Kafka modules.

- [Docker README](docker.md)
- [ODE Kafka README](https://github.com/usdot-jpo-ode/jpo-ode/blob/dev/kafka.md)

**Installation and Deployment:**

- Docker builds may fail if you are on a corporate network due to DNS resolution errors.
- Windows users may find more information on installing and using Docker [here](https://github.com/usdot-jpo-ode/jpo-ode/wiki/Docker-management).
- Users interested in Kafka may find more guidance and configuration options [here](docker/kafka/README.md).

**Configuration:**

The GeoJsonConverter configuration is customized through the environment variables provided to Docker when Docker-Compose runs the Docker built GeoJsonConverter image. You may customize the Kafka broker endpoint.

**Important!**
You must rename `sample.env` to `.env` in both the [root directory](sample.env) and in [jpo-utils](jpo-utils/sample.env) for Docker to automatically read the file. Do not push this file to source control.

[Back to top](#toc)

<!--
########################################
############# Installation #############
########################################
 -->

<a name="installation"/>

## 3. Installation

The following instructions describe the minimal procedure to fetch, build, and run the main GeoJsonConverter application.

#### Step 0 - For Windows Users Only

If running on Windows, please make sure that your global git config is set up to not convert end-of-line characters during checkout.

Disable `git core.autocrlf` (One Time Only)

```bash
git config --global core.autocrlf false
```

#### Step 1 - Download the Source Code

Note: to avoid cloning redundant local copies of submodules, if you intend to run the entire ConflictVisualizer system, you should instead start with [step 1 of the ConflictVisualizer Installation Guide](https://github.com/usdot-jpo-ode/jpo-conflictvisualizer?tab=readme-ov-file#1-initialize-and-update-submodules) before returning to step 2 below.

The jpo-geojsonconverter software system consists of the following modules hosted in separate Github repositories:

|Name|Visibility|Description|
|----|----------|-----------|
|[jpo-geojsonconverter](https://github.com/usdot-jpo-ode/jpo-geojsonconverter)|public|Contains the public components of the application code.|
|[jpo-ode](https://github.com/usdot-jpo-ode/jpo-ode)|public|Contains the public classes and libraries of the jpo-ode used in the GeoJsonConverter.|

You may download the stable, default branch for ALL of these dependencies by using the following recursive git clone command:

```bash
git clone --recurse-submodules https://github.com/usdot-jpo-ode/jpo-geojsonconverter.git
```

Once you have these repositories obtained, you are ready to build and deploy the application.

#### Step 2 - Build and run jpo-ode application

Navigate to the root directory of the jpo-ode project and run the following command:

```bash
docker-compose up --build -d
docker-compose ps
```

Verify the jpo-ode, kafka, zookeeper, asn1-decoder and asn1-encoder are running before performing step 3.

#### Step 3 - Generate GitHub Token

A GitHub token is required to pull artifacts from GitHub repositories. This is required to obtain the jpo-ode jars and must be done before attempting to build this repository.

1. Log into GitHub.
2. Navigate to Settings -> Developer settings -> Personal access tokens.
3. Click "New personal access token (classic)".
   1. As of now, GitHub does not support `Fine-grained tokens` for obtaining packages.
4. Provide a name and expiration for the token.
5. Select the `read:packages` scope.
6. Click "Generate token" and copy the token.
7. Copy the token name and token value into your `.env` file.
8. Create a copy of [settings.xml](jpo-geojsonconverter/settings.xml) and save it to `~/.m2/settings.xml`
9. Update the variables in your `~/.m2/settings.xml` with the token value and target jpo-ode organization. Here is an example filled in `settings.xml` file:

```XML
<?xml version="1.0" encoding="UTF-8"?>
<settings>
    <activeProfiles>
        <activeProfile>default</activeProfile>
    </activeProfiles>
    <servers>
        <server>
            <id>github</id>
            <username>jpo_geojsonconverter</username>
            <password>ghp_token-string-value</password>
        </server>
    </servers>
    <profiles>
        <profile>
            <id>default</id>
            <repositories>
                <repository>
                    <id>github</id>
                    <name>GitHub Apache Maven Packages</name>
                    <url>https://maven.pkg.github.com/usdot-jpo-ode/jpo-ode</url>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

#### Step 4 - Build and run jpo-geojsonconverter application

**Notes:**

- Docker builds may fail if you are on a corporate network due to DNS resolution errors.
- In order for Docker to automatically read the environment variable file, you must rename it from `sample.env` to `.env`. **This file will contain private keys, do not put add it to version control.**. A copy of the `sample.env` file must be created in [jpo-utils](jpo-utils/sample.env) in order for the base applications (kafka, mongo, etc.) to start up correctly
- Unless you intend to run geojsonconverter without jpo-ode, replace the contents of docker-compose.yml with those of docker-compose-standalone.yml.

Navigate to the root directory of the jpo-geojsonconverter project and run the following command:

```bash
docker-compose up --build -d
docker-compose ps
```

To bring down the services and remove the running containers run the following command:

```bash
docker-compose down
```

For a fresh restart, run:

```bash
docker-compose down
docker-compose up --build -d
docker-compose ps
```

To completely rebuild from scratch, run:

```bash
docker-compose down
docker-compose rm -fvs
docker-compose up --build -d
docker-compose ps
```

## Environment variables

### Purpose & Usage

#### Runtime Environmental Variables

These variables are required for the image during runtime:

- The `DOCKER_HOST_IP` environment variable is used to communicate with the bootstrap server that the instance of Kafka is running on.
- The `GEOMETRY_OUTPUT_MODE` environmental variable is used to enable the Processed Map topology to create Well Known Text formatted messages on the `topic.ProcessedMapWKT` topic. Options are `GEOJSON_ONLY` or `WKT`, if the variable is not set it will default to `GEOJSON_ONLY`.
-

#### Image Building Environmental Variables

These variables are passed into the Docker image as build arguments and allow for pulling the jpo-ode `.jar` files from GitHub Maven Central.

- The `MAVEN_GITHUB_TOKEN` environment variable is the value of the generated token from xxx used for pulling the jpo-ode java image.
- The `MAVEN_GITHUB_ORG` environment variable is the name of the GitHub organization to use for the jpo-ode repository.

### Values

In order to utilize Confluent Cloud:

- DOCKER_HOST_IP must be set to the bootstrap server address (excluding the port)

- `KAFKA_TYPE` must be set to "CONFLUENT"
- `CONFLUENT_KEY` must be set to the API key being utilized for CC
- `CONFLUENT_SECRET` must be set to the API secret being utilized for CC

[Back to top](#toc)

<!--
#########################################
############# File Manifest #############
#########################################
 -->

<a name="file-manifest"/>

## 4. File Manifest

This section outlines the software technology stacks of the GeoJsonConverter.

### Containerization and Management

- [Docker](https://www.docker.com/)
- [Docker-Compose](https://docs.docker.com/compose/)

### Messaging

- [Kafka](https://kafka.apache.org/)

### ODE Code

- [Java](https://openjdk.java.net/)
- [Maven](https://maven.apache.org/)
- [Spring Boot](http://spring.io/projects/spring-boot)
- [Logback](https://logback.qos.ch/)
- [SNMP4J](https://www.snmp4j.org/)
- [JUnit](https://junit.org)
- [JMockit](http://jmockit.github.io/)

[Back to top](#toc)

<!--
#############################################
############# Development Setup #############
#############################################
 -->

<a name="development-setup"/>

## 5. Development Setup

### Integrated Development Environment (IDE)

Install the IDE of your choice:

- Eclipse: [https://eclipse.org/](https://eclipse.org/)
- STS: [https://spring.io/tools/sts/all](https://spring.io/tools/sts/all)
- IntelliJ: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)

### Regenerating JSON Schemas

The project includes JSON schemas for validating processed messages (ProcessedMap, ProcessedSpat, ProcessedBsm, ProcessedPsm). These schemas are automatically generated from the Java POJOs using the SchemaGeneratorUtility.

To regenerate the schemas:

1. Using VS Code:
   - Open the project in VS Code
   - Go to the Run and Debug view
   - Select "SchemaGeneratorUtility" from the dropdown
   - Click the play button or press F5

2. Using command line:

   ```bash
   cd jpo-geojsonconverter
   mvn exec:java -Dexec.mainClass="us.dot.its.jpo.geojsonconverter.utils.SchemaGeneratorUtility" -Dexec.args="--output src/main/resources/schemas"
   ```

The schemas will be generated in `jpo-geojsonconverter/src/main/resources/schemas/`.

### Continuous Integration

- TravisCI: <https://travis-ci.org/usdot-jpo-ode/jpo-ode>

[Back to top](#toc)

<!--
###############################################
############# Contact Information #############
###############################################
 -->

<a name="contact-information"/>

## 6. Contact Information

Contact the developers of the GeoJsonConverter application by submitting a [Github issue](https://github.com/usdot-jpo-ode/jpo-geojsonconverter/issues).

### License information

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
file except in compliance with the License.
You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>
Unless required by applicable law or agreed to in writing, software distributed under
the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied. See the License for the specific language governing
permissions and limitations under the [License](http://www.apache.org/licenses/LICENSE-2.0).

[Back to top](#toc)

<!--
########################################
############# Contributing #############
########################################
 -->

<a name="contributing"/>

## 7. Contributing

Please read our [contributing guide](docs/contributing_guide.md) to learn about our development process, how to propose pull requests and improvements, and how to build and test your changes to this project.

### Source Repositories - GitHub

- Main repository on GitHub (public)
  - <https://github.com/usdot-jpo-ode/jpo-geojsonconverter>
- USDOT ITS JPO ODE on Github (public)
  - <https://github.com/usdot-jpo-ode/jpo-ode>

[Back to top](#toc)
