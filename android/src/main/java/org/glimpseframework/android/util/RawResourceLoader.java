package org.glimpseframework.android.util;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawResourceLoader {

	public RawResourceLoader(Context context) {
		this.context = context;
	}

	public String loadResource(int id) {
		InputStream in = null;
		try {
			in = context.getResources().openRawResource(id);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			return new String(bytes);
		} catch (IOException e) {
			LOG.error("Error loading raw resource", e);
			try {
				in.close();
			} catch (IOException e2) {
				LOG.error("Error closing input stream", e);
			}
			return null;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(RawResourceLoader.class);

	private Context context;
}
