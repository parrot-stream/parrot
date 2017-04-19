package io.parrot.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

public class ParrotHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] body;

	public ParrotHttpServletRequest(HttpServletRequest request) {
		super(request);
		try {
			body = IOUtils.toByteArray(request.getInputStream());
		} catch (IOException ex) {
			body = new byte[0];
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			ByteArrayInputStream bais = new ByteArrayInputStream(body);

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}

	public String jsonBody() {
		return DatatypeConverter.printBase64Binary(body);
	}
}
