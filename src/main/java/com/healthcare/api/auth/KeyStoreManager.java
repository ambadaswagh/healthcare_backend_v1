package com.healthcare.api.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author orange
 *
 */
public class KeyStoreManager {

	// Instance
	private static KeyStoreManager instance;

	public static synchronized KeyStoreManager getInstance() {
		if (instance == null) {
			instance = new KeyStoreManager();
		}
		return instance;
	}

	private static final long keyStoreExpire = 12 * 60 * 60 * 1000;// 12 hours
	private List<KeyEntry> keyStore = null;

	private KeyStoreManager() {
		keyStore = new LinkedList<KeyEntry>();
	}

	public void save(String key, long time) {
		// Clear expire key
		cleanKeyStore();
		// Save key store
		keyStore.add(new KeyEntry(key, time));

	}

	public boolean remove(String key) {
		KeyEntry keyEntryFound = null;
		for (KeyEntry entry : keyStore) {
			if (entry.getKey().equals(key)) {
				keyEntryFound = entry;
				break;
			}
		}
		if (keyEntryFound != null) {
			keyStore.remove(keyEntryFound);
			return true;
			// System.out.println("Remove token " + key);
			// System.out.println("keyStore size: " + keyStore.size());
		}
		return false;
	}

	public boolean isExist(String key) {
		// System.out.println(" check isExist token: " + key);
		// System.out.println(" current keyStore size: " + keyStore.size());
		if (keyStore.stream().anyMatch((entry) -> (entry.getKey().equals(key)))) {
			return true;
		}
		return false;
	}

	private void cleanKeyStore() {
		try {
			List<KeyEntry> keyRemove = new ArrayList<KeyEntry>();
			long currentTime = new Date().getTime();
			keyStore.stream().filter((entry) -> (currentTime - entry.getTimeCreate() >= keyStoreExpire))
					.forEach((entry) -> {
						keyRemove.add(entry);
					});
			if (keyRemove.size() > 0) {
				keyStore.removeAll(keyRemove);
			}
		} catch (Exception ex) {
		}
	}
}
