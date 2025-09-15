package processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;

public class CanonicalPrettyPrinter extends DefaultPrettyPrinter {

    private static final DefaultIndenter LF_INDENTER = new DefaultIndenter("  ", "\n");

    public CanonicalPrettyPrinter() {
        this._objectIndenter = LF_INDENTER;
        this._arrayIndenter = LF_INDENTER;
    }

    @Override
    public CanonicalPrettyPrinter createInstance() {
        return new CanonicalPrettyPrinter();
    }

    @Override
    public void writeStartObject(JsonGenerator g) throws IOException {
        g.writeRaw('{');
        this._nesting++;
    }

    @Override
    public void writeEndObject(JsonGenerator g, int nrOfEntries) throws IOException {
        this._nesting--;
        if (nrOfEntries > 0) {
            this._objectIndenter.writeIndentation(g, this._nesting);
        }
        g.writeRaw('}');
    }

    @Override
    public void writeStartArray(JsonGenerator g) throws IOException {
        g.writeRaw('[');
        this._nesting++;
    }

    @Override
    public void writeEndArray(JsonGenerator g, int nrOfValues) throws IOException {
        this._nesting--;
        if (nrOfValues > 0) {
            this._arrayIndenter.writeIndentation(g, this._nesting);
        }
        g.writeRaw(']');
    }

    @Override
    public void writeObjectFieldValueSeparator(JsonGenerator gen) throws IOException {
        gen.writeRaw(": ");
    }
}
