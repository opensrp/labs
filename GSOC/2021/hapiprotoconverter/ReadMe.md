# Google Summer of Code 2021 Work Product

**Student** : [Aditya Kurkure](https://github.com/epicadk)

**Project** : [HAPI Struct <> Proto Conversion Tool](https://summerofcode.withgoogle.com/projects/#6418581241200640)

**Organization** : [Google FHIR SDK](https://summerofcode.withgoogle.com/organizations/6201077587771392)

**Mentors** : [Richard Kareko](https://github.com/Rkareko)

*NOTE* : We are currently in the process of migrating the project [here](https://github.com/epicadk/hapi-proto-converter).

## HAPI Struct <> Proto Conversion Tool
The purpose of this project is to implement the HAPI <> Proto converter, a library that converts between Hapi structs and Fhir protos. The Structures defined in the FHIR specification can be broadly categorized into two parts Resources and Base Types. The resources represent the medical data that is to be stored/shared. For example, a patient resource contains data related to a particular patient like their name, address, contact info, etc. Base  types define how this data is stored, for example, a string for name and date for DOB. 
The base types can be further classified into Data types and Primitive types. Primitive Types Cannot be broken down into simpler types. Therefore each Data type and Resource are ultimately a collection of primitive types. The basic idea therefore would be to break down these Resources and Data types until we reach primitive types, convert these primitive types to HAPI / FHIR form. Reconstruct the types.


**Issues** :[Add functionality to convert between composite types](https://github.com/google/android-fhir/issues/741) , [Feat : Add functionality to convert Hapi resources to Fhir protos (JSON)](https://github.com/google/android-fhir/issues/556), [Feat : Add functionality to convert between primitive datatypes](https://github.com/google/android-fhir/issues/559)

**Pull requests** : [Convert composite Types](https://github.com/google/android-fhir/pull/680), [Primitive converter poet](https://github.com/google/android-fhir/pull/604), [adds functionality to converter between resources using Json representation](https://github.com/google/android-fhir/pull/557), [feat: initialize hapi<>proto converter module](https://github.com/google/android-fhir/pull/540), [add functionality to convert between primitive types (reflection)](https://github.com/google/android-fhir/pull/560), [feat: convert between enums](https://github.com/google/android-fhir/pull/573)

**Feature Branch**: [Hapi Proto Converter](https://github.com/google/android-fhir/tree/gsoc/hapi-proto-converter)

### Work Done During GSoC 
- Create library that provides functionlity to convert between hapi structs and fhir protos using JSON representation
- Create library that provides functionality to convert between hapi structs and fhir protos using Code generation
- Comparing both the approaches
- Additional work on the Android FHIR SDK

### TODO's 
- Proper performance testing
-	DomainResource.Contained element -> Handled differently in Fhir protos and Hapi structs
-	XHTML type elements -> Handled differently in Fhir protos and Hapi structs
-	Extensions on Primitive types (Done manually currently) -> Will be fixed with gradle task automation
-	Resource type elements -> Handled differently in Fhir protos and Hapi structs

## The Apporaches 
**1.	Serializing- deserializing JSON**

Hapi Structures as well as FHIR protos can be converted to JSON representation. The general approach would be to serialize the HAPI struct to JSON representation followed by parsing the JSON into a proto object ( and vice versa).

| **Pros**                              | **Cons**                                                                         |
| ------------------------------------- | ---------------------------------------------------------------------------- |
| Natively supported by both libraries  | Serializing- deserializing will require the intermediate JSON representation |
| Estimated effort will be the least.   | Can only convert a complete resource and not primitive / composite types.    |
| Implementation will be fairly simple. |                                                                              |

![g1](https://user-images.githubusercontent.com/56596662/130222169-60a7606c-3b0e-4d7d-8120-0a8f3cc9cd1b.png)


**2.	Java reflection API**

Using Java reflection, we can get Type information on the Hapi struct at runtime. Using this information, we can build the corresponding fhir proto-object.
	The Process is similar to that of Code generation, the only difference is this will take place at runtime and in code generation it would take place at compile time.
| **Pros**                                                                                                       | **Cons**                                                                              |
| -------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| Size of the final library would be less than that of Code generation.  (but still larger than JSON converter). | Slower than code generation. ([Moshi](https://github.com/square/moshi) as an example)                                    |
| Would not involve converting to an intermediate state.                                                       | Possibility of runtime errors (as opposed to compile time errors in code generation). |
| Can convert a resource as well as base types                                                                   |                                                                                       |	
	
	
  
**3.	Code Generation**

Using code generation, we can create converters that would map each element in the HAPI struct to the corresponding element in the Fhir proto. This can be done using the structure definition file defined in the fhir spec, which would give us the type information at compile time.

| **Pros**                                                                               | **Cons**                                                                                     |
| -------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| The errors generated due to incorrect mapping would be at compile time rather runtime. | Size of the final library would be the largest between the 3 approaches.                     |
| Would not involve converting to an intermediate state.                             | Would have to infer all properties from the structure definition file.                       |
| Can convert a resource as well as base types                                           | Harder to maintain, as converter can break if there are changes in the Proto or HAPI library. |

![g2](https://user-images.githubusercontent.com/56596662/130222302-fe77644d-b164-4e73-9db7-0481248ced6a.png)

### A brief description of the elements handled in code generation
The elements were broading categorized into 2 parts :-
1. Primitive types
2. Composite types
 - Backbone elements types
 - Code types (Enums)
 - Elements with a content reference rather than a type
 - Choice type
 - Other types

The types and whether or not it is repeated can be easily infered from the structure definition file. The challenging part was getting the naming convention correct. However since both the Hapi struct codegen and Proto codegen are open sourced makes this somewhat easier.

## Performance testing Results 

For testing the relative performance of the approaches, I converted a 1000 patient resources from hapi to proto and vice versa.

All Values are in milliseconds.

|                             | JSON          |      |		 Codegen |	|	
| --------------------------- | ------------- | ------------- | ----------| ------------| 
|                             | Hapi to Proto | Proto to Hapi | Hapi to Proto | Proto to Hapi |
| **First conversion**            | 391           | 1134          | 705 | 281 |
| **100 conversions**             | 887           | 1584          | 823 | 328 |
| **1000 conversions**            | 3285          | 3231          | 1205 | 500 |
| **Excluding first conversions** | 2894          | 2097          | 500 | 219 |


## Size comparision between FHIR protos and Hapi Structs

| No of files | Hapi JSON | Proto Bytes | Proto String |
| ----------- | --------- | ----------- | ------------ |
| 1           | 2.41 kb   | 761 b       | 2.90 kb      |
| 100         | 243 kb    | 75 kb       | 293 kb       |
| 1000        | 2.35 mb   | 743 kb      | 2.83 mb      |
| 1,00,000    | 235 mb    | 72.5 mb     | 591 mb       |

## Sample Code
### Hapi resource to FHIR Proto Function Signature
	inline fun <reified T : Resource> convert(
	  resource:  GeneratedMessageV3,
	  hapiParser: IParser,
	  protoPrinter: JsonFormat.Printer
	): T

### FHIR Proto to Hapi resource Function Signature
	inline fun <reified T : Resource> convert(
	  resource:  GeneratedMessageV3,
	  hapiParser: IParser,
	  protoPrinter: JsonFormat.Printer
	): T


### To convert a Proto Patient to a Hapi Patient
`protoPatient.toHapi()`
### To convert a Hapi Patient to a Proto Patient  
`hapiPatient.toProto()`


## Use cases

- Protobufs have a relatively smaller size and hence are suitable for network and database operations.
- Hapi structures have a wide variety of use cases, for example the FHIR path engine and the CQL engine.
- The current HAPI fhir server could be modified to accept and store data in the FHIR proto format.
- The Cloud Health Care API (GCP) can introduce new functionality such as evaluation of CQL. (however this project isn't open source)

## A few design decisions explained.

1. **Why the Kotlin/ Kotlin poet library?**

The alternatives to this are [Byte Buddy](https://bytebuddy.net/#/) , [Java poet](https://github.com/square/javapoet). Byte buddy generates the classes at runtime and thus would not fit our use case. I chose kotlin poet over java poet because the android fhir library uses kotlin. Post GSoC I do plan on generating code in java.

2. **Why Object notation over Top level functions?**

While the recommended way is to use Top level functions the library currently uses object notation. There is a heap space error while trying to compile the library while using top level functions. I had attempted a few rudimentary fixes however they don't fix the problem.

## Useful resources
- [KotlinConf 2017 - Generating Kotlin Code by Alec Strong and Jake Wharton](https://www.youtube.com/watch?v=_obNBSldffw)
- [What code generation tools are recommended?](https://discuss.kotlinlang.org/t/what-code-generation-tools-are-recommended/8351)
