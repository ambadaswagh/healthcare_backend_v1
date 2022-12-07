package com.healthcare.service;

import java.util.List;
import com.healthcare.model.entity.WorkItem;

/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */


public interface WorkItemService extends IService<WorkItem>, IFinder<WorkItem>{
	List<WorkItem> findAll();
}
