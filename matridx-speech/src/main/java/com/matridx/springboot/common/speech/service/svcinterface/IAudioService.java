package com.matridx.springboot.common.speech.service.svcinterface;

import java.io.InputStream;
import java.util.Map;

public interface IAudioService {
	/**
	 * 根据录音文件获取转换后的文字
	 * @param is
	 * @return
	 */
    Map<String, Object> getStringFromAudio(InputStream is);
}
