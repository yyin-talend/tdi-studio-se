function onResizeDocument () {
	var tabs = document.getElementsByClassName("tableStyle");
	var newWidth = (document.width - 210)+"px";
	tabs[0].style.width = newWidth;
	tabs[1].style.width = newWidth;
	tabs[2].style.width = newWidth;
	tabs[3].style.width = newWidth;
	tabs[4].style.width = newWidth;
	tabs[5].style.width = newWidth;
	tabs[6].style.width = newWidth;
}

function onchangeFileUpload(){
	var v = document.getElementsByClassName("myFileUpload")[0].value;
	document.getElementsByClassName("myFileUpload")[1].value = v;
}

function filterReWriteUrl() {
	var prefix = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&amp;class=org.talend.designer.components.exchange.ui.actions.FilterExtensionAction&amp;id=org.talend.designer.components.exchange.ui.actions.FilterExtensionAction&amp;text="
	var jsonObj = document.getElementById("avialable_filter_text").value;
	var url = prefix + jsonObj;
	document.getElementById('commit_filter').href = url;
}

function createNewExtensionWriteUrl() {

	String.prototype.replaceAll = function(s1, s2) {
		return this.replace(new RegExp(s1, "gm"), s2);
	}
	String.prototype.replaceAll2Excep = function(s1, s2) {
		var temp = this;
		while(temp.indexOf(s1) != -1) {
			temp = temp.replace(s1, s2);
		}
		return temp;
	}
	var prefix = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&amp;class=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;id=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;type=UPLOAD_MY_EXTENSION"

	var label = "label:" + "'" + document.getElementById("extension_label").value + "'";
	var lastVersionAvailable = "lastVersionAvailable:" + "'" + document.getElementById("lastVersionAvailable").value + "'";
	var description = "description:" + "'" + document.getElementById("extension_description").value + "'";
	description = description.replaceAll2Excep("\r\n", "\\n\\r");

	var checkedType = "";
	var filtervalue = "";
	var compatibilities = document.getElementsByName("compatibility");
	if(compatibilities[0].checked == true) {
		checkedType = "all"
		filtervalue = null;
	} else if(compatibilities[1].checked == true) {
		checkedType = "older"
		filtervalue = document.getElementById(checkedType).value;
	} else if(compatibilities[2].checked == true) {
		checkedType = "except"
		filtervalue = document.getElementById(checkedType).value;
	} else if(compatibilities[3].checked == true) {
		checkedType = "newer"
		filtervalue = document.getElementById(checkedType).value;
	} else if(compatibilities[4].checked == true) {
		checkedType = "only"
		filtervalue = document.getElementById(checkedType).value;
	}

	var listVersionCompatibles = "listVersionCompatibles:" + "'" + checkedType + "'";
	var filter = "filter:" + "'" + filtervalue + "'";
	var filename = "filename:" + "'" + document.getElementById("filename").value + "'";

	var jsonObj = "{" + label + "," + lastVersionAvailable + "," + description + "," + listVersionCompatibles + "," + filter + "," + filename + "}"
	jsonObj = jsonObj.replaceAll2Excep("\\", "/");
	var url = prefix + "&amp;extensionValues=" + jsonObj;
	document.getElementById('commit').href = url;
}

function updateReWriteUrl() {
	String.prototype.replaceAll2Excep = function(s1, s2) {
		var temp = this;
		while(temp.indexOf(s1) != -1) {
			temp = temp.replace(s1, s2);
		}
		return temp;
	}
	var prefix = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&amp;class=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;id=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;type=UPDATE_MY_EXTENSION"

	var lastVersionAvailable = "lastVersionAvailable:" + "'" + document.getElementById("lastVersionAvailable_update").value + "'";

	var checkedType = "";
	var filtervalue = "";
	var compatibilities = document.getElementsByName("compatibility_update");
	if(compatibilities[0].checked == true) {
		checkedType = "all"
		filtervalue = null;
	} else if(compatibilities[1].checked == true) {
		checkedType = "older"
		filtervalue = document.getElementById(checkedType + "_update").value;
	} else if(compatibilities[2].checked == true) {
		checkedType = "except"
		filtervalue = document.getElementById(checkedType + "_update").value;
	} else if(compatibilities[3].checked == true) {
		checkedType = "newer"
		filtervalue = document.getElementById(checkedType + "_update").value;
	} else if(compatibilities[4].checked == true) {
		checkedType = "only"
		filtervalue = document.getElementById(checkedType + "_update").value;
	}

	var listVersionCompatibles = "listVersionCompatibles:" + "'" + checkedType + "'";
	var filter = "filter:" + "'" + filtervalue + "'";
	var filename = "filename:" + "'" + document.getElementById("filename_update").value + "'";

	var jsonObj = "{" + lastVersionAvailable + "," + listVersionCompatibles + "," + filter + "," + filename + "}"
	jsonObj = jsonObj.replaceAll2Excep("\\", "/");
	var url = prefix + "&amp;extensionValues=" + jsonObj;
	document.getElementById('commit_update').href = url;

}

function modifyReWriteUrl() {
	var prefix = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&amp;class=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;id=org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionAction&amp;type=MODIFY_MY_EXTENSION"

	var leabel = "label:" + "'" + document.getElementById("extension_label_modify").value + "'"
	var description = "description:" + "'" + document.getElementById("extension_description_modify").value + "'"
	description = description.replaceAll2Excep("\r\n", "\\n\\r");
	var checkedType = "";
	var filtervalue = "";
	var compatibilities = document.getElementsByName("compatibility_modify");
	if(compatibilities[0].checked == true) {
		checkedType = "all"
		filtervalue = null;
	} else if(compatibilities[1].checked == true) {
		checkedType = "older"
		filtervalue = document.getElementById(checkedType + "_modify").value;
	} else if(compatibilities[2].checked == true) {
		checkedType = "except"
		filtervalue = document.getElementById(checkedType + "_modify").value;
	} else if(compatibilities[3].checked == true) {
		checkedType = "newer"
		filtervalue = document.getElementById(checkedType + "_modify").value;
	} else if(compatibilities[4].checked == true) {
		checkedType = "only"
		filtervalue = document.getElementById(checkedType + "_modify").value;
	}

	var listVersionCompatibles = "listVersionCompatibles:" + "'" + checkedType + "'";
	var filter = "filter:" + "'" + filtervalue + "'";
	var jsonObj = "{" + leabel + "," + description + "," + listVersionCompatibles + "," + filter + "}"

	var url = prefix + "&amp;extensionValues=" + jsonObj;
	document.getElementById('commit_modify').href = url;

}

//can't use for cycle in the javascript,each time call the length fuction ,get error in document parser
function setTab(tabIndex, pageId) {
	alert('test');
	var tli = document.getElementById("menu").getElementsByTagName("li");
	var mli = document.getElementById("main1").getElementsByTagName("ul");
	if(tabIndex == 0) {
		tli[0].className = "hover"
		tli[1].className = "leave"
		tli[2].className = "leave"
	} else if(tabIndex == 1) {
		tli[0].className = "leave"
		tli[1].className = "hover"
		tli[2].className = "leave"
	} else if(tabIndex == 2) {
		tli[0].className = "leave"
		tli[1].className = "leave"
		tli[2].className = "hover"
	}
	if(pageId == mli[0].id) {
		mli[0].className = "ulblock"
		mli[1].className = "ulhide"
		mli[2].className = "ulhide"
		mli[3].className = "ulhide"
		mli[4].className = "ulhide"
		mli[5].className = "ulhide"
		mli[6].className = "ulhide"
	} else if(pageId == mli[1].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulblock"
		mli[2].className = "ulhide"
		mli[3].className = "ulhide"
		mli[4].className = "ulhide"
		mli[5].className = "ulhide"
		mli[6].className = "ulhide"
	} else if(pageId == mli[2].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulhide"
		mli[2].className = "ulblock"
		mli[3].className = "ulhide"
		mli[4].className = "ulhide"
		mli[5].className = "ulhide"
		mli[6].className = "ulhide"
	} else if(pageId == mli[3].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulhide"
		mli[2].className = "ulhide"
		mli[3].className = "ulblock"
		mli[4].className = "ulhide"
		mli[5].className = "ulhide"
		mli[6].className = "ulhide"
	} else if(pageId == mli[4].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulhide"
		mli[2].className = "ulhide"
		mli[3].className = "ulhide"
		mli[4].className = "ulblock"
		mli[5].className = "ulhide"
		mli[6].className = "ulhide"
	} else if(pageId == mli[5].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulhide"
		mli[2].className = "ulhide"
		mli[3].className = "ulhide"
		mli[4].className = "ulhide"
		mli[5].className = "ulblock"
		mli[6].className = "ulhide"
	} else if(pageId == mli[6].id) {
		mli[0].className = "ulhide"
		mli[1].className = "ulhide"
		mli[2].className = "ulhide"
		mli[3].className = "ulhide"
		mli[4].className = "ulhide"
		mli[5].className = "ulhide"
		mli[6].className = "ulblock"
	}

}