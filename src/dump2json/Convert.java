package dump2json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.json.stream.JsonStreamFactory;
import de.odysseus.staxon.json.stream.impl.JsonStreamFactoryImpl;

public class Convert {

  final static String convertToString(final String file) throws Exception {
    // https://github.com/beckchr/staxon/wiki/Converting-XML-to-JSON
    final InputStream in = new FileInputStream(new File(file));
    final XMLEventReader xml = XMLInputFactory.newInstance()
        .createXMLEventReader(in);

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true)
        .autoPrimitive(true).prettyPrint(true).build();
    // https://github.com/beckchr/staxon/issues/5
    final JsonStreamFactory jsonStream = new JsonStreamFactoryImpl(" ", "  ",
        "\n");
    final XMLEventWriter json = new JsonXMLOutputFactory(config, jsonStream)
        .createXMLEventWriter(out);

    json.add(xml);
    xml.close();
    json.close();
    out.close();
    in.close();

    return out.toString("UTF-8");
  }
  
  final static void convert(final File file) throws Exception {
    // https://github.com/beckchr/staxon/wiki/Converting-XML-to-JSON
    if (!file.exists() || !file.isFile()) {
      throw new Exception("Path must be an existing file.");
    }
    
    final InputStream in = new FileInputStream(file);
    final XMLEventReader xml = XMLInputFactory.newInstance()
        .createXMLEventReader(in);

    final File jsonFile = new File(file.getParent(), file.getName() + ".json");
    final FileOutputStream out = new FileOutputStream(jsonFile);
    final JsonXMLConfig jsonConfig = new JsonXMLConfigBuilder().autoArray(true)
        .autoPrimitive(true).prettyPrint(true).build();
    // https://github.com/beckchr/staxon/issues/5
    final JsonStreamFactory jsonStream = new JsonStreamFactoryImpl(" ", "  ",
        "\n");
    final XMLEventWriter json = new JsonXMLOutputFactory(jsonConfig, jsonStream)
        .createXMLEventWriter(out);

    json.add(xml);
    xml.close();
    json.close();
    out.close();
    in.close();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      throw new Exception("Pass only one arg.");
    }
    
    final File file = new File(args[0]);
    
    if (!file.exists() || !file.isFile()) {
      throw new Exception("Path must be an existing file.");
    }

    convert(file);
  }
}