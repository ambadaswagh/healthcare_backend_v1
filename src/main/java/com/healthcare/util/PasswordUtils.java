package com.healthcare.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
	public static String hashPassword(String password_plaintext) {
		return BCrypt.hashpw(password_plaintext, BCrypt.gensalt());
	}

	public static boolean checkPassword(String password_plaintext, String stored_hash) {
		return BCrypt.checkpw(password_plaintext, stored_hash);
	}

}
