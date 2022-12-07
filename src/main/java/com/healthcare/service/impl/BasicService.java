package com.healthcare.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.ObjectUtils;

import com.healthcare.model.entity.Visit;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.IFinder;

public abstract class BasicService<T, R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> 
implements IFinder<T>  {

	@Autowired
	R repository;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;
	
	@Override
	public Page<T> findAll(Pageable pageable) {
		pageable = checkPageable(pageable);
		return repository.findAll(pageable);
	}

	@Override
	public Page<T> findAll(T entity, Pageable pageable) {
		pageable = checkPageable(pageable);
		if (entity == null)
			return repository.findAll(pageable);
		return repository.findAll(Example.of(entity), pageable);
	}
	
	@Override
	public Page<T> findAll(Specification<T> specification, Pageable pageable) {
		pageable = checkPageable(pageable);
		return repository.findAll(specification, pageable); 
	}

	
	@Override
	public List<T> findAll(Specification<T> specification) {
		return repository.findAll(specification); 
	}
	   
    protected T convertToClass(Object object) {
        byte[] bytes = serializeByte(object);
        return (T) deserializeVisit(bytes);
    }

    private Object deserializeVisit(byte[] bytes) {
        Converter<byte[], Object> deserializer = new DeserializingConverter(new DefaultDeserializer(Visit.class.getClassLoader()));
        if (ObjectUtils.isEmpty(bytes)) {
            return null;
        }

        try {
            Object result = deserializer.convert(bytes);
            return result;
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize", ex);
        }
    }

    private byte[] serializeByte(Object object) {
        Converter<Object, byte[]> serializer = new SerializingConverter();
        if (object == null) {
            return new byte[0];
        }
        try {
            return serializer.convert(object);
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize", ex);
        }
    }
	
	/**
	 * Check and get pageable
	 * @param pageable
	 * @return
	 */
	protected Pageable checkPageable(Pageable pageable) {
		if(pageable==null)
			pageable = new CustomPageRequest();
		
		if(pageable.getPageSize()==Integer.MAX_VALUE){
			if(defaultMaxSize>0)
				pageable = new PageRequest(pageable.getPageNumber(), defaultMaxSize, pageable.getSort());
			else 
				pageable = new PageRequest(pageable.getPageNumber(), 20, pageable.getSort());
		}
		return pageable;
	}
}
