package cn.ts987.oa.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.ts987.oa.domain.Department;

import com.opensymphony.xwork2.ActionContext;

@Controller("departmentAction")
@Scope("prototype")
public class DepartmentAction extends BaseAction<Department>{

	private static final long serialVersionUID = 1L;
	
	private long parentId;
	
	public String list() throws Exception {
		List<Department> departmentList = null;
		long ppid = 0;
		
		if(parentId > 0) {
			departmentList = departmentService.findChildren(parentId);
			
			Department parentDepartment = departmentService.getParent(parentId);
			if(parentDepartment != null) {
				
				ppid = parentDepartment.getId();
			}
		} else {
			departmentList = departmentService.findRootList();
		}
		
		ActionContext.getContext().put("departmentList", departmentList);
		ActionContext.getContext().put("ppid", ppid);
		return "list";
		
	}
	
	public String add() throws Exception {
		System.err.println("parentId is: " + parentId);
		Department department = new Department();
		
		if(parentId >= 0) {
			Department parentDepartment = departmentService.findById(parentId);
			if(parentDepartment != null)
				department.setParent(parentDepartment);
		}
		
		
		department.setName(model.getName());
		department.setDescription(model.getDescription());
		departmentService.add(department);
		return "toList";
	}
	
	public String update() throws Exception {
		Department department = departmentService.findById(model.getId());
		
		if(parentId >= 0) {
			Department parentDepartment = departmentService.findById(parentId);
			if(parentDepartment != null)
				department.setParent(parentDepartment);
			else 
				department.setParent(null);
		} else {
			department.setParent(null);
		}
		
		department.setName(model.getName());
		department.setDescription(model.getDescription());
		departmentService.update(department);
		return "toList";
	}
	
	public String delete() throws Exception {
		departmentService.delete(model.getId());
		return "toList";
	}
	
	
	//递归层级显示部门列表
	private void show(Collection<Department> trees, String prefix, List<Department> list) {
		for(Department d : trees) {
			Department copyDepart = new Department();
			copyDepart.setName(d.getName());
			copyDepart.setId(d.getId());
			
			copyDepart.setName(prefix + " " + copyDepart.getName());
			list.add(copyDepart);
			
			show(d.getChildren(), "　　" + prefix, list);  //全角空格
		}
	}
	
	public String addUI() throws Exception {
		List<Department> topList = departmentService.findRootList();
		
		List<Department> showDepartmentList = new ArrayList<Department>();
		
		
		show(topList, "", showDepartmentList);
		
		ActionContext.getContext().put("departmentList", showDepartmentList);
		
		return "saveUI";
	}
	
	public String updateUI() throws Exception {
		
		Department department = departmentService.findById(model.getId());
		
		List<Department> topList = departmentService.findRootList();
		
		List<Department> showDepartmentList = new ArrayList<Department>();
		show(topList, "", showDepartmentList);
		
		if(department.getParent() != null)
			parentId = department.getParent().getId();
		
		ActionContext.getContext().put("departmentList", showDepartmentList);
		ActionContext.getContext().getValueStack().push(department);
		
		return "saveUI";
	}
	
	public String toList() throws Exception {
		
		return "toList";
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getParentId() {
		return parentId;
	}

	
}
