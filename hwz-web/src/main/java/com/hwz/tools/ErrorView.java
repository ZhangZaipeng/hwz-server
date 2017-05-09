package com.hwz.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hwz.platform.springmvc.ResponseUtils;
import com.hwz.platform.springmvc.StringView;

public class ErrorView extends StringView {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorView.class);
    private final Exception     ex;

    public static final String getExceptionBody(HttpServletRequest req, Exception ex) {

        ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        try {
            ex.printStackTrace(new java.io.PrintWriter(buf, true));
        } finally {
            IOUtils.closeQuietly(buf);
        }

        String request_path = req.getRequestURI();
        String msg = ex.getMessage();
        String strack = buf.toString();

        return String.format("<html>\r\n" + "<head>\r\n"
                             + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n"
                             + "<title>Error 500</title>\r\n" + "</head>\r\n" + "<body><h2>HTTP ERROR 500</h2>\r\n"
                             + "<p>%s\r\n" + "<pre>%s</pre></p><h3>Caused by:</h3><pre>\r\n" + "%s\r\n" + "</pre>\r\n"
                             + "<hr /><i><small>Powered by hwzhqmc.com</small></i><br/>"
                             + "<br/>\r\n<br/>\r\n</body>\r\n</html>", request_path, msg, strack);
    }

    public ErrorView(HttpServletRequest req, Exception ex){
        super(getExceptionBody(req, ex));
        this.ex = ex;
    }

    @Override
    protected void renderMergedOutputModel(@SuppressWarnings("rawtypes") Map model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        boolean useGzip = ResponseUtils.isSupportGzip(request);
        try {
            LOG.error("HTTP-500 " + request.getRequestURI(), ex);
            ResponseUtils.writeJSONToResponse(response, ResponseUtils.DEFAULT_ENCODING, content, useGzip, false,
                                              HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (IOException e) {
            try {
                LOG.error("500-error", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                throw new RuntimeException("system error.", e1);
            }
        }
    }
}
