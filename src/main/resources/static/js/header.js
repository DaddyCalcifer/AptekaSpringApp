document.addEventListener('DOMContentLoaded', function() {
    // Проверяем наличие JWT токена в cookie
    const jwtToken = getCookieJwt('jwt');
    var cartSize = 228;
    if(jwtToken)
    fetch(`http://localhost:8080/api/cart/count?jwt=${jwtToken}`)
    .then(response => {
    if (!response.ok) {
        alert('Network response was not ok');
    }
        return response.json();
    })
    .then(data => {
        if(jwtToken)
            document.getElementById('cartSize').textContent = data;
        else
            document.getElementById('cartSize').style = "display: none;";
    })
    .catch(error => {
        alert('There was a problem with the fetch operation:', error);
    });

    // Получаем элементы для кнопок
    const header = document.getElementById('header');
    header.innerHTML = ``;
    // Если JWT токен существует, отображаем кнопки профиля и корзины
    if (jwtToken) {
        header.innerHTML = `
            <a href="index" style="margin: 0px 10px;">Главная</a>
            <a href="catalog" style="margin: 0px 10px;">Каталог</a>
            <a href="about" style="margin: 0px 10px;">О нас</a>
            <a href="contacts" style="margin: 0px 10px;">Контакты</a>
            <a href="profile" style="margin: 0px 10px;margin-left: 50px;">Профиль</a>
            <a href="cart" style="margin: 0px 10px;">Корзина <span id="cartSize">0</span></a>
        `;
    } else {
        header.innerHTML = `
            <a href="index" style="margin: 0px 10px;">Главная</a>
            <a href="catalog" style="margin: 0px 10px;">Каталог</a>
            <a href="about" style="margin: 0px 10px;">О нас</a>
            <a href="contacts" style="margin: 0px 10px;">Контакты</a>
            <a href="auth" style="margin: 0px 10px;">Авторизация</a>
        `;
    }
});

// Функция для получения cookie по имени
function getCookieJwt(name) {
    const cookieArr = document.cookie.split(';');
    for (let i = 0; i < cookieArr.length; i++) {
        const cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}
