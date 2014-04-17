package cn.ts987.oa.domain;

import org.activiti.engine.task.Task;

/**
 * 只是在“待我审批”页面中显示数据时使用，不对应数据表
 * 
 * @author fgd
 * 
 */
public class TaskView {
	private Task task; // 当前审批任务
	private Form form; // 当前待审批的表单

	
	public TaskView(Task task, Form form) {
		this.task = task;
		this.form = form;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
