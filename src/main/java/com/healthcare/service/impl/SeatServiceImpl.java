package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Seat;
import com.healthcare.repository.SeatRepository;
import com.healthcare.service.SeatService;

@Service
@Transactional
public class SeatServiceImpl extends  BasicService<Seat, SeatRepository> implements SeatService {
    private static final String KEY = Seat.class.getSimpleName();

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    private RedisTemplate<String, Seat> redisTemplate;

    @Override
    public List<Seat> findAll() {
        return seatRepository.findAll();
    }



    @Override @Transactional
    public Seat save(Seat seat) {
        if (seat.getId() == null) {
            seat.setCreatedAt(new Timestamp(new Date().getTime()));
        } else {
            seat.setUpdatedAt(new Timestamp(new Date().getTime()));
        }
        Seat savedSeat = seatRepository.save(seat);
        redisTemplate.opsForHash().put(KEY, savedSeat.getId(), savedSeat);

        return savedSeat;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        seatRepository.delete(id);

        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Seat findById(Long id) {
        Object seat = redisTemplate.opsForHash().get(KEY, id);
        if (seat != null) {
            return convertToClass(seat);
        }
        return seatRepository.findOne(id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override
    public List<Seat> findByTableId(Long tableId) {
        return seatRepository.findByTableId(tableId);
    }

    @Override
    public Page<Seat> findByTable(Long tableId, Pageable pageable) {
        return seatRepository.findByTableIdPage(tableId, pageable);
    }

    @Override
    public Page<Seat> findByTableIdList(List<Long> tabelIds, Pageable pageable) {
        pageable = checkPageable(pageable);
        return seatRepository.findByTableIdList(tabelIds, pageable);
    }
    
}
