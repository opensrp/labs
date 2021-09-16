<img src="https://user-images.githubusercontent.com/62053304/128657744-924b1f39-05cd-46a9-8f4d-2412ec6f38af.png"
     align="center"/>
     
# Google Summer of Code 2021 Work Product

**Student**: [Deepro Choudhury](https://github.com/DeeproChoudhury)

**Project**: [FHIRPath for FHIRProto Library](https://summerofcode.withgoogle.com/projects/#5361978601635840)

**Organisation**: [Google FHIR SDK](https://summerofcode.withgoogle.com/organizations/6201077587771392)

**Mentor**: [Martin Ndegwa](https://github.com/ndegwamartin)

[Documentation](https://docs.google.com/document/d/15aZA2okP8YaZEReUMIKdn5QHyAK-qih3CVZ7kenJJxQ/)

## FHIRPath

FHIRPath is a path-based graph-traversal extraction language somewhat similar to XPath. Its main purpose is to navigate and extract items from FHIR resources, for which the language is optimised. However, FHIRPath is model-independent, which means that in theory it can work on any graph-based structure. 

The best way to think about FHIRPath is to represent the data as a tree of labelled nodes.
<img src="https://user-images.githubusercontent.com/72229274/130718396-75de1651-77f8-4ca0-8d60-af1fb28d7340.png"
     align="center"/>
     
FHIRPath expressions are evaluated with respect to a specific instance or resource, for example the `Patient` resource here. This instance is called the _context_ or, rather fittingly, the _root_.

FHIRPath's main navigation model is through the tree by using path selection - composing a path of concatenated labels. For example, `name.given` will search through all `name` nodes in the tree, and return a collection composed of all the `given` nodes descending from them. 

FHIRPath also allows the fast formulation of _invariants_ which act like filters on the structure.

More detailed documentation for FHIRPath is available [here](http://hl7.org/fhirpath/).

## FHIRPath for FHIRProto

The most common data serialization formats at the moment are JSON and XML, both of which are used by FHIR to encode data. FHIR currently only supports JSON, XML and RDF (Turtle). However, Google has created its own serialization format, protocol buffers, which can be the future of FHIR.

Protocol Buffers can be stored in 2 formats, Prototxt and Proto Binary. They both take up less storage space, protobinary significantly so - it is very concise. However, proto binaries are not human readable. This is where FHIRPath is most useful. Protobuf also allows you to define your own data structures.

The aim of this project was to get FHIRPath working for FHIR on protocol buffers, specifically
HL7 R4 FHIR.

## Work Done during GSoC
- Create library that allows FHIRPath to be used with protocol buffers.
- Provide functionality for FHIRPath with both `.prototxt` and `.proto` (binary) files.
- Provide functionality for both string and file inputs
- Allow conversion between `.proto`, `.prototxt` and JSON files

## To Do
- Package the library as a `.jar` file for use in other projects
- Implement native support for parsing protos
- Implement support for r5

## The Approach
![image](https://user-images.githubusercontent.com/72229274/130813755-0cbbdfb5-415f-4c17-965b-9f061dadcac1.png)

## Design Decisions
We decided to use this approach because it was the most efficient. We initially started by trying to implement a native protocol buffers engine for this project, however both my mentor and I agreed that this was not feasible during the period of GSoC so we went for an alternative approach which gave the same results.

In addition, when the HAPI FHIRPathEngine core library is updated, we just need to update the dependencies in the project.

## Functions and Use

The user may create a new instance of the `FHIRPathProtoEvaluator` class when they have the resource they want to evaluate ready. The user can input the proto resource in several ways - either as a `.prototxt` file, a `.proto` binary file, an array of multiple `.prototxt` or `.protobinary` files, or a `.prototxt` string. For each format the resource is in, there is an evaluation method inside the class which takes a proto resource in that format, along with the FHIRPath expression as a string. If the user wishes to put in one or more files, all they need to do is input the filename, and the methods will search for it in the working directory. The methods are also capable of creating new empty files if one does not exist.

The conversion between protos and JSON is implemented in 2 main classes, `JSONFormatBase` and `JSONFormatGenerate` (which extends `JSONFormatBase`). If the user wishes, they can create separate instances of those classes to convert their files manually between prototxt and proto binary, proto to JSON, etc. The `JSONFormatBase` class can be instantiated with no parameters in the constructor, in which case a default examples directory inside the repository will be used and the separate directories for the different types of files will keep their standard values. The user can also specify their own directories using an overloaded constructor which takes in string values for the folder of the JSON files, the folder of the ProtoTxt files and the folder of the binary files. 

### To evaluate a Proto binary resource
`FHIRPathProtoEvaluator.evaluateBinaryResource(String filename, String expressionString, T builder)`

### To evaluate a Prototxt resource
`FHIRPathProtoEvaluator.evaluate(String filename, String expressionString, T builder)`

`FHIRPathProtoEvaluator.evaluate(File file, String expressionString, T builder)`

`FHIRPathProtoEvaluator.evaluate(String prototxt, String expressionString, T builder)`

## State before and after project
Before this project, FHIRPath only worked on JSON and XML data, which was limiting. Now it can work with protocol buffers.

## Applications
- Google FHIR SDK could work on protocol buffers.
- Protocol buffers are very suitable for network and database operations.
- The current HAPI FHIR srver could accept proto data.

---
You can find more in the [documentation](https://docs.google.com/document/d/15aZA2okP8YaZEReUMIKdn5QHyAK-qih3CVZ7kenJJxQ/). You can also check out the [test classes here](https://github.com/google/android-fhir/tree/dc/fhir_path_proto_java/utilities/fhirpathprotoforjava/src/test/java).