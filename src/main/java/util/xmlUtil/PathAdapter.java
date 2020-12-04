package util.xmlUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.file.Path;

public class PathAdapter extends XmlAdapter<String, Path> {
    @Override
    public Path unmarshal(String v) throws Exception {
        return null;
    }

    @Override
    public String marshal(Path v) throws Exception {

        return null;
    }
}
