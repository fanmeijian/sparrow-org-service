package cn.sparrowmini.org.service.scope;

public interface OrgScope extends PreserveScope{
	String type="org";
	String admin="admin";
	String SCOPE_ADMIN_CREATE = admin + ":" + type + ":create";
	String SCOPE_ADMIN_READ = admin + ":" + type + ":read";
	String SCOPE_ADMIN_UPDATE = admin + ":" + type + ":update";
	String SCOPE_ADMIN_DELETE = admin + ":" + type + ":delete";
	String SCOPE_ADMIN_TREE = admin + ":" + type + ":tree";
	
	String SCOPE_ADMIN_CHILD_LIST = admin + ":" + type + ":child:list";
	
	String SCOPE_ADMIN_PARENT_ADD = admin + ":" + type + ":parent:add";
	String SCOPE_ADMIN_PARENT_REMOVE = admin + ":" + type + ":parent:remove";
	String SCOPE_ADMIN_PARENT_LIST = admin + ":" + type + ":parent:list";

}
