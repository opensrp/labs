
import ca.uhn.fhir.parser.IJsonLikeParser;
import com.google.fhir.common.InvalidFhirException;
import com.google.fhir.r4.core.Appointment;
import com.google.fhir.r4.core.Patient;
import com.google.fhir.r4.core.Practitioner;
import com.google.fhir.r4.core.UnsignedInt;
import com.google.fhir.shaded.api.client.util.IOUtils;
import com.google.fhirpathproto.FHIRPathProtoEvaluator;
import com.google.fhirpathproto.JsonFormatBase;
import com.google.fhirpathproto.JsonFormatBase.FileType;
import com.google.fhirpathproto.JsonFormatGenerate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.HumanName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class ProtoBinaryFHIRPathTests {
  
  String jsonPractitioner = "{\n"
      + "  \"resourceType\": \"Practitioner\",\n"
      + "  \"id\": \"example\",\n"
      + "  \"text\": {\n"
      + "    \"status\": \"generated\",\n"
      + "    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n      <p>Dr Adam Careful is a Referring Practitioner for Acme Hospital from 1-Jan 2012 to 31-Mar\\n        2012</p>\\n    </div>\"\n"
      + "  },\n"
      + "  \"identifier\": [\n"
      + "    {\n"
      + "      \"system\": \"http://www.acme.org/practitioners\",\n"
      + "      \"value\": \"23\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"active\": true,\n"
      + "  \"name\": [\n"
      + "    {\n"
      + "      \"family\": \"Careful\",\n"
      + "      \"given\": [\n"
      + "        \"Adam\"\n"
      + "      ],\n"
      + "      \"prefix\": [\n"
      + "        \"Dr\"\n"
      + "      ]\n"
      + "    }\n"
      + "  ],\n"
      + "  \"address\": [\n"
      + "    {\n"
      + "      \"use\": \"home\",\n"
      + "      \"line\": [\n"
      + "        \"534 Erewhon St\"\n"
      + "      ],\n"
      + "      \"city\": \"PleasantVille\",\n"
      + "      \"state\": \"Vic\",\n"
      + "      \"postalCode\": \"3999\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"qualification\": [\n"
      + "    {\n"
      + "      \"identifier\": [\n"
      + "        {\n"
      + "          \"system\": \"http://example.org/UniversityIdentifier\",\n"
      + "          \"value\": \"12345\"\n"
      + "        }\n"
      + "      ],\n"
      + "      \"code\": {\n"
      + "        \"coding\": [\n"
      + "          {\n"
      + "            \"system\": \"http://terminology.hl7.org/CodeSystem/v2-0360/2.7\",\n"
      + "            \"code\": \"BS\",\n"
      + "            \"display\": \"Bachelor of Science\"\n"
      + "          }\n"
      + "        ],\n"
      + "        \"text\": \"Bachelor of Science\"\n"
      + "      },\n"
      + "      \"period\": {\n"
      + "        \"start\": \"1995\"\n"
      + "      },\n"
      + "      \"issuer\": {\n"
      + "        \"display\": \"Example University\"\n"
      + "      }\n"
      + "    }\n"
      + "  ]\n"
      + "}";
  
  
  @Test
  public void testPatientNameCount() throws IOException, FHIRException {
    
    JsonFormatBase jsonFormatBase = new JsonFormatBase();

    File file = jsonFormatBase.getExampleFile("PatientExample", FileType.PROTOBINARY);

    System.out.println(new FHIRPathProtoEvaluator().
        evaluateBinaryResource(file, "Patient.name.count()", Patient.newBuilder()));

    System.out.println(new FHIRPathProtoEvaluator().
        evaluateBinaryResource(file, "name.given", Patient.newBuilder()));

    
    
  }

  @Test
  public void testPatientInvariantName() throws IOException, FHIRException {

    File file = new JsonFormatBase().
        createProtoBinaryFile("PatientExample", Patient.newBuilder());
    

    List<Base> result = new FHIRPathProtoEvaluator().evaluateBinaryResource(file,
        "name.where(use = 'official')", Patient.newBuilder());

    Assertions.assertEquals("Peter James",
        ((HumanName) result.get(0)).getGivenAsSingleString());
  }

  @Test
  public void testPractitionerTxt() throws IOException, FHIRException {

    String string = new JsonFormatBase().
        parseStringToProto(jsonPractitioner, Practitioner.newBuilder());

    System.out.println(string);

    String practitioner = new JsonFormatBase().parseToJson(string, Practitioner.newBuilder());

    System.out.println(new FHIRPathProtoEvaluator().processJSON(practitioner, 
        "qualification.issuer"));
  }

  @Test
  public void testBooleanExpressionName() throws IOException, InvalidFhirException, FHIRException {
    String[] filenames = new String[] {"PatientExample"};
    Patient.Builder patientBuilder = Patient.newBuilder();
    new JsonFormatGenerate().generateProtoTxt(filenames, patientBuilder);

    JsonFormatBase jsonFormatBase = new JsonFormatBase();

    File file = jsonFormatBase.getExampleFile("PatientExample", FileType.PROTOTXT);

    List<Base> result = new FHIRPathProtoEvaluator().evaluate(file,
        "name.where(use = 'official').empty()", patientBuilder);

    Assertions.assertEquals("false", result.get(0).primitiveValue());

  }

  @Test
  public void testProtoTxtFilePathNotNullIfInWorkingDirectory() {

    JsonFormatBase jsonFormatBase = new JsonFormatBase();

    String filePath = jsonFormatBase.getProtoTxtPath("PatientExample");
    Assertions.assertNotNull(filePath);
  }

  @Test
  public void testCreateBinaryFileFromProto() throws IOException {

    Patient.Builder patientBuilder = Patient.newBuilder();
    new JsonFormatBase().createProtoBinaryFile("PatientExample", patientBuilder);

    Assertions.assertDoesNotThrow(IOUtils::new);
  }

  @Test
  public void testCreateBinaryFileFromJson()
      throws IOException, NoSuchFieldException {

    Appointment.Builder appointmentBuilder = Appointment.newBuilder();

    JsonFormatBase jsonFormatBase = new JsonFormatBase();
    jsonFormatBase.generateProtoBinary("AppointmentExample", appointmentBuilder);

    Assertions.assertEquals(2,
        appointmentBuilder.getClass().getDeclaredField("priority_").getModifiers());

    Assertions.assertDoesNotThrow(IOUtils::new);
  }

}
