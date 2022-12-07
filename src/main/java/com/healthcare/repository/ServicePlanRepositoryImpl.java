package com.healthcare.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.healthcare.model.entity.ServicePlan;
import com.healthcare.util.DateUtils;

public class ServicePlanRepositoryImpl implements ServicePlanRepositoryCustom {
	private static final String DAYS_DELIMITER = ",";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<String> getServiceCalendar(ServicePlan servicePlan) {
		if (servicePlan.getDays() == null) {
			return new ArrayList<String>();
		}

		StringBuilder sql = new StringBuilder("");
		StringBuilder daySql = new StringBuilder("");
		String[] days = servicePlan.getDays().split(DAYS_DELIMITER);

		for (int i = 0; i < days.length; i++) {
			if (i == 0)
				daySql.append(" and (");
			daySql.append("dayname(selected_date) = '").append(days[i].toUpperCase()).append("'");
			if (i < days.length - 1)
				daySql.append(" or ");
			if (i == days.length - 1)
				daySql.append(") ");
		}

		sql.append("select * from ");
		sql.append(
				"(select adddate('2016-01-01',t4.i*10000 + t3.i*1000 + t2.i*100 + t1.i*10 + t0.i) selected_date from ");
		sql.append(
				"(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, ");
		sql.append(
				"(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, ");
		sql.append(
				"(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, ");
		sql.append(
				"(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, ");
		sql.append(
				"(select 0 i union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v ");
		sql.append("where selected_date between '")
				.append(DateUtils.dateToString(DATE_FORMAT, new Date(servicePlan.getPlanStart().getTime())))
				.append("' and '")
				.append(DateUtils.dateToString(DATE_FORMAT, new Date(servicePlan.getPlanEnd().getTime()))).append("' ");
		sql.append(daySql.toString());
		sql.append("order by selected_date");
		return em.createNativeQuery(sql.toString()).getResultList();
	}

}
