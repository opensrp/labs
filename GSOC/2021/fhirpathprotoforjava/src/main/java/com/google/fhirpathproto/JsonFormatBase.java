package com.google.fhirpathproto;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.fhir.shaded.common.io.Files;
import com.google.fhir.shaded.protobuf.Message;
import com.google.fhir.common.JsonFormat;
import com.google.fhir.shaded.protobuf.MessageOrBuilder;
import com.google.fhir.shaded.protobuf.TextFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;

/**
* @author Deepro choudhury
*
*/
public class JsonFormatBase {

  protected JsonFormat.Parser jsonParser
      = JsonFormat.Parser.withDefaultTimeZone(ZoneId.systemDefault());

  protected TextFormat.Parser textParser = TextFormat.getParser();

  protected JsonFormat.Printer jsonPrinter = JsonFormat.getPrinter();
  
  protected final String examplesDir;

  protected String jsonDir = "/json/";

  protected String protoTxtDir = "/prototxt/";

  protected String protoBinaryDir = "/protobinary/";

  public String getExamplesDir() {
    return examplesDir;
  }

  public String getJsonDir() {
    return jsonDir;
  }

  public String getProtoTxtDir() {
    return protoTxtDir;
  }

  public String getProtoBinaryDir() {
    return protoBinaryDir;
  }

  public enum FileType {
      JSON,
      PROTOTXT,
      PROTOBINARY
  }

  public JsonFormatBase() {
    examplesDir = "/android-fhir/utilities/examplefiles";
  }
  /**
   *
   * @param examplesDir The directory in which the methods of this class should be searching and
   *                    creating files
   */
  public JsonFormatBase(String examplesDir) {
    this.examplesDir = examplesDir;
  }

  /**
   * Constructor of the class
   * @param examplesDir Directory in which the methods of this class are locating and creating files
   * @param jsonDir The directory in which the json files are, by default set to "/json". If there
   *                are no separate directories for the files, then this should be set to null
   * @param protoTxtDir The directory in which the Prototxt files are, by default set to
   *                    "/prototxt/". If there are no separate directories for the files,
   *                    then this should be set to null
   * @param protoBinaryDir The directory in which the Proto binary files are, by default set to
   *                       "/protobinary/". If there are no separate directories for the files,
   *                       then this should be set to null
   */
  public JsonFormatBase(String examplesDir, String jsonDir,
      String protoTxtDir, String protoBinaryDir) {
    this.examplesDir = examplesDir;
    this.jsonDir = jsonDir;
    this.protoTxtDir = protoTxtDir;
    this.protoBinaryDir = protoBinaryDir;
  }

  /**
   * Loads the json contents of a json file with the specified filename in the examplesDir
   * @param filename The name of the file to be searched for. The ".json" extension should
   *                 NOT be given
   * @return The contents of the JSON file as a string
   * @throws IOException If the file is not found
   */
  protected String loadJson(String filename) throws IOException {
    File file = getExampleFile(filename, FileType.JSON);
    return Files.asCharSource(file, UTF_8).read();
  }
  
  public String getProtoBinaryPath(String filename) {
    String pathName = getExamplesDir() + getProtoBinaryDir() + filename + ".proto";
    return pathName;
  }
  
  public String getProtoTxtPath(String filename) {
    String pathName = getExamplesDir() + getProtoTxtDir() + filename + ".prototxt";
    return pathName;
  }
  
  public String getJsonPath(String filename) {
    String pathName = getExamplesDir() + getJsonDir() + filename + ".json";
    return pathName;
  }

  /**
   * Fetches the file specified from the examples directory, or creates one if not found
   * @param filename The name of the file to be searched for
   * @param fileType The type of the file, which determines the extension to look for
   * @return The file if found, or a new file
   */
  public File getExampleFile(String filename, FileType fileType)  {
    String filePath;
    
    switch (fileType) {
      case JSON:
        filePath = getJsonPath(filename);
        break;
      case PROTOTXT:
        filePath = getProtoTxtPath(filename);
        break;
      case PROTOBINARY:
        filePath = getProtoBinaryPath(filename);
        break;
      default:
        throw new IllegalArgumentException("Invalid file type");
    }

    try {
      File resultFile = new File(filePath);
      return resultFile;
    } catch (Exception e) {
      System.out.println("There was no file with the filename you requested at " + getExamplesDir());
    }

    return null;
  }

  /**
   * Finds the file with the name specified and converts it into json, merging with the builder
   * specified
   * @param name The name of the file in the working directory
   * @param builder The builder of the resource contained in the file
   * @throws IOException If the file is not found
   */
  protected void parseToProto(String name, com.google.fhir.shaded.protobuf.Message.Builder builder)
      throws IOException {


    jsonParser.merge(loadJson(name), builder);


  }

  public String parseStringToProto(String json, Message.Builder builder) {
    jsonParser.merge(json, builder);

    return builder.toString();
  }

  /**
   * Creates a proto binary file from a .prototxt file and saves it in the working directory
   * @param protoTxtFile The name of the file to be searched for and converted - the extension should
   *                 NOT be given
   * @param builder A builder of the type of resource contained in the file
   * @return The file in proto binary format
   * @throws IOException If the file is not found
   */
  public File createProtoBinaryFile(String protoTxtFile, Message.Builder builder)
      throws IOException {
    
    File file = getExampleFile(protoTxtFile, FileType.PROTOTXT);

    textParser.merge(Files.asCharSource(file, UTF_8).read(), builder);

    File protoFile = getExampleFile(protoTxtFile, FileType.PROTOBINARY);

    builder.build()
        .writeTo(new FileOutputStream(protoFile));

    Message newMessage = builder.getDefaultInstanceForType().getParserForType()
        .parseFrom(new FileInputStream(protoFile));

    System.out.println(newMessage);
    
    return protoFile;
  }

  /**
   * Generates proto binary file from json input
   * @param jsonFilename Name of Json file to be parsed
   * @param builder Builder of the resource type of the json input
   * @return The proto binary file
   * @throws IOException
   */
  public File generateProtoBinary(String jsonFilename, Message.Builder builder)
      throws IOException {

    File file = getExampleFile(jsonFilename, FileType.JSON);

    jsonParser.merge(Files.asCharSource(file, UTF_8).read(), builder);

    File protoBinaryFile = getExampleFile(jsonFilename, FileType.PROTOBINARY);

    builder.build().writeTo(new FileOutputStream(protoBinaryFile));

    FileInputStream fileInputStream = new FileInputStream(protoBinaryFile);

    Message newMessage = builder.getDefaultInstanceForType()
        .getParserForType().parseFrom(fileInputStream);

    System.out.println(newMessage);

    return protoBinaryFile;

  }

  protected String parseToJson(File file, Message.Builder builder) throws IOException {
    
    textParser.merge(Files.asCharSource(file, UTF_8).read(), builder);

    return jsonPrinter.print(builder);
  }

  public String parseToJson(String proto, Message.Builder builder) throws IOException {
    textParser.merge(proto, builder);

    return jsonPrinter.print(builder);
  }

  public String parseToJson(Message message) throws IOException {
    
    return jsonPrinter.print(message);
  }

}
