package com.hwz.platform.platform;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterModifier;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class IndentingModifier extends ObjectWriterModifier {
    private static final Logger LOG = LoggerFactory.getLogger(IndentingModifier.class);

    public static boolean doIndent = false;

    public final Boolean _indent;

    public IndentingModifier() {
        this(null);
    }

    public IndentingModifier(Boolean indent) {
        _indent = indent;
    }

    @Override
    public ObjectWriter modify(EndpointConfigBase<?> endpoint, MultivaluedMap<String, Object> responseHeaders, Object valueToWrite, ObjectWriter w, JsonGenerator g) throws IOException {
        if (_indent != null) {
            if (_indent.booleanValue()) {
                //g.useDefaultPrettyPrinter();
                g.setPrettyPrinter(new MyFormat());
            }
        } else {
            if (doIndent) {
                //g.useDefaultPrettyPrinter();
                g.setPrettyPrinter(new MyFormat());
            }
        }
        return w;
    }

    public static class MyFormat extends DefaultPrettyPrinter {
        private static final long serialVersionUID = 1L;
        public static final String DEFAULT_ROOT_VALUE_SEPARATOR = " ";
        protected String _rootValueSeparator;

        public MyFormat() {
            super(" " + SystemUtils.LINE_SEPARATOR);
        }

        @Override
        public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException, JsonGenerationException {
            if (!this._objectIndenter.isInline()) {
                --this._nesting;
            }

            if (nrOfEntries > 0) {
                this._objectIndenter.writeIndentation(jg, this._nesting);
            } else {
                jg.writeRaw(' ');
            }

            jg.writeRaw('}' + SystemUtils.LINE_SEPARATOR);
        }

        @Override
        public void writeEndArray(JsonGenerator gen, int nrOfValues) throws IOException {
            if (!_arrayIndenter.isInline()) {
                --_nesting;
            }
            if (nrOfValues > 0) {
                _arrayIndenter.writeIndentation(gen, _nesting);
            } else {
                gen.writeRaw(' ');
            }
            gen.writeRaw(']' + SystemUtils.LINE_SEPARATOR);
        }
    }
}
