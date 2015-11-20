<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="default.css" media="screen"/>
    <title>檢視圖片</title>
</head>
<body>
<center>
    <img src='GetImageServlet.view?imageId=${param.imageId}&type=original'/>
    <br>
    <input type='submit' value='關閉' onclick='window.close()'/>
</center>
</body>
</html>