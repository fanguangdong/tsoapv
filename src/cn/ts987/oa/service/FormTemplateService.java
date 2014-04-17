package cn.ts987.oa.service;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.dao.FormTemplateDao;
import cn.ts987.oa.domain.FormTemplate;

@Service("formTemplateService")
@Transactional
public class FormTemplateService {
	
	@Resource
	private FormTemplateDao formTemplateDao;
	
	public Collection<FormTemplate> list() {
		Collection<FormTemplate> formTemplateList = formTemplateDao.findAll();
		return formTemplateList;
	}

	public void add(FormTemplate model) {
		formTemplateDao.save(model);
		
	}

	public void delete(Long id) {
		formTemplateDao.delete(id);
	}

	public FormTemplate getById(Long id) {
		return formTemplateDao.findById(id);
	}
	
}
