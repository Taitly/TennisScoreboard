<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Error</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">

    <script src="js/app.js"></script>
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard 🎾</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <div class="centered-error">
            <h1>😕 Oops! Something went wrong.</h1>
            <h2>Error Code: ${requestScope.errorCode}</h2>
            <p>${requestScope.errorMessage}</p>
            <p>Make sure the URL is correct or try the options below:</p>
            <div class="error-actions">
                <a href="javascript:history.back()" class="btn-secondary">Go Back</a>
                <a href="${pageContext.request.contextPath}/" class="btn-primary">Go to Home</a>
            </div>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p> Project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd</a> roadmap</p>
    </div>
</footer>
</body>
</html>