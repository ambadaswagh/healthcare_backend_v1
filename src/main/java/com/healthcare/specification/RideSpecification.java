package com.healthcare.specification;

import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.RideLine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class RideSpecification {
    private RideSpecification() {
    }


    public static Specification<Ride> getRideByRideLine(List<RideLine> rideLines) {
        return new Specification<Ride>() {
            @Override
            public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (!isInputValid1(rideLines)) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.and(root.get("rideLine").in(rideLines));
            }
        };
    }

    public static Specification<Ride> hasUser(Long userId) {
        return new Specification<Ride>() {
            @Override
            public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (!isInputValid(userId)) {
                    return criteriaBuilder.conjunction();
                }
                  return criteriaBuilder.and(root.get("user").in(userId));
            }
        };
    }
    public static Specification<Ride> getBydate(Date fromDate, Date toDate) {
        return new Specification<Ride>() {
            @Override
            public Predicate toPredicate(Root<Ride> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (!isInputValid(fromDate)) {
                    return criteriaBuilder.conjunction();
                }
                Predicate p1 = criteriaBuilder.greaterThanOrEqualTo(root.get("date"),(fromDate));
                Predicate p2 = criteriaBuilder.lessThanOrEqualTo(root.get("date"),(toDate));
                return criteriaBuilder.and(p1, p2);
            }
        };
    }

    private static boolean isInputValid(List<String> input) {
        return !(input == null || input.isEmpty());
    }
    private static boolean isInputValid(Date date) {
        return !(date == null);
    }
    private static boolean isInputValid1(List<RideLine> rideLines) {
        return !(rideLines == null || rideLines.isEmpty());
    }

    private static boolean isInputValid(Long input) {
        return !(input == null);
    }

    private static boolean isInputValid(String input) {
        return !(input == null || input.isEmpty());
    }
}
