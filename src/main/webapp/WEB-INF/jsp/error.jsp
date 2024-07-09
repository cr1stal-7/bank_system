<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="css/fontawesome/css/all.css">
    <style>
        *{
            box-sizing: border-box;
            font-family: "Arial Black";
        }
        body{
            height: 100vh;
            background-image: url("../resources/static/images/main.svg");
            background-size: cover;
            background-position: center center;
            background-repeat: no-repeat;
        }
        .card{
            box-shadow: 0px 3px 6px rgb(0, 14, 53);
        }
        .card .card-text{
            font-size: 16px;
        }
    </style>
    <title>Ошибка</title>
</head>
<body class="d-flex align-items-center justify-content-center">

    <div class="card col-4 alert alert-danger border border-danger text-danger">
        <h3 class="card-title">
            <i class="fa fa-window-close me-2"></i>Ошибка:
        </h3>
        <hr>
        <div class="card-body">
            <p class="card-text">
            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger text-center border border-danger">
                    <b>${requestScope.error}</b>
                </div>
            </c:if>
            </p>
            <hr>
            <a href="/login" class="btn btn-sm btn-danger">
                <i class="fa fa-arrow-alt-circle-left me-1"></i> Назад
            </a>
        </div>
    </div>
</body>
</html>