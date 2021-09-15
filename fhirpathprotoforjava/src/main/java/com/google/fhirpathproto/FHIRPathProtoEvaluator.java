package com.google.fhirpathproto;

import com.google.fhir.r4.core.MessageHeader;
import com.google.fhir.shaded.protobuf.InvalidProtocolBufferException;
import com.google.fhir.shaded.protobuf.Message;
import com.google.fhir.shaded.protobuf.MessageOrBuilder;
import com.google.fhirpathproto.JsonFormatBase.FileType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.UcumException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.JsonParser;

import org.hl7.fhir.r4.model.ExpressionNode;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.utilities.Utilities;

/**
 * Class to evaluate FHIRPath expressions on FHIRProto resources
 * @author DeeproChoudhury
 *
 */
public class FHIRPathProtoEvaluator {

  private static FHIRPathEngine fhirPathEngine;
  private final Map<String, Resource> resources = new HashMap<>();

  /**
   * Evaluates a FhirPath Expression for a prototxt file
   * @param protoTxt The prototxt file which the FHIRPath expression is meant to be used on
   * @param expressionString The FHIRPath expression as a string
   * @param builder The builder of the resource type in the file
   * @param <T> Any type of resource builder
   * @return A collection of FHIRTypes which is the result of the FHIRPath expression
   * @throws IOException If the file is not found
   */
  public <T extends Message.Builder> List<Base> 
  evaluate(File protoTxt, String expressionString, T builder) throws IOException, FHIRException {
    String json = new JsonFormatBase().parseToJson(protoTxt, builder);
    return processJSON(json, expressionString);
  }

  /**
   * Evaluates a prototxt file in the jsonFormatBase file directory from just the filename
   * @param fileName The name of the file to be evaluated, no extension required
   * @param expressionString The FHIRPath expression
   * @param builder The builder of the resource type in the file
   * @param jsonFormatBase The class with the specified examples directory
   * @param <T> Any type of resource builder
   * @return A collection of FHIRTypes which is the result of the FHIRPath expression
   * @throws IOException If the file is not found
   */
  public <T extends Message.Builder> List<Base> 
  evaluateProtoTxtFileName(String fileName, String expressionString, T builder,
      JsonFormatBase jsonFormatBase) throws IOException, FHIRException {
    File file = jsonFormatBase.getExampleFile(fileName, FileType.PROTOTXT);
    return evaluate(file, expressionString, builder);
  }

  public <T extends Message.Builder> List<Base> 
  evaluate(String protoTxt, String expressionString, T builder) throws IOException, FHIRException {
    String json = new JsonFormatBase().parseToJson(protoTxt, builder);
    return processJSON(json, expressionString);
  }

  public List<Base> evaluateBinary(File protoBinary, String expressionString)
      throws IOException, FHIRException {

    InputStream binaryInputStream = new FileInputStream(protoBinary);

    MessageHeader resource = MessageHeader.parseFrom(binaryInputStream);
    
    JsonFormatBase jsonFormatBase = new JsonFormatBase();

    String json = jsonFormatBase.parseToJson(resource);
    
    List<Base> result = processJSON(json, expressionString);

    return result;
  }

  /**
   *
   * @param protoBinary The binary file to be evaluated
   * @param expressionString The FHIRPath expression
   * @param messageOrBuilder A resource or a builder type
   * @param <T> Any type of Message or a builder, must be the same type of resource as in the file
   * @return A collection of FHIRTypes
   * @throws IOException If parsing to JSON or processing JSON fails
   */
  public <T extends MessageOrBuilder> List<Base> evaluateBinaryResource(File protoBinary,
      String expressionString, T messageOrBuilder) throws IOException, FHIRException {
    
    InputStream binaryInputStream = new FileInputStream(protoBinary);
    
    var resource = messageOrBuilder.
        getDefaultInstanceForType().getParserForType().parseFrom(binaryInputStream);

    String json = new JsonFormatBase().parseToJson(resource);

    List<Base> result = processJSON(json, expressionString);
    
    return result;
  }

  /**
   * Evaluates a FHIRPath expression on proto binary data expressed a byte array
   * @param protoBinary The proto binary expressed as a byte array
   * @param expressionString The FHIRPath expression as a string
   * @param messageOrBuilder An extension of a message or builder of the same type as expressed
   *                         in the proto binary data
   * @param <T> An extension of messageOrBuilder - any type of resource
   * @return The collection given by the evaluation of the FHIRPath expression on this specific
   * proto binary resource
   * @throws IOException If parsing JSON fails
   * @throws FHIRException If the lexing of the expression and resource fails
   */
  public <T extends MessageOrBuilder> List<Base> evaluateBinaryResource(byte[] protoBinary,
      String expressionString, T messageOrBuilder) throws IOException, FHIRException {

    Message resource =
        messageOrBuilder.getDefaultInstanceForType().getParserForType().parseFrom(protoBinary);

    String json = new JsonFormatBase().parseToJson(resource);

    List<Base> result = processJSON(json, expressionString);

    return result;
  }

  /**
   * Evaluates a FHIRPath expression on a JSON resource
   * @param json The JSON resource as a string
   * @param expression The FHIRPath expression as a string
   * @return A collection of FHIRTypes
   * @throws IOException If parsing JSON fails
   * @throws UcumException If FHIR Parsing fails
   */
  public List<Base> processJSON(String json, String expression)
      throws IOException, FHIRException {
    
    SimpleWorkerContext simpleWorkerContext = new SimpleWorkerContext();

    fhirPathEngine = new FHIRPathEngine(simpleWorkerContext);

    String input = json;


    Resource res = null;


    List<Base> outcome = new ArrayList<Base>();

    ExpressionNode node = null;



    try {
      node = fhirPathEngine.parse(expression);
    } catch (Exception e) {
      System.out.println("Parsing Error");
    }

    if (node != null) {
      try {
        if (Utilities.noString(input)) {
          fhirPathEngine.check(null, null, node);
        } else {
          res = resources.get(input);
          if (res == null) {
            res = new JsonParser().parse(input);
            resources.put(input, res);
          }
        }
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }

    if (node != null) {
      try {
        outcome = fhirPathEngine.evaluate(res, node);
      } catch (Exception e) {
        System.out.println("Execution Error" + e.getMessage());
      }

    }

    System.out.println(outcome);

    return outcome;


  }

}
