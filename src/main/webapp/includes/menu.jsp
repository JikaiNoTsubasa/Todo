<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="sb-menu-bar">
    <a href="index">Home</a>
    <a href="archive">Archive</a>
    <a href="person">Persons</a>
    <a href="admin">Admin</a>

    <div class="sb-menu-right">
        v1.0.4 <a href="disconnect"><s:property value="#session['user'].name"></s:property></a>
    </div>
</div>
