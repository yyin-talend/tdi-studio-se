package org.talend.designer.components.hashfile.common;

import java.util.HashMap;
import java.util.Map;
import org.talend.designer.components.hashfile.memory.AdvancedMemoryHashFile;

public class MapHashFile {
	//use this map instead of globalMap
	private Map<String, AdvancedMemoryHashFile> resourceMap = new HashMap<String, AdvancedMemoryHashFile>();
	//keep the present key of AdvancedMemoryHashFile as key and the previous key as value
	private Map<String, String> keyMap = new HashMap<String, String>();
	//singleton
	private static final MapHashFile mhf = new MapHashFile();

	private MapHashFile() {
	}

	public static MapHashFile getMapHashFile() {
		return mhf;
	}
	//get the linked AdvancedMemoryHashFile
	public AdvancedMemoryHashFile getAdvancedMemoryHashFile(String key) {
		AdvancedMemoryHashFile amhf = resourceMap.get(key);
		String prekey = keyMap.get(key);
		//if present AdvancedMemoryHashFile is null get the AdvancedMemoryHashFile before present.
		while(amhf==null){
			amhf = resourceMap.get(prekey);
			prekey = keyMap.get(prekey);
		}
		return amhf;
	}

	public Map<String, AdvancedMemoryHashFile> getResourceMap() {
		return resourceMap;
	}

	public Map<String, String> getKeyMap() {
		return keyMap;
	}
}