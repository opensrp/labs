import com.google.fhir.common.InvalidFhirException;
import com.google.fhirpathproto.FHIRPathProtoEvaluator;
import com.google.fhirpathproto.JsonFormatGenerate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class JSONProcessTests {

  String JSONPatient = "{\n"
      + "  \"resourceType\": \"Patient\",\n"
      + "  \"id\": \"example\",\n"
      + "  \"address\": [\n"
      + "    {\n"
      + "      \"use\": \"home\",\n"
      + "      \"city\": \"PleasantVille\",\n"
      + "      \"type\": \"both\",\n"
      + "      \"state\": \"Vic\",\n"
      + "      \"line\": [\n"
      + "        \"534 Erewhon St\"\n"
      + "      ],\n"
      + "      \"postalCode\": \"3999\",\n"
      + "      \"period\": {\n"
      + "        \"start\": \"1974-12-25\"\n"
      + "      },\n"
      + "      \"district\": \"Rainbow\",\n"
      + "      \"text\": \"534 Erewhon St PeasantVille, Rainbow, Vic  3999\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"managingOrganization\": {\n"
      + "    \"reference\": \"Organization/1\"\n"
      + "  },\n"
      + "  \"name\": [\n"
      + "    {\n"
      + "      \"use\": \"official\",\n"
      + "      \"given\": [\n"
      + "        \"Peter\",\n"
      + "        \"James\"\n"
      + "      ],\n"
      + "      \"family\": \"Chalmers\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"use\": \"usual\",\n"
      + "      \"given\": [\n"
      + "        \"Jim\"\n"
      + "      ]\n"
      + "    },\n"
      + "    {\n"
      + "      \"use\": \"maiden\",\n"
      + "      \"given\": [\n"
      + "        \"Peter\",\n"
      + "        \"James\"\n"
      + "      ],\n"
      + "      \"family\": \"Windsor\",\n"
      + "      \"period\": {\n"
      + "        \"end\": \"2002\"\n"
      + "      }\n"
      + "    }\n"
      + "  ],\n"
      + "  \"birthDate\": \"1974-12-25\",\n"
      + "  \"deceased\": {\n"
      + "    \"boolean\": false\n"
      + "  },\n"
      + "  \"active\": true,\n"
      + "  \"identifier\": [\n"
      + "    {\n"
      + "      \"use\": \"usual\",\n"
      + "      \"type\": {\n"
      + "        \"coding\": [\n"
      + "          {\n"
      + "            \"code\": \"MR\",\n"
      + "            \"system\": \"http://hl7.org/fhir/v2/0203\"\n"
      + "          }\n"
      + "        ]\n"
      + "      },\n"
      + "      \"value\": \"12345\",\n"
      + "      \"period\": {\n"
      + "        \"start\": \"2001-05-06\"\n"
      + "      },\n"
      + "      \"system\": \"urn:oid:1.2.36.146.595.217.0.1\",\n"
      + "      \"assigner\": {\n"
      + "        \"display\": \"Acme Healthcare\"\n"
      + "      }\n"
      + "    }\n"
      + "  ],\n"
      + "  \"telecom\": [\n"
      + "    {\n"
      + "      \"use\": \"home\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"use\": \"work\",\n"
      + "      \"rank\": 1,\n"
      + "      \"value\": \"(03) 5555 6473\",\n"
      + "      \"system\": \"phone\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"use\": \"mobile\",\n"
      + "      \"rank\": 2,\n"
      + "      \"value\": \"(03) 3410 5613\",\n"
      + "      \"system\": \"phone\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"use\": \"old\",\n"
      + "      \"value\": \"(03) 5555 8834\",\n"
      + "      \"period\": {\n"
      + "        \"end\": \"2014\"\n"
      + "      },\n"
      + "      \"system\": \"phone\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"gender\": \"male\",\n"
      + "  \"contact\": [\n"
      + "    {\n"
      + "      \"name\": {\n"
      + "        \"given\": [\n"
      + "          \"Bénédicte\"\n"
      + "        ],\n"
      + "        \"family\": \"du Marché\",\n"
      + "        \"_family\": {\n"
      + "          \"extension\": [\n"
      + "            {\n"
      + "              \"url\": \"http://hl7.org/fhir/StructureDefinition/humanname-own-prefix\",\n"
      + "              \"valueString\": \"VV\"\n"
      + "            }\n"
      + "          ]\n"
      + "        }\n"
      + "      },\n"
      + "      \"gender\": \"female\",\n"
      + "      \"period\": {\n"
      + "        \"start\": \"2012\"\n"
      + "      },\n"
      + "      \"address\": {\n"
      + "        \"use\": \"home\",\n"
      + "        \"city\": \"PleasantVille\",\n"
      + "        \"line\": [\n"
      + "          \"534 Erewhon St\"\n"
      + "        ],\n"
      + "        \"type\": \"both\",\n"
      + "        \"state\": \"Vic\",\n"
      + "        \"period\": {\n"
      + "          \"start\": \"1974-12-25\"\n"
      + "        },\n"
      + "        \"district\": \"Rainbow\",\n"
      + "        \"postalCode\": \"3999\"\n"
      + "      },\n"
      + "      \"telecom\": [\n"
      + "        {\n"
      + "          \"value\": \"+33 (237) 998327\",\n"
      + "          \"system\": \"phone\"\n"
      + "        }\n"
      + "      ],\n"
      + "      \"relationship\": [\n"
      + "        {\n"
      + "          \"coding\": [\n"
      + "            {\n"
      + "              \"code\": \"N\",\n"
      + "              \"system\": \"http://hl7.org/fhir/v2/0131\"\n"
      + "            }\n"
      + "          ]\n"
      + "        }\n"
      + "      ]\n"
      + "    }\n"
      + "  ]\n"
      + "}";

  private final String JSONPatient2 = "{\n"
      + "  \"active\": true,\n"
      + "  \"deceasedBoolean\": false,\n"
      + "  \"gender\": \"male\",\n"
      + "  \"address\": [\n"
      + "    {\n"
      + "      \"use\": \"home\",\n"
      + "      \"period\": {\n"
      + "        \"start\": \"1974-12-25\"\n"
      + "      },\n"
      + "      \"postalCode\": \"3999\",\n"
      + "      \"type\": \"both\",\n"
      + "      \"district\": \"Rainbow\",\n"
      + "      \"line\": [\n"
      + "        \"534 Erewhon St\"\n"
      + "      ],\n"
      + "      \"text\": \"534 Erewhon St PeasantVille, Rainbow, Vic  3999\",\n"
      + "      \"state\": \"Vic\",\n"
      + "      \"city\": \"PleasantVille\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"id\": \"example\",\n"
      + "  \"name\": [\n"
      + "    {\n"
      + "      \"use\": \"official\",\n"
      + "      \"family\": \"Chalmers\",\n"
      + "      \"given\": [\n"
      + "        \"Peter\",\n"
      + "        \"James\"\n"
      + "      ]\n"
      + "    },\n"
      + "    {\n"
      + "      \"given\": [\n"
      + "        \"Jim\"\n"
      + "      ],\n"
      + "      \"use\": \"usual\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"_birthDate\": {\n"
      + "    \"extension\": [\n"
      + "      {\n"
      + "        \"url\": \"http://hl7.org/fhir/StructureDefinition/patient-birthTime\",\n"
      + "        \"valueDateTime\": \"1974-12-25T14:35:45-05:00\"\n"
      + "      }\n"
      + "    ]\n"
      + "  },\n"
      + "  \"resourceType\": \"Patient\",\n"
      + "  \"telecom\": [\n"
      + "    {\n"
      + "      \"rank\": 2,\n"
      + "      \"value\": \"(03) 3410 5613\",\n"
      + "      \"system\": \"phone\",\n"
      + "      \"use\": \"mobile\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"birthDate\": \"1974-12-25\"\n"
      + "}";

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }


  @Test
  public void countName() throws IOException, FHIRException {
    new FHIRPathProtoEvaluator().processJSON(JSONPatient, "name.count()");

    Assertions.assertEquals("[IntegerType[3]]", outputStreamCaptor.toString().trim());
  }

  @Test
  public void nameCountCollection() throws IOException, FHIRException {
    List<Base> result = new FHIRPathProtoEvaluator().processJSON(JSONPatient, "name.count()");

    IntegerType three = new IntegerType(3);


    for (int i = 0; i < result.size(); i++) {
//      Assertions.assertTrue(result.get(i).equalsDeep(list3.get(i)));
      Assertions.assertEquals(three.getValue(), ((IntegerType) result.get(i)).getValue());
    }
  }

  @Test
  public void  patientNameChildren1() throws IOException, FHIRException {
    new FHIRPathProtoEvaluator().processJSON(JSONPatient2, "Patient.name.children()");

    Assertions.assertEquals("[Enumeration[official], Chalmers, Peter, James, Enumeration[usual], Jim]",
        outputStreamCaptor.toString().trim());
  }

  @Test
  public void PatientNameChildren() throws IOException, FHIRException {
    List<Base> result
        = new FHIRPathProtoEvaluator().processJSON(JSONPatient2, "Patient.name.children()");

    ArrayList<Base> expectedChildren = new ArrayList<>();

    Assertions.assertEquals("official", ((Enumeration) result.get(0)).getValueAsString());
    Assertions.assertEquals("Chalmers", ((StringType) result.get(1)).getValue());
    Assertions.assertEquals("Peter", ((StringType) result.get(2)).getValue());
    Assertions.assertEquals("James", ((StringType) result.get(3)).getValue());
    Assertions.assertEquals("usual", ((Enumeration) result.get(4)).getValueAsString());
    Assertions.assertEquals("Jim", ((StringType) result.get(5)).getValue());

  }

  @Test
  public void invalidFilter() throws IOException, FHIRException {
    List<Base> result
        = new FHIRPathProtoEvaluator().processJSON(JSONPatient2, "name.where(use='family')");

    Assertions.assertEquals(result, new ArrayList<>());
  }
  @Test
  public void nameFilter() throws IOException, FHIRException {
    List<Base> result
        = new FHIRPathProtoEvaluator().processJSON(JSONPatient2, "name.where(use='official')");

    Assertions.assertEquals("Peter James",
        ((HumanName) result.get(0)).getGivenAsSingleString());

    Assertions.assertEquals("Chalmers", ((HumanName) result.get(0)).getFamily());
  }

  @Test
  public void telecomDescendants() throws IOException, FHIRException {
    List<Base> result
        = new FHIRPathProtoEvaluator().processJSON(JSONPatient2, "telecom.descendants()");

    Assertions.assertEquals("phone", ((Enumeration) result.get(0)).getValueAsString());
    Assertions.assertEquals("(03) 3410 5613", ((StringType) result.get(1)).getValue());
    Assertions.assertEquals("mobile", ((Enumeration) result.get(2)).getValueAsString());
    Assertions.assertEquals(2, ((PositiveIntType) result.get(3)).getValue());
  }

  @Test
  public void telecomWithFilterAndFirst() throws IOException, FHIRException {
    List<Base> result
        = new FHIRPathProtoEvaluator().processJSON(JSONPatient,
        "telecom.where(use='mobile').system.first()");

    Assertions.assertEquals("phone", ((Enumeration) result.get(0)).getValueAsString());
  }
  

  @Test
  public void testBooleanExpressionName() throws IOException, FHIRException {
    List<Base> result = new FHIRPathProtoEvaluator().processJSON(JSONPatient2,
        "name.where(use = 'official').empty()");

    Assertions.assertNotNull(result);
    Assertions.assertEquals("false", result.get(0).primitiveValue());
  }

  @Test
  public void testBooleanExpressionName2() throws IOException, FHIRException {
    List<Base> result = new FHIRPathProtoEvaluator().processJSON(JSONPatient2,
        "name.where(use = 'family').empty()");

  Assertions.assertNotNull(result);
  Assertions.assertEquals("true", result.get(0).primitiveValue());
  }
}
