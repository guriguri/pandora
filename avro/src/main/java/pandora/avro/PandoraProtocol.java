/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package pandora.avro;

@SuppressWarnings("all")
public interface PandoraProtocol {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"PandoraProtocol\",\"namespace\":\"pandora.avro\",\"types\":[{\"type\":\"record\",\"name\":\"PandoraRecord\",\"fields\":[{\"name\":\"stringData\",\"type\":\"string\",\"default\":null},{\"name\":\"byteData\",\"type\":\"bytes\"},{\"name\":\"intData\",\"type\":\"int\"},{\"name\":\"longData\",\"type\":\"long\"},{\"name\":\"floatData\",\"type\":\"float\"},{\"name\":\"doubleData\",\"type\":\"double\"},{\"name\":\"booleanData\",\"type\":\"boolean\",\"default\":true}]},{\"type\":\"enum\",\"name\":\"PandoraEnum\",\"symbols\":[\"FIRST\",\"SECOND\",\"THIRD\"]},{\"type\":\"record\",\"name\":\"PandoraList\",\"fields\":[{\"name\":\"recordList\",\"type\":{\"type\":\"array\",\"items\":\"PandoraRecord\"}},{\"name\":\"stringList\",\"type\":{\"type\":\"array\",\"items\":\"string\"}},{\"name\":\"pandoraEnum\",\"type\":\"PandoraEnum\"}]}],\"messages\":{}}");

  @SuppressWarnings("all")
  public interface Callback extends PandoraProtocol {
    public static final org.apache.avro.Protocol PROTOCOL = pandora.avro.PandoraProtocol.PROTOCOL;
  }
}