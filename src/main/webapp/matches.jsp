<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
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
        <h1>Matches</h1>
        <c:if test="${not empty requestScope.errorMessage}">
            <p style="color: red;">${requestScope.errorMessage}</p>
        </c:if>
        <div class="input-container">
            <form id="filterForm" class="form-matches" method="get" action="${pageContext.request.contextPath}/matches"
                  style="display: flex; width: 100%; gap: 10px;">
                <input type="hidden" name="page" value="1"/>
                <input class="input-filter" placeholder="Filter by player name" type="text" name="filter_by_player_name"
                       value="${param.filter_by_player_name}"/>
                <button type="submit" class="btn-filter">Filter</button>
                <button type="button" class="btn-filter"
                        onclick="window.location.href='${pageContext.request.contextPath}/matches?page=1';">Reset Filter
                </button>
            </form>
        </div>

        <c:choose>
            <c:when test="${empty requestScope.matches}">
                <p>No matches found for your filter.</p>
            </c:when>
            <c:otherwise>
                <table class="table-matches">
                    <tr>
                        <th>Player One</th>
                        <th>Player Two</th>
                        <th>Winner</th>
                    </tr>
                    <c:forEach var="match" items="${requestScope.matches}">
                        <tr>
                            <td>${match.player1.name}</td>
                            <td>${match.player2.name}</td>
                            <td><span class="winner-name-td">${match.winner.name} 🏆</span></td>
                        </tr>
                    </c:forEach>
                </table>

                <div class="pagination">

                    <!-- Prev link -->
                    <c:if test="${requestScope.page > 1}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a class="prev"
                                   href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 1}&filter_by_player_name=${param.filter_by_player_name}">
                                    &lt; </a>
                            </c:when>
                            <c:otherwise>
                                <a class="prev"
                                   href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 1}">
                                    &lt; </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <!-- Pages before current -->
                    <c:if test="${requestScope.page > 2}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 2}&filter_by_player_name=${param.filter_by_player_name}"
                                   class="num-page">${requestScope.page - 2}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 2}"
                                   class="num-page">${requestScope.page - 2}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <c:if test="${requestScope.page > 1}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 1}&filter_by_player_name=${param.filter_by_player_name}"
                                   class="num-page">${requestScope.page - 1}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page - 1}"
                                   class="num-page">${requestScope.page - 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <!-- Current page -->
                    <a class="num-page current" href="#">
                            ${requestScope.page}
                    </a>

                    <!-- Pages after current -->
                    <c:if test="${requestScope.page + 1 <= requestScope.totalPages}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 1}&filter_by_player_name=${param.filter_by_player_name}"
                                   class="num-page">${requestScope.page + 1}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 1}"
                                   class="num-page">${requestScope.page + 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <c:if test="${requestScope.page + 2 <= requestScope.totalPages}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 2}&filter_by_player_name=${param.filter_by_player_name}"
                                   class="num-page">${requestScope.page + 2}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 2}"
                                   class="num-page">${requestScope.page + 2}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <!-- Next link -->
                    <c:if test="${requestScope.page < requestScope.totalPages}">
                        <c:choose>
                            <c:when test="${not empty param.filter_by_player_name}">
                                <a class="next"
                                   href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 1}&filter_by_player_name=${param.filter_by_player_name}">
                                    &gt; </a>
                            </c:when>
                            <c:otherwise>
                                <a class="next"
                                   href="${pageContext.request.contextPath}/matches?page=${requestScope.page + 1}">
                                    &gt; </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>