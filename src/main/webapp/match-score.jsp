<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Match Score</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Mono:wght@300&display=swap" rel="stylesheet">
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
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches?page=1">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Current match</h1>
        <div class="current-match-image"></div>
        <section class="score">
            <table class="table">
                <thead class="result">
                <tr>
                    <th class="table-text">Player</th>
                    <th class="table-text">Sets</th>
                    <th class="table-text">Games</th>
                    <th class="table-text">Points</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr class="player1">
                    <td class="table-text">${requestScope.matchScoreDto.firstPlayerScore.player.name}</td>
                    <td class="table-text">${requestScope.matchScoreDto.firstPlayerScore.setsWon}</td>
                    <td class="table-text">${requestScope.matchScoreDto.firstPlayerScore.gamesWon}</td>
                    <td class="table-text">
                        <c:choose>
                            <c:when test="${requestScope.matchScoreDto.firstPlayerScore.gamesWon == 6 and requestScope.matchScoreDto.secondPlayerScore.gamesWon == 6}">
                                ${requestScope.matchScoreDto.firstPlayerScore.tieBreakPoints}
                            </c:when>

                            <c:otherwise>
                                ${requestScope.matchScoreDto.firstPlayerScore.currentPoint.point}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="table-text">
                        <form method="post" action="${pageContext.request.contextPath}/match-score?uuid=${requestScope.uuid}">
                            <input type="hidden" name="playerName" value="${requestScope.matchScoreDto.firstPlayerScore.player.name}">
                            <button class="score-btn" type="submit">Score</button>
                        </form>
                    </td>
                </tr>
                <tr class="player2">
                    <td class="table-text">${requestScope.matchScoreDto.secondPlayerScore.player.name}</td>
                    <td class="table-text">${requestScope.matchScoreDto.secondPlayerScore.setsWon}</td>
                    <td class="table-text">${requestScope.matchScoreDto.secondPlayerScore.gamesWon}</td>
                    <td class="table-text">
                        <c:choose>
                            <c:when test="${requestScope.matchScoreDto.firstPlayerScore.gamesWon == 6 and requestScope.matchScoreDto.secondPlayerScore.gamesWon == 6}">
                                ${requestScope.matchScoreDto.secondPlayerScore.tieBreakPoints}
                            </c:when>

                            <c:otherwise>
                                ${requestScope.matchScoreDto.secondPlayerScore.currentPoint.point}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="table-text">
                        <form method="post" action="${pageContext.request.contextPath}/match-score?uuid=${requestScope.uuid}">
                            <input type="hidden" name="playerName" value="${requestScope.matchScoreDto.secondPlayerScore.player.name}">
                            <button class="score-btn" type="submit">Score</button>
                        </form>
                    </td>
                </tr>
                </tbody>
            </table>
        </section>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.</p>
    </div>
</footer>
</body>
</html>