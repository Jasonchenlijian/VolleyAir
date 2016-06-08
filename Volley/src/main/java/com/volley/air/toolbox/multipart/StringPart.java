package com.volley.air.toolbox.multipart;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static com.volley.air.misc.MultipartUtils.*;
import static com.volley.air.misc.MultipartUtils.COLON_SPACE;
import static com.volley.air.misc.MultipartUtils.CRLF_BYTES;
import static com.volley.air.misc.MultipartUtils.EIGHT_BIT;
import static com.volley.air.misc.MultipartUtils.FORM_DATA;
import static com.volley.air.misc.MultipartUtils.HEADER_CONTENT_DISPOSITION;
import static com.volley.air.misc.MultipartUtils.HEADER_CONTENT_TRANSFER_ENCODING;
import static com.volley.air.misc.MultipartUtils.HEADER_CONTENT_TYPE;

/**
 * @author <a href="mailto:vit at cleverua.com">Vitaliy Khudenko</a>
 */
public final class StringPart extends BasePart {
    
    private final byte[] valueBytes;
    
    /**
     * @param name String - name of parameter (may not be <code>null</code>).
     * @param value String - value of parameter (may not be <code>null</code>).
     * @param charset String, if null is passed then default "ISO-8859-1" charset is used.
     * 
     * @throws IllegalArgumentException if either <code>value</code> 
     *         or <code>name</code> is <code>null</code>.
     * @throws RuntimeException if <code>charset</code> is unsupported by OS.
     */
    public StringPart(String name, String value, String charset) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");     //$NON-NLS-1$
        }
        if (value == null) {
            throw new IllegalArgumentException("Value may not be null");    //$NON-NLS-1$
        }
        
        final String partName = UrlEncodingHelper.encode(name, HTTP.DEFAULT_PROTOCOL_CHARSET);
        
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        final String partCharset = charset;
        
        try {
            this.valueBytes = value.getBytes(partCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        
        headersProvider = new IHeadersProvider() {
            public String getContentDisposition() {
                return String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA, partName);
            }
            public String getContentType() {
                return HEADER_CONTENT_TYPE + COLON_SPACE + HTTP.PLAIN_TEXT_TYPE;
                		//"Content-Type: " + HTTP.PLAIN_TEXT_TYPE + HTTP.CHARSET_PARAM + partCharset;  //$NON-NLS-1$
            }
            public String getContentTransferEncoding() {
                return HEADER_CONTENT_TRANSFER_ENCODING + COLON_SPACE + EIGHT_BIT;
            }
        };
    }

    /**
     * Default "ISO-8859-1" charset is used.
     * @param name String - name of parameter (may not be <code>null</code>).
     * @param value String - value of parameter (may not be <code>null</code>).
     * 
     * @throws IllegalArgumentException if either <code>value</code> 
     *         or <code>name</code> is <code>null</code>.
     */
    public StringPart(String name, String value) {
        this(name, value, null);
    }
    
    public long getContentLength(Boundary boundary) {
        return getHeader(boundary).length + valueBytes.length + CRLF_BYTES.length;
    }

    public void writeTo(final OutputStream out, Boundary boundary) throws IOException {
        out.write(getHeader(boundary));
        out.write(valueBytes);
        out.write(CRLF_BYTES);
    }
}
