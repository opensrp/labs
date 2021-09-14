import com.google.fhir.r4.core.Appointment;
import com.google.fhir.r4.core.Practitioner;
import com.google.fhirpathproto.FHIRPathProtoEvaluator;
import com.google.fhirpathproto.JsonFormatBase;
import com.google.fhirpathproto.JsonFormatBase.FileType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtoTxtFHIRPathTests {

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
  public void TestPractitionerTxt() throws IOException, FHIRException {

    String string = new JsonFormatBase().
        parseStringToProto(jsonPractitioner, Practitioner.newBuilder());

    System.out.println(string);

    String practitioner = new JsonFormatBase().parseToJson(string, Practitioner.newBuilder());

    List<Base> result = new FHIRPathProtoEvaluator().processJSON(practitioner,
        "qualification.issuer");

    Assertions.assertEquals("Example University", ((Reference) result.get(0)).getDisplay());

  }

  @Test
  public void testAppointmentExampleAndTail() throws IOException, FHIRException {

    JsonFormatBase jsonFormatBase = new JsonFormatBase();

    File file = jsonFormatBase.getExampleFile("Appointment-example", FileType.PROTOTXT);

    FHIRPathProtoEvaluator fhirPathProtoEvaluator = new FHIRPathProtoEvaluator();

    Appointment.Builder appointmentBuilder = Appointment.newBuilder();

    List<Base> result = fhirPathProtoEvaluator.
        evaluate(file, "participant.actor", appointmentBuilder);

    Assertions.assertEquals("Peter James Chalmers",
        ((Reference) result.get(0)).getDisplay());

    Assertions.assertEquals("Dr Adam Careful",
        ((Reference) result.get(1)).getDisplay());

    Assertions.assertEquals("South Wing, second floor",
        ((Reference) result.get(2)).getDisplay());

    List<Base> tailResult = fhirPathProtoEvaluator.
        evaluate(file, "participant.actor.tail()", appointmentBuilder);

    Assertions.assertEquals("Dr Adam Careful",
        ((Reference) tailResult.get(0)).getDisplay());

    Assertions.assertEquals("South Wing, second floor",
        ((Reference) tailResult.get(1)).getDisplay());

    System.out.println(result.get(0).children());
    
  }

}
