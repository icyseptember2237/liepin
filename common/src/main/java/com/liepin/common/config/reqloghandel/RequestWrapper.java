package com.liepin.common.config.reqloghandel;




import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Vector;


public class RequestWrapper extends HttpServletRequestWrapper {
    private ServletInputStream inputStream;
    private String originalBody;
    private String body;
    private BufferedReader reader;
    private boolean secure;
    private String scheme;
    private boolean proxied;
    private String remoteAddr;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if (!this.isMultipart()) {
            this.preLoadBody(request);
        }

        this.parseProxyInfo(request);
    }

    private void parseProxyInfo(HttpServletRequest request) {
        this.secure = request.isSecure();
        this.scheme = request.getScheme();
        String forwardedProtocol = request.getHeader("x-forwarded-proto");
        if (StringUtils.isNotEmpty(forwardedProtocol)) {
            this.proxied = true;
            this.scheme = forwardedProtocol.toLowerCase();
            this.secure = "https".equals(this.scheme);
            String forwardedForInfo = request.getHeader("x-forwarded-for");
            this.remoteAddr = StringUtils.isNotEmpty(forwardedForInfo) ? forwardedForInfo.trim().split(",")[0] : request.getRemoteAddr();
        }

    }

    private void preLoadBody(HttpServletRequest request) throws IOException {
        Charset charset = Charset.forName(this.getCharacterEncoding());
        byte[] bodyBytes = this.bytes(request.getInputStream());
        this.originalBody = new String(bodyBytes, charset);
        this.body = this.getParameter("_body");
        if (this.body == null) {
            this.body = this.originalBody;
        }

        this.inputStream = new RequestCachingInputStream(this.body.getBytes(charset));
    }

    public final String getContentType() {
        String overrideContentType = this.getParameter("_contentType");
        return overrideContentType != null ? overrideContentType : super.getContentType();
    }

    public String getHeader(String name) {
        if ("Accept".equals(name)) {
            String overrideAccept = this.getParameter("_accept");
            if (overrideAccept != null) {
                return overrideAccept;
            }
        }

        return super.getHeader(name);
    }

    public Enumeration<String> getHeaders(String name) {
        if ("Accept".equalsIgnoreCase(name)) {
            String overrideAccept = this.getParameter("_accept");
            if (overrideAccept != null) {
                Vector<String> headers = new Vector();
                headers.add(overrideAccept);
                return headers.elements();
            }
        }

        return super.getHeaders(name);
    }

    public String getMethod() {
        String overrideMethod = this.getParameter("_method");
        return overrideMethod != null ? overrideMethod : super.getMethod();
    }

    public int getServerPort() {
        if (this.proxied) {
            if ("http".equals(this.scheme)) {
                return 80;
            }

            if ("https".equals(this.scheme)) {
                return 443;
            }
        }

        return super.getServerPort();
    }

    public ServletInputStream getInputStream() throws IOException {
        return this.inputStream != null ? this.inputStream : super.getInputStream();
    }

    public final String getCharacterEncoding() {
        String defaultEncoding = super.getCharacterEncoding();
        return defaultEncoding != null ? defaultEncoding : "UTF-8";
    }

    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(this.inputStream, this.getCharacterEncoding()));
        }

        return this.reader;
    }

    public String getScheme() {
        return this.scheme;
    }

    public boolean isSecure() {
        return this.secure;
    }

    public String getRemoteAddr() {
        return this.proxied ? this.remoteAddr : super.getRemoteAddr();
    }

    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        String scheme = this.getScheme();
        int port = this.getServerPort();
        if (port < 0) {
            port = 80;
        }

        url.append(scheme).append("://").append(this.getServerName());
        if ("http".equals(scheme) && port != 80) {
            url.append(':');
            url.append(port);
        }

        if ("https".equals(scheme) && port != 443) {
            url.append(':');
            url.append(port);
        }

        url.append(this.getRequestURI());
        return url;
    }

    public String getOriginalBody() {
        if (this.isMultipart()) {
            throw new IllegalStateException("multipart request does not support preloaded body");
        } else {
            return this.originalBody;
        }
    }

    public String getBody() {
        if (this.isMultipart()) {
            throw new IllegalStateException("multipart request does not support preloaded body");
        } else {
            return this.body;
        }
    }

    public final boolean isMultipart() {
        String contentType = this.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    private byte[] bytes(InputStream stream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];

        try {
            while(true) {
                int len = stream.read(buf);
                if (len < 0) {
                    return byteArrayOutputStream.toByteArray();
                }

                byteArrayOutputStream.write(buf, 0, len);
            }
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    private static class RequestCachingInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;
        private boolean finished;

        public RequestCachingInputStream(byte[] bytes) {
            this.inputStream = new ByteArrayInputStream(bytes);
        }

        public int read() throws IOException {
            int eof = this.inputStream.read();
            this.finished = eof == -1;
            return eof;
        }

        public boolean isFinished() {
            return this.finished;
        }

        public boolean isReady() {
            return !this.finished;
        }

        public void setReadListener(ReadListener listener) {
        }
    }
}
