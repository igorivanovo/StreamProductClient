import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static java.lang.Boolean.parseBoolean;

public class XMLSettingsReader {
    private boolean isLoad;
    private String loadFile;
    private String loadFormat;

    private boolean isSave;
    private String saveFile;
    private String saveFormat;

    private boolean isLog;
    private String logFile;

    public boolean isLoad() {
        return isLoad;
    }

    public String getLoadFile() {
        return loadFile;
    }

    public String getLoadFormat() {
        return loadFormat;
    }

    public boolean isSave() {
        return isSave;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public boolean isLog() {
        return isLog;
    }

    public String getLogFile() {
        return logFile;
    }

    public XMLSettingsReader(File xmFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmFile);

        Element root = doc.getDocumentElement();
        Element loadSettings = (Element) root.getElementsByTagName("load").item(0);
        Element saveSettings = (Element) root.getElementsByTagName("save").item(0);
        Element logSettings = (Element) root.getElementsByTagName("log").item(0);

        isLoad = Boolean.parseBoolean((loadSettings.getElementsByTagName("enabled").item(0).getTextContent()));
        loadFile = loadSettings.getElementsByTagName("fileName").item(0).getTextContent();
        loadFormat = loadSettings.getElementsByTagName("format").item(0).getTextContent();

        isSave = Boolean.parseBoolean((saveSettings.getElementsByTagName("enabled").item(0).getTextContent()));
        saveFile = saveSettings.getElementsByTagName("fileName").item(0).getTextContent();
        saveFormat = saveSettings.getElementsByTagName("format").item(0).getTextContent();

        isLog = Boolean.parseBoolean((logSettings.getElementsByTagName("enabled").item(0).getTextContent()));
        logFile = logSettings.getElementsByTagName("fileName").item(0).getTextContent();

    }

}
