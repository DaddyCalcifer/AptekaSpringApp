document.addEventListener('DOMContentLoaded', function() {
    // Проверяем наличие JWT токена в cookie
    const jwtToken = getCookieJwt('jwt');

    // Получаем элементы для кнопок
    document.getElementById('logout').addEventListener('click', function() {
        if (jwtToken) {
            if(confirm("Вы уверены, что хотите выйти из аккаунта?"))
            {
                setCookie('jwt',null,0);
                window.location.href = window.location.href.replace('/profile','/auth');
            }
        } 
        else 
        {
            alert("Невозможно выйти: Авторизация не выполнена!");
        }
    })
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
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + expires.toUTCString();
}
