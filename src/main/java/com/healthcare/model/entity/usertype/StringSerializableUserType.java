package com.healthcare.model.entity.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

/**
 * Basic serializable user type
 */
public abstract class StringSerializableUserType<T> implements UserType {

	private static final int[] SQL_TYPES = { Types.LONGVARCHAR };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.nullSafeEquals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor implementor, Object owner)
			throws HibernateException, SQLException {
		return rs.wasNull() ? null : deserialize(rs.getString(names[0]));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor implementor)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, SQL_TYPES[0]);
		} else {
			st.setString(index, serialize(value));
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return (value == null) ? null : deserialize(serialize(value));
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deserialize((String) cached);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return serialize(value);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}

	/**
	 * cast Object to concrete type
	 * 
	 * @param obj
	 *            object
	 * @return casted object
	 */
	protected abstract T cast(Object obj);

	/**
	 * serialize concrete type
	 * 
	 * @param t
	 *            instance to serialize
	 * @return serialized string
	 */
	protected abstract String serializeConcrete(T t);

	private String serialize(Object obj) {
		return serializeConcrete(cast(obj));
	}

	/**
	 * deserialize concrete type
	 * 
	 * @param string
	 *            serialized string
	 * @return deserialized object
	 */
	protected abstract T deserializeConcrete(String string);

	private T deserialize(String string) {
		return deserializeConcrete(string);
	}
}
