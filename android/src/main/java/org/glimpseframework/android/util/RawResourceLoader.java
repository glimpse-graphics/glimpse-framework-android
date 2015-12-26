package org.glimpseframework.android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Android raw resource file loader.
 * @author Slawomir Czerwinski
 */
public class RawResourceLoader {

	/**
	 * Creates a new resource loader.
	 * @param context Android context
	 */
	public RawResourceLoader(Context context) {
		this.context = context;
	}

	/**
	 * Loads raw resource to {@code String}.
	 * @param id raw resource ID
	 * @return raw resource {@code String} contents
	 */
	@NonNull
	public String loadResource(@RawRes int id) {
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
			return EMPTY_STRING;
		}
	}

	private static final String EMPTY_STRING = "";

	private static final Logger LOG = LoggerFactory.getLogger(RawResourceLoader.class);

	private Context context;
}
