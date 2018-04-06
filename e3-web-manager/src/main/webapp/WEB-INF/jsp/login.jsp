<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<c:set var="baseurl" value="${pageContext.request.contextPath}/"></c:set>
<html>
<head>
<TITLE>商城后台登录</TITLE>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
sc<script type="text/javascript" src="js/jquery-easyui-1.4.1/jquery.min.js"></script>
<LINK rel="stylesheet" type="text/css" href="styles/style.css">
<LINK rel="stylesheet" type="text/css" href="styles/login.css">
<LINK rel="stylesheet" type="text/css"	href="js/jquery-easyui-1.4.1/themes/default/easyui.css">
<LINK rel="stylesheet" type="text/css"	href="js/jquery-easyui-1.4.1/themes/icon.css">

<STYLE type="text/css">

.btnalink {
	cursor: hand;
	display: block;
	width: 80px;
	height: 29px;
	float: left;
	margin: 12px 28px 12px auto;
	line-height: 30px;
	background: url('${baseurl}images/login/btnbg.jpg') no-repeat;
	font-size: 14px;
	color: #fff;
	font-weight: bold;
	text-decoration: none;
}
</STYLE>

<script type="text/javascript">

	//登录提示方法
	function loginsubmit() {
		$("#loginform").submit();

	}
	
</SCRIPT>
</HEAD>
<BODY style="background: #f6fdff url(${baseurl}images/login/bg1.jpg) repeat-x;">
	<FORM id="loginform" name="loginform" action="user/login"
		method="post">
		<DIV class="logincon">

			<DIV class="title">
				<IMG alt="" src="${baseurl}images/login/logo1.png">
			</DIV>

			<DIV class="cen_con">
				<IMG alt="" src="${baseurl}images/login/bg2.png">
			</DIV>

			<DIV class="tab_con">

				<input type="password" style="display:none;" />
				<TABLE class="tab" border="0" cellSpacing="6" cellPadding="8">
					<TBODY>
						<TR>
							<TD>用户名：</TD>
							<TD colSpan="2"><input type="text" id="usercode"
								name="usercode" style="WIDTH: 130px" /></TD>
						</TR>
						<TR>
							<TD>密 码：</TD>
							<TD><input type="password" id="pwd" name="password" style="WIDTH: 130px" />
							</TD>
						</TR>
						<TR>
							<TD>验证码：</TD>
							<TD><input id="randomcode" name="randomcode" size="8" /> <img
								id="randomcode_img" src="validatecode" alt=""
								width="56" height="20" align='absMiddle' onclick="this.src='validatecode?d='+Math.random();"/> 
								<a href="" onclick="this.src='validatecode?d='+Math.random();">刷新</a></TD>
						</TR>

						<TR>
							<TD colSpan="2" align="center"><input type="button"
								class="btnalink" onclick="loginsubmit()" value="登&nbsp;&nbsp;录" />
								<input type="reset" class="btnalink" value="重&nbsp;&nbsp;置" /></TD>
						</TR>
					</TBODY>
				</TABLE>

			</DIV>
		</DIV>
	</FORM>
</BODY>
</HTML>
