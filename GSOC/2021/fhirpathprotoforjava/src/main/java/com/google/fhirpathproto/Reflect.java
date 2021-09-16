package com.google.fhirpathproto;
import com.google.fhir.r4.core.Patient;
import java.lang.reflect.*;

import com.google.fhir.shaded.protobuf.Message;

public class Reflect {

  public void getProtoBinary(String name, Message.Builder builder) throws NoSuchMethodException {

    Object newBuilder = builder;

    Class a = builder.getClass();

    System.out.println(a);
  }

  public static void main(String[] args) throws NoSuchMethodException {
    new Reflect().getProtoBinary("name", Patient.newBuilder());
  }

}
